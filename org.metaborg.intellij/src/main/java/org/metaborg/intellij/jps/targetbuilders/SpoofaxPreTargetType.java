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

package org.metaborg.intellij.jps.targetbuilders;

import com.google.inject.*;
import org.metaborg.intellij.jps.project.*;

public final class SpoofaxPreTargetType extends SpoofaxTargetType<SpoofaxPreTarget> {

    @Inject
    public SpoofaxPreTargetType(final IJpsProjectService projectService) {
        super("spoofax-pre-production", projectService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SpoofaxPreTarget createTarget(final MetaborgJpsProject project) {
        return new SpoofaxPreTarget(project, this);
    }

}
