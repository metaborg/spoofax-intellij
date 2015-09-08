package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * Created by daniel on 9/8/15.
 */
public class SpoofaxGlobalConfigImpl extends JpsElementBase<SpoofaxGlobalConfigImpl> implements SpoofaxGlobalConfig {
    public static final JpsElementChildRole<SpoofaxGlobalConfig> ROLE = JpsElementChildRoleBase.create("Spoofax");

    private String myName = "<DEFAULT>";
    @Override
    public String getMyName() {
        return this.myName;
    }
    public void setMyName(String value) {
        this.myName = value;
    }

    @NotNull
    @Override
    public SpoofaxGlobalConfigImpl createCopy() {
        return new SpoofaxGlobalConfigImpl();
    }

    @Override
    public void applyChanges(@NotNull SpoofaxGlobalConfigImpl modified) {
        this.myName = modified.myName;

    }
}
