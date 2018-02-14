package com.virtlink.vfs.text

import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec

@Suppress("ClassName")
class PathEncodingTests_decode : StringSpec() { init {

    "Should decode control characters" {
        val table = table(
                headers("encoded", "result"),
                row("%00", "\u0000")        // U+0
        )
        forAll(table) { encoded, result ->
            // Arrange
            val encoding = createNew()

            // Act
            val decoded = encoding.decode(encoded)

            // Assert
            decoded.toUpperCase() shouldBe result
        }
    }

    "Should decode reserved characters" {
        val table = table(
                headers("encoded", "result"),
                row("%21", "!"),        // U+21
                row("%25", "%"),        // U+25
                row("%2F", "/")         // U+2F
        )
        forAll(table) { encoded, result ->
            // Arrange
            val encoding = createNew()

            // Act
            val decoded = encoding.decode(encoded)

            // Assert
            decoded.toUpperCase() shouldBe result
        }
    }

    "Should not decode normal characters" {
        val table = table(
                headers("unencoded", "result"),
                row("a", "a"),
                row("E", "E"),
                row("æ", "æ"),
                row("&", "&")
        )
        forAll(table) { encoded, result ->
            // Arrange
            val encoding = createNew()

            // Act
            val decoded = encoding.decode(encoded)

            // Assert
            decoded shouldBe result
        }
    }

    }

    private fun createNew(): PathEncoding
            = PathEncoding()
}