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

package org.metaborg.intellij.jps.projects;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.JpsUrlList;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.config.*;
import org.metaborg.core.project.*;

import jakarta.annotation.Nullable;
import java.util.List;

/**
 * A Spoofax project used in JPS.
 */
public class MetaborgJpsProject extends Project {

    private final JpsModule module;

    /**
     * Initializes a new instance of the {@link MetaborgJpsProject} class.
     *
     * @param location
     *            The location of the project root.
     */
    @jakarta.inject.Inject
    public MetaborgJpsProject(
            @Assisted final JpsModule module,
            @Assisted final FileObject location,
            @Assisted @Nullable final IProjectConfig config) {
        super(location, config);
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
        // String projectRoot = this.module.getContentRootsList().getUrls().get(0);
        // return new MetaborgJpsProject(this.resourceService.resolve(projectRoot));
    }

    /**
     * Gets the JPS module of this project.
     */
    public JpsModule getModule() {
        return this.module;
    }

    /**
     * {@inheritDoc}
     */
    @Override public String toString() {
        return this.module.getName();
    }
}
