package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxProjectSerializer.NAME,
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + SpoofaxProjectSerializer.CONFIG_FILE, scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public final class SpoofaxProjectComponent implements PersistentStateComponent<SpoofaxProjectState> {

    private SpoofaxProjectState state;

    public SpoofaxProjectComponent() {
        state = new SpoofaxProjectState();
    }

    @Nullable @Override public SpoofaxProjectState getState() { return this.state; }

    @Override
    public void loadState(SpoofaxProjectState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpoofaxProjectComponent))
            return false;
        return this.state.equals(((SpoofaxProjectComponent)obj).state);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }
}
