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

import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.module.*;

import javax.annotation.*;

/**
 * Default implementation of the {@link IMetaborgConfigService} interface.
 */
public final class DefaultMetaborgConfigService implements IMetaborgConfigService {

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final JpsMetaborgApplicationConfig getGlobalConfiguration(final JpsGlobal global) {
        return global.getContainer().getChild(JpsMetaborgApplicationConfig.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final JpsMetaborgProjectConfig getProjectConfiguration(final JpsProject project) {
        return project.getContainer().getChild(JpsMetaborgProjectConfig.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public JpsMetaborgModuleConfig getModuleConfiguration(final JpsModule module) {
        final JpsElement result = module.getProperties();
        return result instanceof JpsMetaborgModuleConfig ? (JpsMetaborgModuleConfig)result : null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public JpsMetaborgModuleFacetConfig getFacetConfiguration(final JpsModule module) {
        return module.getContainer().getChild(JpsMetaborgModuleFacetConfig.ROLE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void setGlobalConfiguration(final JpsGlobal global, @Nullable final JpsMetaborgApplicationConfig config) {
        global.getContainer().setChild(JpsMetaborgApplicationConfig.ROLE, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setProjectConfiguration(final JpsProject project, @Nullable final JpsMetaborgProjectConfig config) {
        project.getContainer().setChild(JpsMetaborgProjectConfig.ROLE, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setModuleConfiguration(final JpsModule module, @Nullable final JpsMetaborgModuleConfig config) {
        module.getContainer().setChild(JpsMetaborgModuleConfig.ROLE, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setFacetConfiguration(final JpsModule module, @Nullable final JpsMetaborgModuleFacetConfig config) {
        module.getContainer().setChild(JpsMetaborgModuleFacetConfig.ROLE, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasGlobalConfiguration(final JpsGlobal global) {
        return getGlobalConfiguration(global) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasProjectConfiguration(final JpsProject project) {
        return getProjectConfiguration(project) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasModuleConfiguration(final JpsModule module) {
        return getModuleConfiguration(module) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFacetConfiguration(final JpsModule module) {
        return getFacetConfiguration(module) != null;
    }
}
