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
class FilePathComponent_file : StringSpec() { init {

    "Should create file component when given a name" {
        // Act
        val component = FilePathComponent.file("myFile")

        // Assert
        component.name shouldBe "myFile"
        component.kind shouldBe FilePathComponentKind.File
        component.isSpecial shouldBe false
    }

    "Should throw when given an empty string" {
        // Act/Assert
        shouldThrow<IllegalArgumentException> {
            FilePathComponent.file("")
        }
    }
} }