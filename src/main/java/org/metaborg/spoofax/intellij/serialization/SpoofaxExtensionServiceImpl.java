package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;

/**
 * Implementation of the {@link SpoofaxExtensionService}.
 */
public final class SpoofaxExtensionServiceImpl extends SpoofaxExtensionService {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final SpoofaxGlobalConfig getConfiguration(@NotNull final JpsGlobal global) {
        return global.getContainer().getChild(SpoofaxGlobalConfig.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setConfiguration(@NotNull final JpsGlobal global, @NotNull final SpoofaxGlobalConfig config) {
        global.getContainer().setChild(SpoofaxGlobalConfig.ROLE, config);
    }


    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final SpoofaxProjectConfig getConfiguration(@NotNull final JpsProject project) {
        return project.getContainer().getChild(SpoofaxProjectConfig.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setConfiguration(@NotNull final JpsProject project, @NotNull final SpoofaxProjectConfig config) {
        project.getContainer().setChild(SpoofaxProjectConfig.ROLE, config);
    }
}
