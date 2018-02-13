package org.metaborg.intellij.idea.modules

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.vfs.VirtualFile
import org.metaborg.intellij.idea.configuration.ConfigurationFileEventListener
import org.metaborg.intellij.idea.files.SpoofaxFileType
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType
import org.metaborg.intellij.idea.projects.MetaborgModuleType

/**
 * Utility functions for working with Spoofax modules.
 */
object SpoofaxModuleUtils {

    /**
     * The filename of the configuration file in a Spoofax project.
     */
    const val METABORG_CONFIG_FILENAME = "metaborg.yaml"

    /**
     * Tests whether the specified module is a Spoofax module.
     *
     * @param module The module to test; or null.
     * @return True when it is a Spoofax module; otherwise, false.
     */
    fun isSpoofaxModule(module: Module?): Boolean {
        return module != null
            && ModuleType.`is`(module, MetaborgModuleType.getModuleType())
    }

    /**
     * Tests whether the specified file is a Metaborg configuration file.
     *
     * @param file The file to test.
     * @return True when the file is a Metaborg configuration file; otherwise, false.
     */
    fun isMetaborgConfigurationFile(file: VirtualFile): Boolean {
        return file.name == METABORG_CONFIG_FILENAME
    }

    /**
     * Tests whether the specified file is a source file of a loaded Metaborg language.
     *
     * @param file The file to test.
     * @return True when the file is a source file of a loaded Metaborg language; otherwise, false.
     */
    @Deprecated("")
    fun isMetaborgLanguageFile(file: VirtualFile): Boolean {
        // Determine whether the file has one of the loaded languages.
        return file.fileType is MetaborgLanguageFileType
    }

    /**
     * Tests whether the specified file is a source file of a loaded Metaborg language.
     *
     * @param file The file to test.
     * @return True when the file is a source file of a loaded Metaborg language; otherwise, false.
     */
    fun isSpoofaxLanguageFile(file: VirtualFile): Boolean {
        // Determine whether the file has one of the loaded languages.
        return file.fileType is SpoofaxFileType
    }
}