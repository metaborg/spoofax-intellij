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
class FilePath_components : StringSpec() { init {

    "Should return the normalized components for paths" {
        val table = table(
                headers("path", "expected"),
                row("foo/bar.txt", listOf(folder("foo"), file("bar.txt"))),
                row("foo/baz/../bar.txt", listOf(folder("foo"), file("bar.txt"))),
                row("bar.txt", listOf(file("bar.txt"))),
                row("foo/", listOf(folder("foo"))),
                row("../", listOf(parent)),
                row("./", listOf(current)),
                row("/foo/bar.txt", listOf(root, folder("foo"), file("bar.txt"))),
                row("/foo/baz/../bar.txt", listOf(root, folder("foo"), file("bar.txt"))),
                row("/bar.txt", listOf(root, file("bar.txt"))),
                row("/foo/", listOf(root, folder("foo"))),
                row("/", listOf(root))
        )
        forAll(table) { path, expected ->
            // Assert
            val filePath = FilePath.parse(path)

            // Assert
            filePath.components shouldBe expected
        }
    }

} }