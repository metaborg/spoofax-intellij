package com.virtlink.vfs

import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.matchers.shouldThrowAny
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec


@Suppress("ClassName")
class FilePathComponent_isSpecial : StringSpec() { init {

    "Should return false for files and folders" {
        val table = table(
                headers("input", "expected"),
                row(FilePathComponent.file("myFile"), false),
                row(FilePathComponent.folder("myFolder"), false),
                row(FilePathComponent.root, true),
                row(FilePathComponent.current, true),
                row(FilePathComponent.parent, true)
        )
        forAll(table) { input, expected ->
            // Act
            val result = input.isSpecial

            // Assert
            result shouldBe expected
        }
    }
} }