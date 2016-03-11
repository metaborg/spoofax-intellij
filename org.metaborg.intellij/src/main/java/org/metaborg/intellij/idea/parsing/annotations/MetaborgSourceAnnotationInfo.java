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

package org.metaborg.intellij.idea.parsing.annotations;

import org.apache.commons.vfs2.*;
import org.metaborg.core.context.*;

/**
 * Contains the collected annotation info.
 */
public final class MetaborgSourceAnnotationInfo {

    private final FileObject resource;
    private final String text;
    private final IContext context;

    /**
     * Initializes a new instance of the {@link MetaborgSourceAnnotationInfo} class.
     *
     * @param resource The annotated resource.
     * @param text The annotated resource's content.
     * @param context The context.
     */
    public MetaborgSourceAnnotationInfo(final FileObject resource, final String text, final IContext context) {
        this.resource = resource;
        this.text = text;
        this.context = context;
    }

    /**
     * Gets the annotated resource.
     *
     * @return The resource.
     */
    public FileObject resource() { return this.resource; }

    /**
     * Gets the annotated resource's content.
     *
     * @return The content.
     */
    public String text() { return this.text; }

    /**
     * Gets the context.
     *
     * @return The context.
     */
    public IContext context() { return this.context; }
}
