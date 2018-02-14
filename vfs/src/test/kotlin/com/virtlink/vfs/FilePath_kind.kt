package com.virtlink.vfs

import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec


@Suppress("ClassName")
class FilePath_kind : StringSpec() { init {

    "Should return Relative for relative paths" {
        val table = table(
                headers("path"),
                row("foo/bar.txt"),
                row("foo/bar/"),
                row("foo/"),
                row("bar.txt"),
                row("foo/bar/../../../"),
                row("foo/../../../"),
                row("../../../"),
                row("../foo/bar.txt"),
                row("../foo/bar/"),
                row("../foo/"),
                row("../bar.txt"),
                row("./foo/bar.txt"),
                row("./foo/bar/"),
                row("./foo/"),
                row("./bar.txt"),
                row("../"),
                row("./"),
                row(".."),
                row("."),
                row("")
        )
        forAll(table) { path ->
            // Assert
            val filePath = FilePath.parse(path)

            // Assert
            filePath.isAbsolute shouldBe false
            filePath.isRelative shouldBe true
            filePath.kind shouldBe FilePathKind.Relative
        }
    }

    "Should return Absolute for absolute paths" {
        val table = table(
                headers("path"),
                row("/foo/bar.txt"),
                row("/foo/bar/"),
                row("/foo/"),
                row("/bar.txt"),
                row("/foo/bar/../../../"),
                row("/foo/../../../"),
                row("/../../../"),
                row("/../foo/bar.txt"),
                row("/../foo/bar/"),
                row("/../foo/"),
                row("/../bar.txt"),
                row("/./foo/bar.txt"),
                row("/./foo/bar/"),
                row("/./foo/"),
                row("/./bar.txt"),
                row("/../"),
                row("/./"),
                row("/.."),
                row("/."),
                row("/")
        )
        forAll(table) { path ->
            // Assert
            val filePath = FilePath.parse(path)

            // Assert
            filePath.isAbsolute shouldBe true
            filePath.isRelative shouldBe false
            filePath.kind shouldBe FilePathKind.Absolute
        }
    }
} }