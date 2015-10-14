package org.metaborg.spoofax.intellij.serialization;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.service.JpsServiceManager;

/**
 * Gets or sets the configuration for JPS model objects.
 */
public abstract class SpoofaxExtensionService {

    // TODO: Inject
    public final static SpoofaxExtensionService getInstance() {
        return JpsServiceManager.getInstance().getService(SpoofaxExtensionService.class);
    }

    /**
     * Gets the global configuration.
     *
     * @param global The global.
     * @return The configuration.
     */
    @NotNull
    public abstract SpoofaxGlobalConfig getConfiguration(@NotNull JpsGlobal global);

    /**
     * Sets the global configuration.
     *
     * @param global The global.
     * @param config The configuration.
     */
    public abstract void setConfiguration(@NotNull JpsGlobal global, @NotNull SpoofaxGlobalConfig config);

    /**
     * Gets the project configuration.
     *
     * @param project The project.
     * @return The configuration.
     */
    @NotNull
    public abstract SpoofaxProjectConfig getConfiguration(@NotNull JpsProject project);

    /**
     * Sets the project configuration.
     *
     * @param project The project.
     * @param config  The configuration.
     */
    public abstract void setConfiguration(@NotNull JpsProject project, @NotNull SpoofaxProjectConfig config);
}
