package org.metaborg.intellij.idea.modules

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import com.intellij.util.containers.ContainerUtil
import org.metaborg.intellij.idea.projects.MetaborgModuleType

class WrongModuleTypeNotificationProvider(private val project: Project)
    : EditorNotifications.Provider<EditorNotificationPanel>(), DumbAware {

    companion object {
        private val KEY = Key.create<EditorNotificationPanel>("Wrong module type")
        private const val DO_NOT_ASK_TO_CHANGE_MODULE_TYPE_KEY = "do.not.ask.to.change.module.type"

        private fun createPanel(project: Project, module: Module): EditorNotificationPanel {
            val panel = EditorNotificationPanel()
            panel.setText("'${module.name}' is not a Spoofax module; Spoofax-specific features might not work")
            panel.createActionLabel("Change module type to Spoofax and reload project") {
                val message = Messages.showOkCancelDialog(project,
                        "Updating module type requires project reload. Proceed?",
                        "Update Module Type", "Reload project", "Cancel", null)
                if (message == Messages.YES) {
                    // Set the correct module type and reload the project.
                    module.setModuleType(MetaborgModuleType.ID)
                    project.save()
                    EditorNotifications.getInstance(project).updateAllNotifications()
                    ProjectManager.getInstance().reloadProject(project)
                }
            }
            // Ask the user to ignore this for this module.
            panel.createActionLabel("Don't show again for this module") {
                val ignoredModules = getIgnoredModules(project)
                ignoredModules.add(module.name)
                // Store the names of the ignored modules in the project.
                PropertiesComponent.getInstance(project).setValue(DO_NOT_ASK_TO_CHANGE_MODULE_TYPE_KEY, StringUtil.join(ignoredModules, ","))
                EditorNotifications.getInstance(project).updateAllNotifications()
            }
            return panel
        }

        private fun getIgnoredModules(project: Project): MutableSet<String> {
            // Gets the names of the ignored modules in the project.
            val value = PropertiesComponent.getInstance(project).getValue(DO_NOT_ASK_TO_CHANGE_MODULE_TYPE_KEY, "")
            return ContainerUtil.newLinkedHashSet(StringUtil.split(value, ","))
        }
    }

    override fun getKey(): Key<EditorNotificationPanel> {
        return KEY
    }

    override fun createNotificationPanel(file: VirtualFile, fileEditor: FileEditor): EditorNotificationPanel? {
        // First we have to find a Metaborg configuration file in the project.
        if (!SpoofaxModuleUtils.isMetaborgConfigurationFile(file) && !SpoofaxModuleUtils.isMetaborgLanguageFile(file))
            return null

        // Now we know it's supposed to be a Metaborg module.
        // Display a dialog if it hasn't been marked as such.
        // The dialog allows the user to change the module type, do nothing, or ignore the module forever.
        val module = ModuleUtilCore.findModuleForFile(file, project)
        if (module == null || SpoofaxModuleUtils.isSpoofaxModule(module) || getIgnoredModules(project).contains(module.name))
            return null
        return createPanel(project, module)
    }


}