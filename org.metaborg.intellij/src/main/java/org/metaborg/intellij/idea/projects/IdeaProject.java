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

package org.metaborg.intellij.idea.projects;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.IProjectConfig;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.Project;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.module.Module;

/**
 * An IntelliJ IDEA project.
 */
public class IdeaProject extends Project {

    private final Module module;

    @Inject
    /* package private */ IdeaProject(
            @Assisted final Module module,
            @Assisted final FileObject location,
            @Assisted @Nullable final IProjectConfig config) {
        super(location, config);
        this.module = module;
    }

    /**
     * Gets the IDE-specific module.
     *
     * @return The module.
     */
    public final Module getModule() {
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