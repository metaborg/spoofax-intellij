package org.metaborg.spoofax.intellij.project.settings;

import org.metaborg.core.project.IProject;
import org.metaborg.core.project.settings.IProjectSettings;

/**
 * Loads and stores the settings of a project.
 */
public interface IProjectSettingsService2 {

    /**
     * Creates new settings.
     * @return The settings.
     */
    IProjectSettings create();

    /**
     * Gets the settings for the specified project.
     * @param project The project.
     * @return The settings of the project.
     */
    IProjectSettings get(IProject project);

    /**
     * Sets the settings for the specified project.
     * @param project The project.
     * @param settings The settings of the project.
     */
    void set(IProject project, IProjectSettings settings);

}
