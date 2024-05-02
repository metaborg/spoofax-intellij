package org.metaborg.intellij.idea.utils

import mb.common.util.ResourceUtil
import org.metaborg.intellij.UnhandledException
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin
import org.metaborg.intellij.logging.InjectLogger
import org.metaborg.intellij.logging.LoggerUtils2
import org.metaborg.util.log.ILogger
import mb.util.vfs2.resource.ResourceUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Reads simple configuration files that consist of lines of content.
 *
 * This class ignored any empty lines, or any lines that starts with a '#' (comments).
 */
class SimpleConfigUtil {

    @InjectLogger
    private lateinit var logger: ILogger

    /**
     * Reads a resource of this assembly.
     *
     * @param path The path of the resource to parse, relative to the 'resources/' folder.
     * @return The read lines, trimmed, excluding comment-only lines and empty lines.
     */
    fun readResource(path: String): List<String> {
        val url = SpoofaxIdeaPlugin::class.java.getResource(path)!!

        val text: String
        try {
            text = ResourceUtils.readInputStream(url.openStream(), StandardCharsets.UTF_8)
        } catch (e: IOException) {
            throw LoggerUtils2.exception(this.logger, UnhandledException::class.java,
                    "Cannot get resource content of resource: {}", e, url)
        }

        return readString(text)
    }

    /**
     * Reads the lines from the specified string.
     *
     * @param text The text to parse.
     * @return The read lines, trimmed, excluding comment-only lines and empty lines.
     */
    fun readString(text: String): List<String> {
        return text.split("\\r?\\n".toRegex())                // Split lines
                .map { it.trim() }                            // Remove whitespace
                .filterNot { it.startsWith("#") }       // Drop comments
                .filterNot { it.isEmpty() }                   // Drop empty lines

    }

}