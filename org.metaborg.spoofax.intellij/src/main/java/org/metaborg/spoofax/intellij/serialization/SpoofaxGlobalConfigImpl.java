package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Created by daniel on 9/8/15.
 */
public class SpoofaxGlobalConfigImpl extends JpsElementBase<SpoofaxGlobalConfigImpl> {
    public static final JpsElementChildRole<SpoofaxGlobalConfigImpl> ROLE = JpsElementChildRoleBase.create("Spoofax");

    private SpoofaxGlobalState state = new SpoofaxGlobalState();
    public SpoofaxGlobalState getState() { return this.state; }
    public void loadState(SpoofaxGlobalState value) { this.state = value; }

    @NotNull
    @Override
    public SpoofaxGlobalConfigImpl createCopy() {
        return new SpoofaxGlobalConfigImpl();
    }

    @Override
    public void applyChanges(@NotNull SpoofaxGlobalConfigImpl modified) {
        this.state = modified.state;

    }
}
