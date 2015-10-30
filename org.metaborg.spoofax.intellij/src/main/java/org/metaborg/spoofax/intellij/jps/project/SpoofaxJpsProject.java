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

package org.metaborg.spoofax.intellij.jps.project;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;

/**
 * A Spoofax project used in JPS.
 */
public final class SpoofaxJpsProject implements IProject {

    @NotNull
    private final FileObject location;
    @NotNull
    private final JpsModule module;

    /**
     * Initializes a new instance of the {@link SpoofaxJpsProject} class.
     *
     * @param location The location of the project root.
     */
    public SpoofaxJpsProject(@NotNull final JpsModule module, @NotNull final FileObject location) {
        this.module = module;
        // TODO: Get location from JpsModule?
        // NOTE: A module can have multiple content roots, or none at all.
        // Instead of trying to do everything relative to the project root,
        // we should use explicit directories for generated files and such.
        // Those would be configurable in the IDE, and can be found in the
        // content roots list as directories for generated files. Then we'd
        // simply pick the first, or error when there are none.
        //String projectRoot = this.module.getContentRootsList().getUrls().get(0);
        //return new SpoofaxJpsProject(this.resourceService.resolve(projectRoot));

        this.location = location;
    }

    @Override
    @NotNull
    public FileObject location() {
        return this.location;
    }

    @NotNull
    public JpsModule module() {
        return this.module;
    }

    @Override
    public String toString() {
        return this.module.getName();
    }
}
