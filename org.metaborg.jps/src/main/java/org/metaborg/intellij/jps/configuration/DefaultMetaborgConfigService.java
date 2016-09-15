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

import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.module.JpsModule;

import javax.annotation.Nullable;

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
