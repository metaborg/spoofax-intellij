package org.metaborg.spoofax.intellij.project.settings;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.settings.IProjectSettings;

/**
 * Loads and stores the settings of a project.
 */
public interface IProjectSettingsService2 {

    /**
     * Gets the default settings.
     *
     * @return The default settings.
     */
    @NotNull
    IProjectSettings getDefault();

    /**
     * Gets the settings for the specified project.
     *
     * @param project The project.
     * @return The settings of the project.
     */
    @NotNull
    IProjectSettings get(@NotNull IProject project);

    /**
     * Sets the settings for the specified project.
     *
     * @param project  The project.
     * @param settings The settings of the project.
     */
    void set(@NotNull IProject project, @NotNull IProjectSettings settings);

}
