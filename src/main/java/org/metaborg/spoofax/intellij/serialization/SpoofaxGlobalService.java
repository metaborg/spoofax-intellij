package org.metaborg.spoofax.intellij.serialization;

import com.google.common.base.Preconditions;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxGlobalSerializer.NAME,
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/" + SpoofaxGlobalSerializer.CONFIG_FILE)
        }
)
public final class SpoofaxGlobalService implements PersistentStateComponent<SpoofaxGlobalState> {

    private SpoofaxGlobalState state;

    public SpoofaxGlobalService() {
        state = new SpoofaxGlobalState();
    }

    public static SpoofaxGlobalService getInstance() {
        SpoofaxGlobalService service = ServiceManager.getService(SpoofaxGlobalService.class);
        assert service != null;
        return service;
    }

    @Nullable @Override public SpoofaxGlobalState getState() { return this.state; }

    @Override
    public void loadState(SpoofaxGlobalState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpoofaxGlobalService))
            return false;
        return this.state.equals(((SpoofaxGlobalService)obj).state);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }
}
