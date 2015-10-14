package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = SpoofaxGlobalSerializer.NAME,
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/" + SpoofaxGlobalSerializer.CONFIG_FILE)
        }
)
public final class SpoofaxGlobalService implements PersistentStateComponent<SpoofaxGlobalState> {

    @Nullable
    private SpoofaxGlobalState state;

    public SpoofaxGlobalService() {
        state = new SpoofaxGlobalState();
    }

    @NotNull
    public final static SpoofaxGlobalService getInstance() {
        final SpoofaxGlobalService service = ServiceManager.getService(SpoofaxGlobalService.class);
        assert service != null;
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public SpoofaxGlobalState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadState(@NotNull final SpoofaxGlobalState state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SpoofaxGlobalService))
            return false;
        return this.state.equals(((SpoofaxGlobalService) obj).state);
    }
}
