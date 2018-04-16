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

package org.metaborg.intellij.idea.facets;

import com.google.inject.Inject;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.facets.MetaborgFacetConstants;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.configuration.IdeaMetaborgModuleFacetConfig;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.idea.projects.MetaborgModuleType;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import javax.swing.*;

/**
 * The Metaborg facet type.
 *
 * This type of facet on a module indicates that the module uses files written in a Metaborg language.
 */
public final class MetaborgFacetType extends FacetType<MetaborgFacet, IdeaMetaborgModuleFacetConfig> {

    public static final String ID = MetaborgFacetConstants.ID;
    public static final String NAME = MetaborgFacetConstants.NAME;

    private IIconManager iconManager;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgFacetType() {
        super(MetaborgFacet.ID, MetaborgFacetType.ID, MetaborgFacetType.NAME, null);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final IIconManager iconManager) {
        this.iconManager = iconManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaMetaborgModuleFacetConfig createDefaultConfiguration() {
        return new IdeaMetaborgModuleFacetConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetaborgFacet createFacet(final Module module,
                                     final String name,
                                     final IdeaMetaborgModuleFacetConfig configuration,
                                     @Nullable final Facet underlyingFacet) {
        return new MetaborgFacet(this, module, name, configuration, underlyingFacet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSuitableModuleType(final ModuleType moduleType) {
        // You can't apply this facet to a Metaborg language specification.
        return !(moduleType instanceof MetaborgModuleType);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return this.iconManager.getFacetIcon();
    }
}
