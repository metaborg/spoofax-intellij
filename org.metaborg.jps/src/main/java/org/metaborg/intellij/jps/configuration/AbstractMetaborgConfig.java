/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.jps.configuration;

import org.jetbrains.jps.model.ex.JpsElementBase;

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
