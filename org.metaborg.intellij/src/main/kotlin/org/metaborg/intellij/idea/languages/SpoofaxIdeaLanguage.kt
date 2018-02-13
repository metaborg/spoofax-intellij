package org.metaborg.intellij.idea.languages

import com.intellij.lang.Language
import com.virtlink.editorservices.intellij.languages.AesiLanguage
import org.metaborg.core.language.*

abstract class SpoofaxIdeaLanguage(
        val language: ILanguage
): AesiLanguage(language.name()) {

    override fun toString(): String
            = language.activeImpl()?.id()?.toString() ?: language.name()

}