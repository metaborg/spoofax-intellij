package org.metaborg.intellij.idea

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.Notifications
import com.intellij.openapi.project.ProjectManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.notification.NotificationDisplayType



/**
 * Utility functions for working with notifications.
 */
object NotificationUtils {

    @JvmField val METABORG_NOTIFICATIONS = NotificationGroup("Metaborg Languages", NotificationDisplayType.BALLOON, true)

    /**
     * Shows a notification to the user.
     *
     * @param project The project that raised the notification.
     * @param notification The notification to show.
     */
    fun notify(project: Project, notification: Notification) {
        ApplicationManager.getApplication().invokeLater {
            Notifications.Bus.notify(notification, project)
        }
    }

}