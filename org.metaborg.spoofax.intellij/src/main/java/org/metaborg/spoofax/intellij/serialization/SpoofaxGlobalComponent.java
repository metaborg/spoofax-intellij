package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

@State(
        name = "SpoofaxGlobalComponent",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.APP_CONFIG + "/Spoofax.xml")
        }
)
public final class SpoofaxGlobalComponent implements PersistentStateComponent<SpoofaxGlobalState> {

    private SpoofaxGlobalState state = new SpoofaxGlobalState();
    @Nullable @Override public SpoofaxGlobalState getState() { return this.state; }
    public void setState(SpoofaxGlobalState value) { this.state = value; }


    @Override
    public void loadState(SpoofaxGlobalState state) {
        this.state = state;
    }
}
