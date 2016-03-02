/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Spoofax for IntelliJ. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.jps.projects;

import java.util.List;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.JpsUrlList;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.config.*;
import org.metaborg.core.project.*;
import org.metaborg.meta.core.config.ILanguageSpecConfig;
import org.metaborg.meta.core.project.ILanguageSpec;
import org.metaborg.meta.core.project.ILanguageSpecPaths;
import org.metaborg.meta.core.project.LanguageSpec;

import javax.annotation.*;

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
    @Inject
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
