package com.virtlink.editorservices.spoofax

import com.google.inject.Inject
import org.metaborg.core.MetaborgRuntimeException
import org.metaborg.core.language.ILanguageImpl
import org.metaborg.core.language.ILanguageService
import org.metaborg.core.language.LanguageIdentifier
import org.metaborg.core.language.LanguageIdentifierService
import org.metaborg.core.resource.IResourceService
import java.net.URI

/**
 * Helps with languages.
 */
class LanguageHelper @Inject constructor(
        private val resourceService: IResourceService,
        private val languageService: ILanguageService,
        private val languageIdentifierService: LanguageIdentifierService){

    /**
     * Determines the language of the specified document.
     *
     * @param document The URI of the document whose language to determine.
     * @param languageID The language ID given by the service, if any; otherwise, null.
     * @return The language implementation; or null when it could not be determined.
     */
    fun determineLanguageOf(document: URI, languageID: String?): ILanguageImpl? {
        val languageImpl = determineLanguageOf(languageID)
        if (languageImpl != null) return languageImpl

        // TODO: We need a way to convert from the intellij:///project/folder/file.ext
        // to a local file system path.  Of course, this is not supported by AESI
        // out-of-the-box, as AESI should work even across network boundaries.

        val localFile = try {
            // NOTE: Spoofax does not (yet) support a virtual file system,
            // which means the Spoofax AESI implementation must always reside
            // on the same local file system as the project itself.
            resourceService.resolve(document)
        } catch (_: MetaborgRuntimeException) {
            // URI is invalid.
            null
        } ?: return null

        return this.languageIdentifierService.identify(localFile)
    }

    /**
     * Determines the language designated by the specified language ID.
     *
     * The language ID should be of the form `group:name:version`.
     * For example: `org.metaborg:org.metaborg.meta.lang.stratego:2.5.0-SNAPSHOT`
     *
     * @param languageID The language identifier given by the service, if any; otherwise, null.
     * @return The language implementation; or null when it could not be determined.
     */
    fun determineLanguageOf(languageID: String?): ILanguageImpl? {
        if (languageID == null)
            return null
        val languageIdObj = LanguageIdentifier.parseFull(languageID)
        return this.languageService.getImpl(languageIdObj)
    }
}