package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxProjectSerializer.NAME,
        storages = {
                @Storage(file = StoragePathMacros.PROJECT_FILE),
                @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + SpoofaxProjectSerializer.CONFIG_FILE, scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public final class SpoofaxProjectService implements PersistentStateComponent<SpoofaxProjectState> {

    @Nullable
    private SpoofaxProjectState state;

    public SpoofaxProjectService() {
        state = new SpoofaxProjectState();
    }

    @NotNull
    public final static SpoofaxProjectService getInstance(@NotNull final Project project) {
        final SpoofaxProjectService service = ServiceManager.getService(project, SpoofaxProjectService.class);
        assert service != null;
        return service;
    }

    @Nullable
    @Override
    public final SpoofaxProjectState getState() {
        return this.state;
    }

    @Override
    public final void loadState(@Nullable final SpoofaxProjectState state) {
        this.state = state;
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof SpoofaxProjectService))
            return false;
        return this.state.equals(((SpoofaxProjectService) obj).state);
    }

    @Override
    public final int hashCode() {
        return this.state.hashCode();
    }
}
