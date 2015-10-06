package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Created by daniel on 9/8/15.
 */
public final class SpoofaxGlobalConfig extends JpsElementBase<SpoofaxGlobalConfig> {
    @NotNull
    public static final JpsElementChildRole<SpoofaxGlobalConfig> ROLE = JpsElementChildRoleBase.create("Spoofax");

    @NotNull
    private SpoofaxGlobalState state = new SpoofaxGlobalState();

    @NotNull
    public final SpoofaxGlobalState getState() {
        return this.state;
    }

    public final void loadState(@NotNull final SpoofaxGlobalState value) {
        this.state = value;
    }

    @NotNull
    @Override
    public final SpoofaxGlobalConfig createCopy() {
        return new SpoofaxGlobalConfig();
    }

    @Override
    public void applyChanges(@NotNull final SpoofaxGlobalConfig modified) {
        this.state = modified.state;
    }
}
