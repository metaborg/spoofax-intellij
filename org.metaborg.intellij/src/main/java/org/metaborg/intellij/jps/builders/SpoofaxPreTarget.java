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

package org.metaborg.intellij.jps.builders;

import org.jetbrains.jps.builders.*;
import org.metaborg.intellij.jps.project.*;

import java.util.*;

/**
 * Build target for Spoofax projects pre-Java.
 */
public final class SpoofaxPreTarget extends SpoofaxTarget {

    /**
     * Initializes a new instance of the {@link SpoofaxPreTarget} class.
     */
    // TODO: Inject!
    public SpoofaxPreTarget(final MetaborgJpsProject project, final SpoofaxPreTargetType targetType) {
        super(project, targetType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isCompiledBeforeModuleLevelBuilders() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<BuildTarget<?>> computeDependencies(
            final BuildTargetRegistry buildTargetRegistry,
            final TargetOutputIndex targetOutputIndex) {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getPresentableName() {
        return "Spoofax PRE target 2 '" + getId() + "'";
    }

}
