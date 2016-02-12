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

package org.metaborg.intellij.jps.serialization;


import org.jetbrains.jps.model.*;
import org.jetbrains.jps.service.*;

// TODO: Make interface.
/**
 * Gets or sets the configuration for JPS model objects.
 */
public abstract class SpoofaxExtensionService {

    /**
     * Gets the global configuration.
     *
     * @param global The global.
     * @return The configuration.
     */
    public abstract SpoofaxGlobalConfig getConfiguration(JpsGlobal global);

    /**
     * Sets the global configuration.
     *
     * @param global The global.
     * @param config The configuration.
     */
    public abstract void setConfiguration(JpsGlobal global, SpoofaxGlobalConfig config);

    /**
     * Gets the project configuration.
     *
     * @param project The project.
     * @return The configuration.
     */
    public abstract SpoofaxProjectConfig getConfiguration(JpsProject project);

    /**
     * Sets the project configuration.
     *
     * @param project The project.
     * @param config  The configuration.
     */
    public abstract void setConfiguration(JpsProject project, SpoofaxProjectConfig config);
}
