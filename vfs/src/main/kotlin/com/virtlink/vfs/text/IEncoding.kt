package com.virtlink.vfs.text

/**
 * Encodes and decodes strings.
 */
interface IEncoding {

    /**
     * Encodes a string.
     *
     * @param decoded The decoded string.
     * @return The encoded string.
     */
    fun encode(decoded: String): String

    /**
     * Decodes a string.
     *
     * @param encoded The encoded string.
     * @return The decoded string.
     */
    fun decode(encoded: String): String

}