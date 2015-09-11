package org.metaborg.spoofax.intellij.serialization;

import com.google.common.base.Preconditions;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxProjectSerializer.NAME,
        storages = {
                @Storage(file = StoragePathMacros.PROJECT_FILE),
                @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + SpoofaxProjectSerializer.CONFIG_FILE, scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public final class SpoofaxProjectService implements PersistentStateComponent<SpoofaxProjectState> {

    private SpoofaxProjectState state;

    public SpoofaxProjectService() {
        state = new SpoofaxProjectState();
    }

    public static SpoofaxProjectService getInstance(Project project) {
        Preconditions.checkNotNull(project);
        SpoofaxProjectService service = ServiceManager.getService(project, SpoofaxProjectService.class);
        assert service != null;
        return service;
    }

    @Nullable @Override public SpoofaxProjectState getState() { return this.state; }

    @Override
    public void loadState(SpoofaxProjectState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpoofaxProjectService))
            return false;
        return this.state.equals(((SpoofaxProjectService)obj).state);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }
}
