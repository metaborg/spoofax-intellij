package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;

public class SpoofaxExtensionServiceImpl extends SpoofaxExtensionService {
    @NotNull
    @Override
    public SpoofaxGlobalConfig getConfiguration(@NotNull JpsGlobal global) {
        return global.getContainer().getChild(SpoofaxGlobalConfig.ROLE);
    }

    @Override
    public void setConfiguration(@NotNull JpsGlobal global, @NotNull SpoofaxGlobalConfig config) {
        global.getContainer().setChild(SpoofaxGlobalConfig.ROLE, config);
    }


    @NotNull
    @Override
    public SpoofaxProjectConfig getConfiguration(@NotNull JpsProject project)
    {
        return project.getContainer().getChild(SpoofaxProjectConfig.ROLE);
    }

    @Override
    public void setConfiguration(@NotNull JpsProject project, @NotNull SpoofaxProjectConfig config)
    {
        project.getContainer().setChild(SpoofaxProjectConfig.ROLE, config);
    }
}
