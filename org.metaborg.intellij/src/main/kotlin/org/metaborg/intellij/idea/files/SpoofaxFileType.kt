package org.metaborg.intellij.idea.files

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.openapi.fileTypes.LanguageFileType
import com.virtlink.editorservices.intellij.files.IFileTypeExt
import org.metaborg.intellij.idea.graphics.IIconManager
import org.metaborg.intellij.idea.languages.SpoofaxIdeaLanguage
import org.metaborg.intellij.idea.languages.defaultExtension
import org.metaborg.intellij.idea.languages.extensions
import javax.swing.Icon

/**
 * The file type of a Spoofax file.
 *
 * Every Spoofax language gets its own [SpoofaxFileType].
 */
class SpoofaxFileType @Inject constructor(
        @Assisted language: SpoofaxIdeaLanguage,
        private val iconManager: IIconManager)
    : LanguageFileType(language), IFileTypeExt {

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
        // NOTE: This is the ID of the language.
        return this.spoofaxLanguage.language.name()
    }

    override fun getDescription(): String {
        // NOTE: This is displayed as the name of the language.
        // TODO SPOOFAX: This should be a human-readable custom name, a 'displayName'.
        return "$name (Spoofax)"
    }

    override fun getIcon(): Icon? {
        // TODO SPOOFAX: Spoofax should allow a custom icon for the language.
        return this.iconManager.getLanguageFileIcon(this.spoofaxLanguage.language)
    }

    override fun getDefaultExtension(): String {
        return this.spoofaxLanguage.language.defaultExtension ?: DUMMY_EXTENSION
    }

    override val extensions: Set<String>
        get() = this.spoofaxLanguage.language.extensions
}