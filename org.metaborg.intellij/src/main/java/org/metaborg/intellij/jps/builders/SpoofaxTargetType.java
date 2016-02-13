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

import org.jetbrains.annotations.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.intellij.jps.*;
import org.metaborg.intellij.jps.configuration.*;
import org.metaborg.intellij.jps.projects.*;

import java.util.*;

/**
 * The spoofax build target type, which is a module-based build target.
 *
 * @param <T> The associated type of build target.
 */
public abstract class SpoofaxTargetType<T extends SpoofaxTarget> extends ModuleBasedBuildTargetType<T> {

    private final JpsMetaborgModuleType moduleType;
    private final IJpsProjectService projectService;

    protected SpoofaxTargetType(final String typeId, final IJpsProjectService projectService,
                                final JpsMetaborgModuleType moduleType) {
        super(typeId);
        this.projectService = projectService;
        this.moduleType = moduleType;
    }

    /**
     * Creates a build target for the specified project.
     *
     * @param project The project.
     * @return The created build target.
     */
    public abstract T createTarget(MetaborgJpsProject project);

    /**
     * Creates a build target for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created build target.
     */
    public final T createTarget(final JpsModule module) {
        @Nullable MetaborgJpsProject project = this.projectService.get(module);
        if (project == null)
            project = this.projectService.create(module);
        return createTarget(project);
    }

    /**
     * Computes all build targets for the specified JPS model.
     *
     * @param model The JPS model.
     * @return A list of build targets.
     */
    @Override
    public final List<T> computeAllTargets(final JpsModel model) {
        // Default implementation.
        final List<T> targets = new ArrayList<>();
        final Iterable<JpsTypedModule<JpsMetaborgModuleConfig>> modules = model.getProject().getModules(this.moduleType);
        for (final JpsTypedModule<JpsMetaborgModuleConfig> module : modules) {
            targets.add(createTarget(module));
        }
        return targets;
    }

    /**
     * Creates a build target loader, that creates a build target for a JPS module with a specified ID.
     *
     * @param model The JPS model.
     * @return The build target loader.
     */
    @Override
    public final BuildTargetLoader<T> createLoader(final JpsModel model) {
        // Default implementation.
        return new BuildTargetLoader<T>() {
            @Nullable
            @Override
            public T createTarget(final String targetId) {
                for (final T target : computeAllTargets(model)) {
                    if (target.getId().equals(targetId))
                        return target;
                }
                return null;
            }
        };
    }

}
