package org.metaborg.intellij.idea.languages

import com.intellij.lang.Language
import org.metaborg.core.language.*

abstract class SpoofaxIdeaLanguage(
        val language: ILanguage
): Language(language.name()) {

    override fun toString(): String
            = language.activeImpl()?.id()?.toString() ?: language.name()

}