/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.jps.configuration;

import org.jetbrains.jps.model.ex.*;

/**
 * JPS configuration element.
 */
public abstract class AbstractMetaborgConfig<TState, Self extends AbstractMetaborgConfig<TState, Self>> extends JpsElementBase<Self> {

    private TState state;

    /**
     * Initializes a new instance of the {@link AbstractMetaborgConfig} class.
     *
     * @param defaultState The default state.
     */
    protected AbstractMetaborgConfig(final TState defaultState) {
        super();
        loadState(defaultState);
    }

    /**
     * Sets the state.
     *
     * @param state The state.
     */
    public void loadState(final TState state) {
        this.state = state;
    }

    /**
     * Returns a copy of this configuration.
     *
     * @return The copy.
     */
    @Override
    public abstract Self createCopy();

    /**
     * Copies the state of the specified configuration to this one.
     *
     * @param modified The other configuration.
     */
    @Override
    public final void applyChanges(final Self modified) {
        loadState(modified.getState());
    }

    /**
     * Gets the state.
     *
     * @return The state.
     */
    public final TState getState() {
        return this.state;
    }
}
