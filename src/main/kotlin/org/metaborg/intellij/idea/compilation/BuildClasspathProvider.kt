package org.metaborg.intellij.idea.compilation

import com.google.inject.Inject
import com.intellij.compiler.server.BuildProcessParametersProvider
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin
import org.metaborg.intellij.idea.SpoofaxPlugin.plugin
import org.metaborg.intellij.idea.utils.SimpleConfigUtil
import org.metaborg.intellij.logging.InjectLogger
import org.metaborg.intellij.resources.LibraryService
import org.metaborg.util.log.ILogger
import java.io.File

/**
 * Provides the classpath for the 'compileServer.plugin' extension.
 *
 * By providing the classpath dynamically, we no longer have to specify
 * and update the list of build dependencies manually (which is why the
 * 'classpath' attribute on the 'compileServer.plugin' extension in
 * plugin.xml is now empty).
 */
class BuildClasspathProvider : BuildProcessParametersProvider() {

    @InjectLogger
    private lateinit var logger: ILogger
    private lateinit var configUtil: SimpleConfigUtil
    private lateinit var libraryService: LibraryService

    init {
        SpoofaxIdeaPlugin.injector().injectMembers(this)
    }

    @Inject
    @Suppress("unused")
    private fun inject(configUtil: SimpleConfigUtil, libraryService: LibraryService) {
        this.configUtil = configUtil
        this.libraryService = libraryService
    }

    override fun getClassPath(): List<String> {
        return configUtil.readResource("/compile_dependencies.txt")
                .mapNotNull { getPathOfDependency(it) }
    }

    private fun getPathOfDependency(relativePath: String): String? {
        val jarFile = File(libraryService.pluginLibPath, relativePath)

        return if (jarFile.exists()) {
            jarFile.path
        } else {
            logger.error("Cannot add $relativePath to Spoofax compiler classpath: home directory of plugin not found")
            null
        }
    }

}