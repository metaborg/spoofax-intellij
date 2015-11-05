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

package org.metaborg.spoofax.intellij.project;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;

/**
 * An IntelliJ project.
 *
 * @author Daniël Pelsmaeker
 * @since 1.0
 */
public final class IntelliJProject implements IProject {

    @NotNull
    private final Module module;
    @NotNull
    private final FileObject location;

    @Inject
    /* package private */ IntelliJProject(
            @Assisted @NotNull final Module module,
            @Assisted @NotNull final FileObject location) {
        this.module = module;
        this.location = location;
    }

    /**
     * Gets the IDE-specific module.
     *
     * @return The module.
     */
    @NotNull
    public final Module getModule() {
        return this.module;
    }

    /**
     * Gets the location of the project.
     *
     * @return The project root location.
     */
    @Override
    @NotNull
    public final FileObject location() {
        return this.location;
    }
}