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

import com.google.inject.*;
import org.metaborg.intellij.jps.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.spoofax.meta.core.project.*;

/**
 * The type of a post-Java build target.
 */
public final class SpoofaxPostTargetType extends SpoofaxTargetType<SpoofaxPostTarget> {

    private final SpoofaxPreTargetType preTargetType;

    /**
     * Initializes a new instance of the {@link SpoofaxPostTargetType} class.
     */
    @Inject
    public SpoofaxPostTargetType(
            final IJpsProjectService projectService,
            final SpoofaxPreTargetType preTargetType,
            final JpsMetaborgModuleType moduleType,
            final JpsSpoofaxMetaBuilder metaBuilder,
            final ISpoofaxLanguageSpecService languageSpecService) {
        super("spoofax-post-production", projectService, moduleType, metaBuilder, languageSpecService);
        this.preTargetType = preTargetType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SpoofaxPostTarget createTarget(final MetaborgJpsProject project) {
        return new SpoofaxPostTarget(project, this, this.preTargetType);
    }

}
