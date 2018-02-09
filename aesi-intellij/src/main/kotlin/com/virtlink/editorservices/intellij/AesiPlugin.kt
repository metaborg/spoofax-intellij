package com.virtlink.editorservices.intellij

import com.google.inject.Injector
import com.virtlink.editorservices.SessionManager

/**
 * The plugin.
 *
 * @property injector The dependency injector.
 */
open class AesiPlugin(val injector: Injector) {

    /**
     * The session manager.
     */
    val sessionManager get() = this.injector.getInstance(SessionManager::class.java)

    /**
     * Starts the plugin.
     */
    open fun start() {
        this.sessionManager.start()
    }

    /**
     * Stops the plugin.
     */
    open fun stop() {
        this.sessionManager.stop()
    }
}


