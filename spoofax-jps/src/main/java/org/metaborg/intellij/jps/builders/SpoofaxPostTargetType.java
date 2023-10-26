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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import org.metaborg.intellij.builders.TargetTypeConstants;
import org.metaborg.intellij.jps.JpsMetaborgModuleType;
import org.metaborg.intellij.jps.projects.IJpsProjectService;
import org.metaborg.intellij.jps.projects.MetaborgJpsProject;
import org.metaborg.spoofax.meta.core.project.*;

/**
 * The type of a post-Java build target.
 */
public final class SpoofaxPostTargetType extends SpoofaxTargetType<SpoofaxPostTarget> {

    private final SpoofaxPreTargetType preTargetType;

    /**
     * Initializes a new instance of the {@link SpoofaxPostTargetType} class.
     */
    @jakarta.inject.Inject @javax.inject.Inject
    public SpoofaxPostTargetType(
            final IJpsProjectService projectService,
            final SpoofaxPreTargetType preTargetType,
            final JpsMetaborgModuleType moduleType,
            final JpsSpoofaxMetaBuilder metaBuilder,
            final ISpoofaxLanguageSpecService languageSpecService) {
        super(TargetTypeConstants.PostTargetType, projectService, moduleType, metaBuilder, languageSpecService);
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
