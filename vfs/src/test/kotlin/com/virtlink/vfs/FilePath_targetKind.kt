package com.virtlink.vfs

import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec


@Suppress("ClassName")
class FilePath_targetKind : StringSpec() { init {

    "Should return correct kind for given path" {
        val table = table(
                headers("path", "expected"),
                row("/foo/bar.txt", FilenameKind.File),
                row("/foo/bar/", FilenameKind.Folder),
                row("..", FilenameKind.Folder),
                row(".", FilenameKind.Folder),
                row("/", FilenameKind.Folder),
                row("", FilenameKind.Unspecified)
        )
        forAll(table) { path, expected ->
            // Assert
            val filePath = FilePath.parse(path)

            // Assert
            filePath.targetKind shouldBe expected
        }
    }

} }