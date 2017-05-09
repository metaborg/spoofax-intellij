package org.metaborg.intellij.idea.modules

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.vfs.VirtualFile
import org.metaborg.intellij.idea.configuration.ConfigurationFileEventListener
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
}