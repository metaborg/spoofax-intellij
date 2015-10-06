package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

import javax.annotation.Nullable;

/**
 * Created by daniel on 9/8/15.
 */
public final class SpoofaxProjectConfig extends JpsElementBase<SpoofaxProjectConfig> {
    @NotNull
    public static final JpsElementChildRole<SpoofaxProjectConfig> ROLE = JpsElementChildRoleBase.create(
            "Spoofax Project");

    @Nullable
    private SpoofaxProjectState state = new SpoofaxProjectState();

    @Nullable
    public final SpoofaxProjectState getState() {
        return this.state;
    }

    public final void loadState(@Nullable final SpoofaxProjectState value) {
        this.state = value;
    }

    @NotNull
    @Override
    public final SpoofaxProjectConfig createCopy() {
        return new SpoofaxProjectConfig();
    }

    @Override
    public final void applyChanges(@NotNull final SpoofaxProjectConfig modified) {
        this.state = modified.state;
    }
}
