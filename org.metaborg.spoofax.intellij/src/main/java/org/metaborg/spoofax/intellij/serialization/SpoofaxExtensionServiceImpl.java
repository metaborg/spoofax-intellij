/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;

/**
 * Implementation of the {@link SpoofaxExtensionService}.
 */
public final class SpoofaxExtensionServiceImpl extends SpoofaxExtensionService {

    /**
     * {@inheritDoc}
     */
    @Override
    public final SpoofaxGlobalConfig getConfiguration(final JpsGlobal global) {
        return global.getContainer().getChild(SpoofaxGlobalConfig.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setConfiguration(final JpsGlobal global, final SpoofaxGlobalConfig config) {
        global.getContainer().setChild(SpoofaxGlobalConfig.ROLE, config);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final SpoofaxProjectConfig getConfiguration(final JpsProject project) {
        return project.getContainer().getChild(SpoofaxProjectConfig.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setConfiguration(final JpsProject project, final SpoofaxProjectConfig config) {
        project.getContainer().setChild(SpoofaxProjectConfig.ROLE, config);
    }
}
