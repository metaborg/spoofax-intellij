package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.ex.JpsElementBase;

/**
 * JPS configuration element.
 */
public abstract class SpoofaxConfig<TState, Self extends SpoofaxConfig<TState, Self>> extends JpsElementBase<Self> {

    @NotNull
    private TState state;

    /**
     * Initializes a new instance of the {@link SpoofaxConfig} class.
     *
     * @param defaultState The default state.
     */
    protected SpoofaxConfig(TState defaultState) {
        this.state = defaultState;
    }

    /**
     * Sets the state.
     *
     * @param value The state.
     */
    public final void loadState(@NotNull final TState value) {
        this.state = value;
    }

    /**
     * Returns a copy of this configuration.
     *
     * @return The copy.
     */
    @NotNull
    @Override
    public abstract Self createCopy();

    /**
     * Copies the state of the specified configuration to this one.
     *
     * @param modified The other configuration.
     */
    @Override
    public void applyChanges(@NotNull final Self modified) {
        this.state = modified.getState();
    }

    /**
     * Gets the state.
     *
     * @return The state.
     */
    @NotNull
    public final TState getState() {
        return this.state;
    }
}
