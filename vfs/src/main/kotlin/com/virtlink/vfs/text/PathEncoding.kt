package com.virtlink.vfs.text

import com.virtlink.vfs.FilePath
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder
import java.util.*

/**
 * Encodes and decodes names in a path.
 */
class PathEncoding: IEncoding {

    companion object {
        @JvmField val DEFAULT_CHARSET = Charsets.UTF_8
    }

    /**
     * Encodes a name in a path.
     */
    override fun encode(unencoded: String): String {
        val encoder = DEFAULT_CHARSET.newEncoder()
        val byteBuffer = ByteBuffer.allocate(4)
        val sb = StringBuilder(unencoded.length * 2)

        // Encode as many characters as we can
        var i = 0
        while (i < unencoded.length) {
            if (!isAllowedCharacter(unencoded[i])) {
                // Encode the character
                val step = encodeChar(unencoded, i, sb, encoder, byteBuffer)
                i += step
            } else {
                // Copy the character unchanged
                sb.append(unencoded[i])
                i += 1
            }
        }

        // Avoid creating an illegal name
        val encoded = sb.toString()
        return if (!isAllowedName(encoded)) {
            // Encode the first character
            sb.setLength(0)
            val step = encodeChar(encoded, 0, sb, encoder, byteBuffer)
            sb.append(encoded.substring(step))
            sb.toString()
        } else {
            encoded
        }
    }

    /**
     * Decodes a name in a path.
     */
    override fun decode(encoded: String): String {
        val decoder = DEFAULT_CHARSET.newDecoder()
        val byteBuffer = ByteBuffer.allocate(4)
        val charBuffer = CharBuffer.allocate(4)
        val sb = StringBuilder(encoded.length)

        var i = 0
        while (i < encoded.length) {
            if (encoded[i] == '%') {
                // Decode the character
                val step = decodeChars(encoded, i, sb, decoder, byteBuffer, charBuffer)
                i += step
            } else {
                // Copy the character unchanged
                sb.append(encoded[i])
                i += 1
            }
        }

        return sb.toString()
    }

    /**
     * Encodes a single character or surrogate pair of characters.
     *
     * @param unencoded The un-encoded string.
     * @param index The zero-based index in the un-encoded string of the character to encode.
     * @param output The [StringBuilder] to which to write the character.
     * @param encoder The character set encoder to use.
     * @param buffer The buffer to use.
     */
    private fun encodeChar(unencoded: String, index: Int, output: StringBuilder, encoder: CharsetEncoder, buffer: ByteBuffer): Int {
        buffer.clear()
        encoder.reset()

        // Determine how many characters we're encoding.
        // If a surrogate pair, encode both. Otherwise, encode one.
        val charCount = if (index < unencoded.length - 1 && Character.isSurrogatePair(unencoded[index], unencoded[index + 1]))
            2 else 1
        // Encode the character (or pair of characters).  This results in one or more bytes.
        val result = encoder.encode(CharBuffer.wrap(unencoded, index, charCount), buffer, true)
        if (!result.isUnderflow) result.throwException()
        encoder.flush(buffer)

        // Write each byte to the output.
        buffer.flip()
        val formatter = Formatter(output)
        while (buffer.remaining() > 0) {
            output.append('%')
            formatter.format("%02X", buffer.get())
        }
        return charCount
    }

    /**
     * Decodes as many characters as possible.
     *
     * @param encoded The encoded string.
     * @param index The zero-based index in the encoded string of the first character to decode.
     * @param output The [StringBuilder] to which to write the characters.
     * @param decoder The character set decoder to use.
     * @param byteBuffer The byte buffer to use.
     * @param charBuffer The character buffer to use.
     */
    private fun decodeChars(encoded: String, index: Int, output: StringBuilder, decoder: CharsetDecoder, byteBuffer: ByteBuffer, charBuffer: CharBuffer): Int {
        // Read as many encoded characters as we can into the buffer.
        decoder.reset()
        byteBuffer.clear()
        charBuffer.clear()
        var read = 0
        var success: Boolean
        do {
            success = tryReadEncodedByte(encoded, index + read, byteBuffer)
            if (success) read += 3
        } while (success)

        // Decode all the characters in the buffer
        byteBuffer.flip()
        if (byteBuffer.limit() > 0) {
            val result = decoder.decode(byteBuffer, charBuffer, true)
            if (!result.isUnderflow) result.throwException()
            decoder.flush(charBuffer)
            charBuffer.flip()
            output.append(charBuffer)
        }

        return read
    }

    /**
     * Attempts to read a percent encoded byte, e.g. `%20`, as a byte into the specified buffer.
     *
     * @param encoded The encoded string to read from.
     * @param index The zero-based index of the start of the encoded byte.
     * @param buffer The buffer to which to add the read byte.
     * @return True when an encoded byte was found and read;
     * otherwise, false.
     */
    private fun tryReadEncodedByte(encoded: String, index: Int, buffer: ByteBuffer): Boolean {
        // When we're past the end of the input, or we are at an un-encoded character: stop.
        if (index >= encoded.length || encoded[index] != '%')
            return false

        if (index + 3 > encoded.length)
            throw IllegalStateException("Percent encoded ${DEFAULT_CHARSET.name()} character ended prematurely.")

        val result = tryParseInt(encoded.substring(index + 1, index + 3), 16)
            ?: throw IllegalStateException("Percent encoded ${DEFAULT_CHARSET.name()} character was not encoded properly.")

        buffer.put(result.toByte())

        return true
    }

    /**
     * Determines whether the given character is allowed in an encoded name.
     *
     * Disallowed characters are: control characters, and the characters `%`, `/` and `!`.
     *
     * @param char The character.
     * @return True when the character is allowed; otherwise, false.
     */
    private fun isAllowedCharacter(char: Char): Boolean {
        return !char.isISOControl() && when(char) {
            '%', '/', '!' -> false
            else -> true
        }
    }

    /**
     * Determines whether the given name is allowed as an encoded name.
     *
     * While the individual characters of a name need not be encoded,
     * the name as a whole can still be disallowed.
     *
     * @param name The encoded name.
     * @return True when the encoded name is allowed; otherwise, false.
     */
    private fun isAllowedName(name: String): Boolean {
        return when (name) {
            FilePath.PARENT_FOLDER,
            FilePath.CURRENT_FOLDER -> false
            else -> true
        }
    }

    /**
     * Attempts to parse an integer from the specified string.
     *
     * @param s The string.
     * @param radix The radix.
     * @return The integer; or null when parsing failed.
     */
    private fun tryParseInt(s: String, radix: Int): Int? {
        return try {
            Integer.parseInt(s, radix)
        } catch (_: NumberFormatException) {
            null
        }
    }
}