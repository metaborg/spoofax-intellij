package org.metaborg.intellij.idea.files

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import com.intellij.psi.LanguageSubstitutor
import com.intellij.psi.LanguageSubstitutors
import com.intellij.psi.tree.IFileElementType
import com.virtlink.editorservices.intellij.files.AesiFileType
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType
import org.metaborg.intellij.idea.graphics.IIconManager
import org.metaborg.intellij.idea.languages.IIdeaLanguageManager
import org.metaborg.intellij.idea.languages.SpoofaxIdeaLanguage
import org.metaborg.intellij.idea.languages.defaultExtension
import org.metaborg.intellij.idea.languages.extensions
import org.metaborg.intellij.idea.parsing.MetaborgAesiParserDefinition
import javax.swing.Icon

/**
 * The file type of a Spoofax file.
 *
 * Every Spoofax language gets its own [SpoofaxFileType].
 */
class SpoofaxFileType @Inject constructor(
        @Assisted language: SpoofaxIdeaLanguage,
        private val iconManager: IIconManager)
    : AesiFileType(language) {

    companion object {
        /**
         * The dummy extension that is used when the default extension could not be determined.
         *
         * Should not have a leading dot.
         */
        const val DUMMY_EXTENSION = "spoofax"
    }

    interface IFactory {
        fun create(language: SpoofaxIdeaLanguage)
                : SpoofaxFileType
    }

    /**
     * Gets the Spoofax language of this file type.
     */
    val spoofaxLanguage: SpoofaxIdeaLanguage get() = super.getLanguage() as SpoofaxIdeaLanguage

    override fun getName(): String {
        // TODO SPOOFAX: This should be a human-readable custom name, a 'displayName'.
        return this.spoofaxLanguage.language.name()
    }

    override fun getDescription(): String {
        // TODO SPOOFAX: This should be a custom description (where this may be the fallback if none is specified).
        return "$name (Spoofax)"
    }

    override fun getDefaultExtension(): String {
        return this.spoofaxLanguage.language.defaultExtension ?: DUMMY_EXTENSION
    }

    override val extensions: Set<String>
        get() = this.spoofaxLanguage.language.extensions

    override fun getIcon(): Icon? {
        // TODO SPOOFAX: Spoofax should allow a custom icon for the language.
        return this.iconManager.getLanguageFileIcon(this.spoofaxLanguage.language)
    }
}