package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Project JPS configuration.
 */
public final class SpoofaxProjectConfig extends SpoofaxConfig<SpoofaxProjectState, SpoofaxProjectConfig> {
    @NotNull
    public static final JpsElementChildRole<SpoofaxProjectConfig> ROLE = JpsElementChildRoleBase.create(
            "Spoofax Project");

    /**
     * Initializes a new instance of the {@link SpoofaxGlobalConfig} class.
     */
    public SpoofaxProjectConfig() {
        super(new SpoofaxProjectState());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final SpoofaxProjectConfig createCopy() {
        SpoofaxProjectConfig config = new SpoofaxProjectConfig();
        config.applyChanges(this);
        return config;
    }

}
