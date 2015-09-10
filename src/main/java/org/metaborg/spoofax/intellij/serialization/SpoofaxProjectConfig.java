package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Created by daniel on 9/8/15.
 */
public class SpoofaxProjectConfig extends JpsElementBase<SpoofaxProjectConfig> {
    public static final JpsElementChildRole<SpoofaxProjectConfig> ROLE = JpsElementChildRoleBase.create("Spoofax Project");

    private SpoofaxProjectState state = new SpoofaxProjectState();
    public SpoofaxProjectState getState() { return this.state; }
    public void loadState(SpoofaxProjectState value) { this.state = value; }

    @NotNull
    @Override
    public SpoofaxProjectConfig createCopy() {
        return new SpoofaxProjectConfig();
    }

    @Override
    public void applyChanges(@NotNull SpoofaxProjectConfig modified) {
        this.state = modified.state;
    }
}
