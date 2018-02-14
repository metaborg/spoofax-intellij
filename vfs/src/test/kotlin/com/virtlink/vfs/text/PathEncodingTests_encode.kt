package com.virtlink.vfs.text

import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec

@Suppress("ClassName")
class PathEncodingTests_encode : StringSpec() { init {

    "Should encode control characters" {
        val table = table(
                headers("unencoded", "result"),
                row("\u0000", "%00")        // U+0
        )
        forAll(table) { unencoded, result ->
            // Arrange
            val encoding = createNew()

            // Act
            val encoded = encoding.encode(unencoded)

            // Assert
            encoded.toUpperCase() shouldBe result
        }
    }

    "Should encode reserved characters" {
        val table = table(
                headers("unencoded", "result"),
                row("!", "%21"),        // U+21
                row("%", "%25"),        // U+25
                row("/", "%2F")         // U+2F
        )
        forAll(table) { unencoded, result ->
            // Arrange
            val encoding = createNew()

            // Act
            val encoded = encoding.encode(unencoded)

            // Assert
            encoded.toUpperCase() shouldBe result
        }
    }

    "Should not encode normal characters" {
        val table = table(
                headers("unencoded", "result"),
                row("a", "a"),
                row("E", "E"),
                row("æ", "æ"),
                row("&", "&")
        )
        forAll(table) { unencoded, result ->
            // Arrange
            val encoding = createNew()

            // Act
            val encoded = encoding.encode(unencoded)

            // Assert
            encoded shouldBe result
        }
    }

    }

    private fun createNew(): PathEncoding
            = PathEncoding()
}