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

package org.metaborg.spoofax.intellij;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildRootDescriptor;
import org.jetbrains.jps.builders.BuildTarget;

import java.io.File;

/**
 * Describes a source root for the Spoofax build target.
 */
public final class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    @NotNull
    private final File root;
    @NotNull
    private final BuildTarget<?> target;

    /**
     * Initializes a new instance of the {@link SpoofaxSourceRootDescriptor} class.
     *
     * @param root   The directory of the source root.
     * @param target The build target to which the source root belongs.
     */
    public SpoofaxSourceRootDescriptor(@NotNull final File root, @NotNull final BuildTarget<?> target) {
        this.root = root;
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final String getRootId() {
        return this.root.getAbsolutePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final File getRootFile() {
        return this.root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final BuildTarget<?> getTarget() {
        return this.target;
    }

}