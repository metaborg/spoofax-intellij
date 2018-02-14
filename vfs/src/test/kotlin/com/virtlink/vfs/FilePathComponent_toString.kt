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
class FilePathComponent_toString : StringSpec() { init {

    "Should return a proper path" {
        val table = table(
                headers("input", "result"),
                row(FilePathComponent.file("myFile"), "myFile"),
                row(FilePathComponent.folder("myFolder"), "myFolder/"),
                row(FilePathComponent.root, "/"),
                row(FilePathComponent.current, "./"),
                row(FilePathComponent.parent, "../")
        )
        forAll(table) { input, result ->
            // Act
            val str = input.toString()

            // Assert
            str shouldBe result
        }
    }
} }