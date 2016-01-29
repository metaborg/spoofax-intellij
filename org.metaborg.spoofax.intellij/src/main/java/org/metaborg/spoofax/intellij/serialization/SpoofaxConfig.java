/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    protected SpoofaxConfig(final TState defaultState) {
        super();
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
