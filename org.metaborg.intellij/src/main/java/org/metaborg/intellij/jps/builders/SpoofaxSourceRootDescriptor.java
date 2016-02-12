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

import org.jetbrains.jps.builders.*;

import java.io.*;

/**
 * Describes a source root for the Spoofax build target.
 */
public final class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    private final File root;
    private final BuildTarget<?> target;

    /**
     * Initializes a new instance of the {@link SpoofaxSourceRootDescriptor} class.
     *
     * @param root   The directory of the source root.
     * @param target The build target to which the source root belongs.
     */
    public SpoofaxSourceRootDescriptor(final File root, final BuildTarget<?> target) {
        super();
        this.root = root;
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getRootId() {
        return this.root.getAbsolutePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final File getRootFile() {
        return this.root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BuildTarget<?> getTarget() {
        return this.target;
    }

}