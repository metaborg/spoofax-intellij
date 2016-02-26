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
