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

package org.metaborg.intellij.jps.project;

import org.apache.commons.vfs2.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.core.project.*;

import java.util.*;

/**
 * A Spoofax project used in JPS.
 */
public final class MetaborgJpsProject implements IProject, ILanguageSpec {

    private final FileObject location;
    private final JpsModule module;

    /**
     * Initializes a new instance of the {@link MetaborgJpsProject} class.
     *
     * @param location The location of the project root.
     */
    public MetaborgJpsProject(final JpsModule module, final FileObject location) {
        this.module = module;
        // TODO: Get location from JpsModule?
        // NOTE: A module can have multiple content roots, or none at all.
        // Instead of trying to do everything relative to the project root,
        // we should use explicit directories for generated files and such.
        // Those would be configurable in the IDE, and can be found in the
        // content roots list as directories for generated files. Then we'd
        // simply pick the first, or error when there are none.
        final JpsUrlList contentRootsList = this.module.getContentRootsList();
        final List<String> urls = contentRootsList.getUrls();
        //String projectRoot = this.module.getContentRootsList().getUrls().get(0);
        //return new MetaborgJpsProject(this.resourceService.resolve(projectRoot));

        this.location = location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject location() {
        return this.location;
    }

    /**
     * Gets the JPS module of this project.
     */
    public JpsModule module() {
        return this.module;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.module.getName();
    }
}
