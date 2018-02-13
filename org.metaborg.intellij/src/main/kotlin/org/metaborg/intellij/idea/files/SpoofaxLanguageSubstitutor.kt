package org.metaborg.intellij.idea.files

import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutor
import com.intellij.psi.LanguageSubstitutors

// TODO: Register the subtitutor for every Spoofax language.

/**
 * Determines the actual language or dialect to use for a Spoofax file.
 *
 * By default, the file type of a file is determined by its [LanguageFileType].
 * However, the language can be overridden by this substitutor.  This is used,
 * for example, to substitute a language with its dialect when a `.meta` file
 * is present.
 */
class SpoofaxLanguageSubstitutor: LanguageSubstitutor() {

    companion object {
        /**
         * Registers a language substitutor.
         *
         * @param key The language for which to register the substitutor.
         * @param value The substitutor to register.
         */
        fun register(key: Language, value: LanguageSubstitutor) {
            LanguageSubstitutors.INSTANCE.addExplicitExtension(key, value)
        }

        /**
         * Unregisters a language substitutor.
         *
         * @param key The language for which to unregister the substitutor.
         * @param value The substitutor to unregister.
         */
        fun unregister(key: Language, value: LanguageSubstitutor) {
            LanguageSubstitutors.INSTANCE.removeExplicitExtension(key, value)
        }
    }

    override fun getLanguage(file: VirtualFile, project: Project): Language? {
        // TODO: Use Spoofax to determine the actual language.
        return null
    }
}