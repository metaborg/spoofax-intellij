package org.metaborg.intellij.idea.files


import com.google.common.collect.Lists
import com.google.inject.Inject
import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import com.virtlink.logging.logger
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin
import org.metaborg.intellij.idea.filetypes.IMetaborgFileType
import org.metaborg.intellij.idea.getInstance

/**
 * File type factory for the Spoofax Language Artifact (.spoofax-language) file type.
 */
class SpoofaxLanguageArtifactFileTypeFactory private constructor(): FileTypeFactory() {

    @Suppress("PrivatePropertyName")
    private val LOG by logger()
    private var artifactFileType: SpoofaxLanguageArtifactFileType = SpoofaxIdeaPlugin.injector().getInstance()

    override fun createFileTypes(consumer: FileTypeConsumer) {
        LOG.debug("Registering file type: {}", this.artifactFileType)
        consumer.consume(this.artifactFileType)
        LOG.info("Registered file type: {}", this.artifactFileType)
    }

}
