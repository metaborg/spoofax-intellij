package org.metaborg.intellij.idea.files

import com.google.inject.Inject
import com.intellij.ide.highlighter.ArchiveFileType
import com.virtlink.editorservices.intellij.files.IFileTypeExt
import org.metaborg.intellij.idea.graphics.IIconManager
import javax.swing.Icon

/**
 * The file type for Spoofax artifacts.
 */
class SpoofaxLanguageArtifactFileType @Inject constructor(
        private val iconManager: IIconManager)
    : ArchiveFileType(), IFileTypeExt {

    companion object {
        private val ID = "SPOOFAX_ARTIFACT"
        private val DESCRIPTION = "Spoofax artifact"
        private val EXTENSION = "spoofax-language"
    }

    override fun getName(): String {
        return ID
    }

    override fun getDescription(): String {
        return DESCRIPTION
    }

    override fun getDefaultExtension(): String {
        return extensions.first()
    }

    override fun getIcon(): Icon? {
        return this.iconManager.languageArtifactIcon
    }

    override val extensions: Set<String>
        get() = setOf(EXTENSION)

}
