package com.virtlink.vfs

import com.virtlink.vfs.FilePathComponent.Companion.folder
import com.virtlink.vfs.FilePathComponent.Companion.file
import com.virtlink.vfs.FilePathComponent.Companion.root
import com.virtlink.vfs.FilePathComponent.Companion.current
import com.virtlink.vfs.FilePathComponent.Companion.parent
import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec


@Suppress("ClassName")
class FilePath_parse : StringSpec() { init {

    "Should parse and normalize the path" {
        val table = table(
                headers("path", "expected"),
                row("foo/bar/baz.txt", "foo/bar/baz.txt"),
                row("foo/bar/../baz.txt", "foo/baz.txt"),
                row("foo/bar/./baz.txt", "foo/bar/baz.txt"),
                row("foo/bar/../bar/baz.txt", "foo/bar/baz.txt"),
                row("foo/bar/../../baz.txt", "baz.txt"),
                row("foo/bar/../../../../baz.txt", "../../baz.txt"),
                row("foo/bar/.", "foo/bar/"),
                row("foo/./bar/./baz.txt", "foo/bar/baz.txt"),
                row("./foo/bar/baz.txt", "foo/bar/baz.txt"),
                row("../foo/bar/baz.txt", "../foo/bar/baz.txt"),
                row("../../..", "../../../"),
                row("././.", "./"),
                row("", ""),
                row("/foo/bar/baz.txt", "/foo/bar/baz.txt"),
                row("/foo/bar/../baz.txt", "/foo/baz.txt"),
                row("/foo/bar/./baz.txt", "/foo/bar/baz.txt"),
                row("/foo/bar/../bar/baz.txt", "/foo/bar/baz.txt"),
                row("/foo/bar/../../baz.txt", "/baz.txt"),
                row("/foo/bar/../../../../baz.txt", "/baz.txt"),
                row("/foo/bar/.", "/foo/bar/"),
                row("/foo/./bar/./baz.txt", "/foo/bar/baz.txt"),
                row("/./foo/bar/baz.txt", "/foo/bar/baz.txt"),
                row("/../foo/bar/baz.txt", "/foo/bar/baz.txt"),
                row("/../../..", "/"),
                row("/././.", "/"),
                row("/", "/")
        )
        forAll(table) { path, expected ->
            // Assert
            val filePath = FilePath.parse(path)

            // Assert
            filePath.toString() shouldBe expected
        }
    }

} }