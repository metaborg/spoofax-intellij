package com.virtlink.vfs

import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec


@Suppress("ClassName")
class FilePath_append : StringSpec() { init {

    "Should append the path" {
        val table = table(
                headers("base", "append", "expected"),
                // Absolute
                row("foo/bar/baz.txt", "/qix/fra.txt", "/qix/fra.txt"),
                row("foo/bar/baz/", "/qix/fra.txt", "/qix/fra.txt"),
                row("foo/bar/baz/", "/", "/"),

                // Relative to a nested file
                row("foo/bar/baz.txt", "foo/bar/", "foo/bar/foo/bar/"),
                row("foo/bar/baz.txt", "foo/bar.txt", "foo/bar/foo/bar.txt"),
                row("foo/bar/baz.txt", "foo/", "foo/bar/foo/"),
                row("foo/bar/baz.txt", "bar.txt", "foo/bar/bar.txt"),
                row("foo/bar/baz.txt", "./", "foo/bar/"),
                row("foo/bar/baz.txt", "../", "foo/"),
                row("foo/bar/baz.txt", "../qix/", "foo/qix/"),
                row("foo/bar/baz.txt", "../qix/fra.txt", "foo/qix/fra.txt"),
                row("foo/bar/baz.txt", "../../../fra.txt", "../fra.txt"),
                row("foo/bar/baz.txt", "", "foo/bar/baz.txt"),

                // Relative to a nested folder
                row("foo/bar/baz/", "foo/bar/", "foo/bar/baz/foo/bar/"),
                row("foo/bar/baz/", "foo/bar.txt", "foo/bar/baz/foo/bar.txt"),
                row("foo/bar/baz/", "foo/", "foo/bar/baz/foo/"),
                row("foo/bar/baz/", "bar.txt", "foo/bar/baz/bar.txt"),
                row("foo/bar/baz/", "./", "foo/bar/baz/"),
                row("foo/bar/baz/", "../", "foo/bar/"),
                row("foo/bar/baz/", "../qix/", "foo/bar/qix/"),
                row("foo/bar/baz/", "../qix/fra.txt", "foo/bar/qix/fra.txt"),
                row("foo/bar/baz/", "../../../fra.txt", "fra.txt"),
                row("foo/bar/baz/", "", "foo/bar/baz/"),

                // Relative to a single file
                row("baz.txt", "foo/bar/", "foo/bar/"),
                row("baz.txt", "foo/bar.txt", "foo/bar.txt"),
                row("baz.txt", "foo/", "foo/"),
                row("baz.txt", "bar.txt", "bar.txt"),
                row("baz.txt", "./", "./"),
                row("baz.txt", "../", "../"),
                row("baz.txt", "../qix/", "../qix/"),
                row("baz.txt", "../qix/fra.txt", "../qix/fra.txt"),
                row("baz.txt", "../../../fra.txt", "../../../fra.txt"),
                row("baz.txt", "", "baz.txt"),

                // Relative to a single folder
                row("foo/", "foo/bar/", "foo/foo/bar/"),
                row("foo/", "foo/bar.txt", "foo/foo/bar.txt"),
                row("foo/", "foo/", "foo/foo/"),
                row("foo/", "bar.txt", "foo/bar.txt"),
                row("foo/", "./", "foo/"),
                row("foo/", "../", "./"),
                row("foo/", "../qix/", "qix/"),
                row("foo/", "../qix/fra.txt", "qix/fra.txt"),
                row("foo/", "../../../fra.txt", "../../fra.txt"),
                row("foo/", "", "foo/"),

                // Relative to the current folder
                row("./", "foo/bar/", "foo/bar/"),
                row("./", "foo/bar.txt", "foo/bar.txt"),
                row("./", "foo/", "foo/"),
                row("./", "bar.txt", "bar.txt"),
                row("./", "./", "./"),
                row("./", "../", "../"),
                row("./", "../qix/", "../qix/"),
                row("./", "../qix/fra.txt", "../qix/fra.txt"),
                row("./", "../../../fra.txt", "../../../fra.txt"),
                row("./", "", "./"),

                // Relative to the parent folder
                row("../", "foo/bar/", "../foo/bar/"),
                row("../", "foo/bar.txt", "../foo/bar.txt"),
                row("../", "foo/", "../foo/"),
                row("../", "bar.txt", "../bar.txt"),
                row("../", "./", "../"),
                row("../", "../", "../../"),
                row("../", "../qix/", "../../qix/"),
                row("../", "../qix/fra.txt", "../../qix/fra.txt"),
                row("../", "../../../fra.txt", "../../../../fra.txt"),
                row("../", "", "../"),

                // Relative to the root
                row("/", "foo/bar/", "/foo/bar/"),
                row("/", "foo/bar.txt", "/foo/bar.txt"),
                row("/", "foo/", "/foo/"),
                row("/", "bar.txt", "/bar.txt"),
                row("/", "./", "/"),
                row("/", "../", "/"),
                row("/", "../qix/", "/qix/"),
                row("/", "../qix/fra.txt", "/qix/fra.txt"),
                row("/", "../../../fra.txt", "/fra.txt"),
                row("/", "", "/")
        )
        forAll(table) { base, append, expected ->
            // Assert
            val basePath = FilePath.parse(base)
            val appendPath = FilePath.parse(append)

            // Act
            val result = basePath.append(appendPath)

            // Assert
            result.toString() shouldBe expected
        }
    }

} }