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

package org.metaborg.spoofax.intellij.menu;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple with a resource to transform, along with the (part of its) source code to transform.
 */
public final class TransformResource {

    @NotNull
    private final FileObject resource;
    @NotNull
    private final String text;

    /**
     * Initializes a new instance of the {@link TransformResource} class.
     *
     * @param resource The resource to transform.
     * @param text     The text to transform.
     */
    public TransformResource(final FileObject resource, final String text) {
        this.resource = resource;
        this.text = text;
    }

    /**
     * Gets the resource to transform.
     *
     * @return The resource to transform.
     */
    @NotNull
    public FileObject resource() {
        return this.resource;
    }

    /**
     * Gets the text to transform.
     *
     * @return The text to transform.
     */
    @NotNull
    public String text() {
        return this.text;
    }
}
