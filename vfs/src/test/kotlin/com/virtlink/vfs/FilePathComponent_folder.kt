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
class FilePathComponent_folder : StringSpec() { init {

    "Should create folder component when given a name" {
        // Act
        val component = FilePathComponent.folder("myFolder")

        // Assert
        component.name shouldBe "myFolder"
        component.kind shouldBe FilePathComponentKind.Folder
        component.isSpecial shouldBe false
    }

    "Should throw when given an empty string" {
        // Act/Assert
        shouldThrow<IllegalArgumentException> {
            FilePathComponent.folder("")
        }
    }
} }