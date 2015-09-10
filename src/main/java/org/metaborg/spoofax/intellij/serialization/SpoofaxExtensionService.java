package org.metaborg.spoofax.intellij.serialization;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.service.JpsServiceManager;

public abstract class SpoofaxExtensionService {
    public static SpoofaxExtensionService getInstance() {
        return JpsServiceManager.getInstance().getService(SpoofaxExtensionService.class);
    }

    @NotNull
    public abstract SpoofaxGlobalConfig getConfiguration(@NotNull JpsGlobal global);

    public abstract void setConfiguration(@NotNull JpsGlobal global, @NotNull SpoofaxGlobalConfig config);

    @NotNull
    public abstract SpoofaxProjectConfig getConfiguration(@NotNull JpsProject project);

    public abstract void setConfiguration(@NotNull JpsProject project, @NotNull SpoofaxProjectConfig config);
}
