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

package org.metaborg.intellij.idea.configuration;

import com.google.inject.*;
import com.intellij.facet.*;
import com.intellij.facet.ui.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.*;
import org.jdom.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.facets.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

/**
 * Module-level facet configuration of the plugin.
 */
@State(
        name = IMetaborgModuleFacetConfig.CONFIG_NAME,
        storages = {
                @Storage(file = StoragePathMacros.MODULE_FILE)
        }
)
public final class IdeaMetaborgModuleFacetConfig
        implements FacetConfiguration, IMetaborgModuleFacetConfig,
        PersistentStateComponent<MetaborgModuleFacetConfigState> {

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private MetaborgModuleFacetConfigState state;
    @InjectLogger
    private ILogger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.state.myName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String value) {
        this.state.myName = value;
    }

    /**
     * Initializes a new instance of the {@link IdeaMetaborgModuleFacetConfig} class.
     */
    @Inject
    public IdeaMetaborgModuleFacetConfig() {
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        loadState(new MetaborgModuleFacetConfigState());
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgModuleFacetConfigState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * This method is only called if the configuration has changed.
     */
    @Override
    public void loadState(final MetaborgModuleFacetConfigState state) {
        this.state = state;
        // Initialize fields that depend on state here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FacetEditorTab[] createEditorTabs(final FacetEditorContext editorContext, final FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[] {
                new MetaborgFacetEditorTab(editorContext, validatorsManager)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(final Element element)
            throws InvalidDataException {
        // Not implemented: deprecated.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(final Element element)
            throws WriteExternalException {
        // Not implemented: deprecated.
    }

}
