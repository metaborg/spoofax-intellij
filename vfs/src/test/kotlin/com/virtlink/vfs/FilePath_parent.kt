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
class FilePath_parent : StringSpec() { init {

    "Should get the parent" {
        val table = table(
                headers("path", "expected"),
                // Normal cases: (just remove the last component)
                row("foo/bar/baz.txt", "foo/bar/"),
                row("foo/baz.txt", "foo/"),
                row("../../baz.txt", "../../"),
                row("foo/bar/", "foo/"),
                row("../foo/bar/baz.txt", "../foo/bar/"),
                row("/foo/bar/baz.txt", "/foo/bar/"),
                row("/foo/baz.txt", "/foo/"),
                row("/baz.txt", "/"),
                row("/foo/bar/", "/foo/"),
                // Special cases:
                row("../../../", "../../../../"),
                row("baz.txt", "./"),
                row("foo/", "./"),
                row("./", "../"),
                row("/", "/"),
                row("", "")
        )
        forAll(table) { path, expected ->
            // Assert
            val filePath = FilePath.parse(path)

            // Assert
            filePath.parent?.toString() shouldBe expected
        }
    }

} }