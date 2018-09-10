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

import org.jetbrains.jps.builders.BuildTarget;
import org.jetbrains.jps.builders.BuildTargetRegistry;
import org.jetbrains.jps.builders.TargetOutputIndex;
import org.metaborg.intellij.jps.projects.MetaborgJpsProject;

import java.util.Collection;
import java.util.Collections;

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
