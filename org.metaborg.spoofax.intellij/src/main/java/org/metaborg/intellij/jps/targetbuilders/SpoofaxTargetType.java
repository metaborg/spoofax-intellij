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

package org.metaborg.intellij.jps.targetbuilders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.intellij.jps.project.IJpsProjectService;
import org.metaborg.intellij.jps.project.MetaborgJpsProject;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;

import java.util.ArrayList;
import java.util.List;

/**
 * The spoofax build target type, which is a module-based build target.
 *
 * @param <T> The associated type of build target.
 */
public abstract class SpoofaxTargetType<T extends SpoofaxTarget> extends ModuleBasedBuildTargetType<T> {

    @NotNull
    private final IJpsProjectService projectService;

    protected SpoofaxTargetType(final String typeId, final IJpsProjectService projectService) {
        super(typeId);
        this.projectService = projectService;
    }

    /**
     * Creates a build target for the specified project.
     *
     * @param project The project.
     * @return The created build target.
     */
    @NotNull
    public abstract T createTarget(MetaborgJpsProject project);

    /**
     * Creates a build target for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created build target.
     */
    @NotNull
    public final T createTarget(final JpsModule module) {
        MetaborgJpsProject project = this.projectService.get(module);
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
    @NotNull
    @Override
    public final List<T> computeAllTargets(final JpsModel model) {
        // Default implementation.
        final List<T> targets = new ArrayList<>();
        for (final JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
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
    @NotNull
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
