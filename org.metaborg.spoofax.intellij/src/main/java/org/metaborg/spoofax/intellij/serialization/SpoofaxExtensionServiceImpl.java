package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;

public class SpoofaxExtensionServiceImpl extends SpoofaxExtensionService {
    @NotNull
    @Override
    public SpoofaxGlobalConfigImpl getConfiguration(@NotNull JpsGlobal global) {
        return global.getContainer().getChild(SpoofaxGlobalConfigImpl.ROLE);
    }

    @Override
    public void setConfiguration(@NotNull JpsGlobal global, @NotNull SpoofaxGlobalConfigImpl config) {
        global.getContainer().setChild(SpoofaxGlobalConfigImpl.ROLE, config);
    }
}
