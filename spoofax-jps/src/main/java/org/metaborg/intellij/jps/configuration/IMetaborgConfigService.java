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


import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.module.JpsModule;

import jakarta.annotation.Nullable;

/**
 * Gets or sets the configuration for JPS model objects.
 */
public interface IMetaborgConfigService {

    /**
     * Gets the Metaborg application configuration.
     *
     * @param global The global JPS element.
     * @return The configuration; or <code>null</code> if not present.
     */
    @Nullable
    JpsMetaborgApplicationConfig getGlobalConfiguration(JpsGlobal global);

    /**
     * Gets the Metaborg project configuration.
     *
     * @param project The project JPS element.
     * @return The configuration; or <code>null</code> if not present.
     */
    @Nullable
    JpsMetaborgProjectConfig getProjectConfiguration(JpsProject project);

    /**
     * Gets the Metaborg module configuration.
     *
     * The module configuration is only present
     * for Metaborg Language Specification modules.
     *
     * @param module The module JPS element.
     * @return The configuration; or <code>null</code> if not present.
     */
    @Nullable
    JpsMetaborgModuleConfig getModuleConfiguration(JpsModule module);

    /**
     * Gets the Metaborg module facet configuration.
     *
     * The module facet configuration is only present
     * when the module has the Metaborg Facet applied to it.
     *
     * @param module The module JPS element.
     * @return The facet configuration; or <code>null</code> if not present.
     */
    @Nullable
    JpsMetaborgModuleFacetConfig getFacetConfiguration(JpsModule module);


    /**
     * Sets the Metaborg application configuration.
     *
     * @param global The global JPS element.
     * @param config The configuration; or <code>null</code> if not present.
     */
    void setGlobalConfiguration(JpsGlobal global, @Nullable JpsMetaborgApplicationConfig config);

    /**
     * Sets the Metaborg project configuration.
     *
     * @param project The project JPS element.
     * @param config  The configuration; or <code>null</code> if not present.
     */
    void setProjectConfiguration(JpsProject project, @Nullable JpsMetaborgProjectConfig config);

    /**
     * Sets the Metaborg module configuration.
     *
     * @param module The module JPS element.
     * @param config  The configuration; or <code>null</code> if not present.
     */
    void setModuleConfiguration(JpsModule module, @Nullable JpsMetaborgModuleConfig config);

    /**
     * Sets the Metaborg module facet configuration.
     *
     * @param module The module JPS element.
     * @param config  The facet configuration; or <code>null</code> if not present.
     */
    void setFacetConfiguration(JpsModule module, @Nullable JpsMetaborgModuleFacetConfig config);


    /**
     * Gets whether there is a Metaborg application configuration.
     *
     * @param global The global JPS element.
     * @return <code>true</code> when the configuration is present;
     * otherwise, <code>false</code>.
     */
    boolean hasGlobalConfiguration(JpsGlobal global);

    /**
     * Gets whether there is a Metaborg project configuration.
     *
     * @param project The project JPS element.
     * @return <code>true</code> when the configuration is present;
     * otherwise, <code>false</code>.
     */
    boolean hasProjectConfiguration(JpsProject project);

    /**
     * Gets whether there is a Metaborg module configuration.
     *
     * The module configuration is only present
     * for Metaborg Language Specification modules.
     *
     * @param module The module JPS element.
     * @return <code>true</code> when the configuration is present;
     * otherwise, <code>false</code>.
     */
    boolean hasModuleConfiguration(JpsModule module);

    /**
     * Gets whether there is a Metaborg module facet configuration.
     *
     * The module facet configuration is only present
     * when the module has the Metaborg Facet applied to it.
     *
     * @param module The module JPS element.
     * @return <code>true</code> when the configuration is present;
     * otherwise, <code>false</code>.
     */
    boolean hasFacetConfiguration(JpsModule module);

}
