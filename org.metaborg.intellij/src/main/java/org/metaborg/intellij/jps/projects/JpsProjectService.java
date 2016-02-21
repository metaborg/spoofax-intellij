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

package org.metaborg.intellij.jps.projects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.JpsUrlList;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfigService;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpecPaths;
import org.metaborg.spoofax.meta.core.project.SpoofaxLanguageSpecPaths;
import org.metaborg.util.log.ILogger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A project service for JPS.
 * <p>
 * Due to how JPS works, we'll have one project service per JPS module.
 */
@Singleton
public final class JpsProjectService implements IJpsProjectService {

    private final List<MetaborgJpsProject> projects = new ArrayList<>();
    private final IResourceService resourceService;
    private final ISpoofaxLanguageSpecConfigService languageSpecConfigService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public JpsProjectService(final IResourceService resourceService, ISpoofaxLanguageSpecConfigService languageSpecConfigService) {
        this.resourceService = resourceService;
        this.languageSpecConfigService = languageSpecConfigService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetaborgJpsProject create(final JpsModule module) {
        final FileObject location = this.resourceService.resolve(module.getContentRootsList().getUrls().get(0));
        final ISpoofaxLanguageSpecConfig config = languageSpecConfigService.get(location);
        final ISpoofaxLanguageSpecPaths paths = new SpoofaxLanguageSpecPaths(location, config);
        final MetaborgJpsProject project = new MetaborgJpsProject(module, location, config, paths);
        this.projects.add(project);
        return project;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public MetaborgJpsProject get(final JpsModule module) {
        for (final MetaborgJpsProject project : this.projects) {
            if (project.module().equals(module))
                return project;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgJpsProject get(final FileObject resource) {
        for (final MetaborgJpsProject project : this.projects) {
            final JpsModule module = project.module();
            if (isInContentRoot(module, resource))
                return project;
        }
        return null;
    }

    /**
     * Determines whether the file is in a content root of the specified module.
     *
     * @param module   The module to look at.
     * @param resource The file to find.
     * @return <code>true</code> when the file is in a content root of the module;
     * otherwise, <code>false</code>.
     */
    private boolean isInContentRoot(final JpsModule module, final FileObject resource) {
        final JpsUrlList contentRootsList = module.getContentRootsList();
        for (final String url : contentRootsList.getUrls()) {
            if (isEqualOrDescendant(url, resource))
                return true;
        }
        return false;
    }

    /**
     * Determines whether the specified file is equal to or a descendant of the specified path.
     *
     * @param ancestor   The path.
     * @param descendant The descendant.
     * @return <code>true</code> when the file is equal to or a descendant of the path;
     * otherwise, <code>false</code>.
     */
    private boolean isEqualOrDescendant(final String ancestor, final FileObject descendant) {
        final FileObject contentRoot = this.resourceService.resolve(ancestor);
        final FileName lhs = contentRoot.getName();
        final FileName rhs = descendant.getName();
        return lhs.equals(rhs) || lhs.isDescendent(rhs);
    }

}
