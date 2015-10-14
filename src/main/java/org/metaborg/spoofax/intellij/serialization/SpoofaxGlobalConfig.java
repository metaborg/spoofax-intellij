package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Global JPS configuration.
 */
public final class SpoofaxGlobalConfig extends SpoofaxConfig<SpoofaxGlobalState, SpoofaxGlobalConfig> {
    @NotNull
    public static final JpsElementChildRole<SpoofaxGlobalConfig> ROLE = JpsElementChildRoleBase.create("Spoofax");

    /**
     * Initializes a new instance of the {@link SpoofaxGlobalConfig} class.
     */
    public SpoofaxGlobalConfig() {
        super(new SpoofaxGlobalState());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final SpoofaxGlobalConfig createCopy() {
        SpoofaxGlobalConfig config = new SpoofaxGlobalConfig();
        config.applyChanges(this);
        return config;
    }
}
