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

package org.metaborg.spoofax.intellij.idea.psi;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.context.IContext;

public final class SpoofaxAnnotationInfo {

    private final FileObject resource;
    private final String text;
    private final IContext context;

    public SpoofaxAnnotationInfo(final FileObject resource, final String text, final IContext context) {
        this.resource = resource;
        this.text = text;
        this.context = context;
    }

    public FileObject resource() { return this.resource; }
    public String text() { return this.text; }
    public IContext context() { return this.context; }
}
