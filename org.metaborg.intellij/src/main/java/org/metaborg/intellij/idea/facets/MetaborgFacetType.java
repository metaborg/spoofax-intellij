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

package org.metaborg.intellij.idea.facets;

import com.google.inject.*;
import com.intellij.facet.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.swing.*;

/**
 * The Metaborg facet type.
 *
 * This type of facet on a module indicates that the module uses files written in a Metaborg language.
 */
public final class MetaborgFacetType extends FacetType<MetaborgFacet, IdeaMetaborgModuleFacetConfig> {

    public static final String ID = "Metaborg";
    public static final String NAME = "Metaborg";

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
