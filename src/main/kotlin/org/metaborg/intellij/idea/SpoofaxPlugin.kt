package org.metaborg.intellij.idea

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import java.io.File

/**
 * About this plugin.
 */
object SpoofaxPlugin {

    /**
     * Gets the ID of the plugin.
     *
     * @return The ID of the plugin.
     */
    // This must match the ID in plugin.xml
    val id = PluginId.getId("org.metaborg.intellij")

    /**
     * Gets the plugin object.
     *
     * @return The plugin object.
     */
    val plugin = PluginManager.getPlugin(this.id)!!

    /**
     * Gets the path to the plugin's home folder.
     *
     * @return The home folder of the plugin.
     */
    val path = this.plugin.path!!

    /**
     * Gets the path to the plugin's library folder.
     *
     * @return The lib/ folder of the plugin.
     */
    val libPath = File(this.path, "lib")

    /**
     * Gets the version of the plugin.
     *
     * @return The version of the plugin.
     */
    val version = this.plugin.version!!
}