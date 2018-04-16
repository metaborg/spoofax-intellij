/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.transformations;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;

/**
 * A tuple with a resource to transform, along with the (part of its) source code to transform.
 */
public final class TransformResource {

    private final FileObject resource;
    private final IProject project;
    private final String text;

    /**
     * Initializes a new instance of the {@link TransformResource} class.
     *
     * @param resource The resource to transform.
     * @param project  The project to which the resource belongs.
     * @param text     The text to transform.
     */
    public TransformResource(final FileObject resource, final IProject project, final String text) {
        this.resource = resource;
        this.project = project;
        this.text = text;
    }

    /**
     * Gets the resource to transform.
     *
     * @return The resource to transform.
     */
    public FileObject resource() {
        return this.resource;
    }

    /**
     * Gets the project to which the resource belongs.
     *
     * @return The project of the resource.
     */
    public IProject project() { return this.project; }

    /**
     * Gets the text to transform.
     *
     * @return The text to transform.
     */
    public String text() {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.resource.toString();
    }
}
