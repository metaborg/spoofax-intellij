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
import org.jetbrains.jps.builders.java.JavaModuleBuildTargetType;
import org.jetbrains.jps.incremental.ModuleBuildTarget;
import org.metaborg.intellij.jps.projects.MetaborgJpsProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Build target for Spoofax projects post-Java.
 */
public final class SpoofaxPostTarget extends SpoofaxTarget {

    private final SpoofaxPreTargetType preTargetType;

    /**
     * Initializes a new instance of the {@link SpoofaxPostTarget} class.
     */
    // TODO: Inject!
    public SpoofaxPostTarget(
            final MetaborgJpsProject project,
            final SpoofaxPostTargetType targetType,
            final SpoofaxPreTargetType preTargetType) {
        super(project, targetType);
        this.preTargetType = preTargetType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isCompiledBeforeModuleLevelBuilders() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<BuildTarget<?>> computeDependencies(
            final BuildTargetRegistry buildTargetRegistry,
            final TargetOutputIndex targetOutputIndex) {
        final List<BuildTarget<?>> dependencies = new ArrayList<>();
        dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));
        dependencies.add(this.preTargetType.createTarget(super.myModule));
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getPresentableName() {
        return "Spoofax POST target 2 '" + getId() + "'";
    }

}
