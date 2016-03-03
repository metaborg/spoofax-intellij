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

package org.metaborg.intellij.idea.projects;

import com.google.inject.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project service for IntelliJ IDEA.
 */
public final class IdeaProjectService implements IIdeaProjectService {

    private final IProjectConfigService projectConfigService;
    private final IIdeaProjectFactory projectFactory;
    private final IIntelliJResourceService resourceService;
    private final ISourceTextService sourceTextService;
    private final Map<Module, IdeaProject> modules = new HashMap<>();
    @InjectLogger
    private ILogger logger;

    @Inject
    private IdeaProjectService(final IProjectConfigService projectConfigService,
                               final IIdeaProjectFactory projectFactory,
                               final IIntelliJResourceService resourceService,
                               final ISourceTextService sourceTextService) {
        this.projectConfigService = projectConfigService;
        this.projectFactory = projectFactory;
        this.resourceService = resourceService;
        this.sourceTextService = sourceTextService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public IdeaProject open(final Module module, final FileObject rootFolder) {

        @Nullable final IdeaProject ideaProject = createProject(module, rootFolder);

        if (ideaProject == null) {

            final ModuleType moduleType = ModuleType.get(module);
            if (moduleType == MetaborgModuleType.getModuleType()) {
                throw LoggerUtils.exception(this.logger, RuntimeException.class, "Metaborg Language Specification " +
                        "project has no associated Metaborg Project.");
            }

            return null;
        }

        open(ideaProject);

        return ideaProject;
    }

    /**
     * Creates a new IDEA project for the specified module.
     *
     * @param module The module.
     * @param rootFolder The module's root folder.
     * @return The IDEA project; or <code>null</code>.
     */
    @Nullable
    private IdeaProject createProject(final Module module, final FileObject rootFolder) {

        final ModuleType moduleType = ModuleType.get(module);
        if (moduleType != MetaborgModuleType.getModuleType() && moduleType != JavaModuleType.getModuleType()) {
            this.logger.info("Project is not a Language Specification or Java project {}", module);
            return null;
        }

        final ConfigRequest<IProjectConfig> configRequest = this.projectConfigService.get(rootFolder);
        if(!configRequest.valid()) {
            this.logger.error(
                    "An error occurred while retrieving the configuration for the project at {}", rootFolder);
            configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
            return null;
        }

        @Nullable final IProjectConfig config = configRequest.config();
        if(config == null) {
            this.logger.info("Project has no Metaborg configuration {}", rootFolder);
            return null;
        }

        return this.projectFactory.create(module, rootFolder, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(final IdeaProject project) {
        this.modules.put(project.getModule(), project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close(final Module module) {
        this.modules.remove(module);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IdeaProject get(final Module module) {
        return this.modules.get(module);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IdeaProject get(final PsiElement element) {
        @Nullable final Module module = ModuleUtil.findModuleForPsiElement(element);
        if (module == null)
            return null;
        return get(module);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IdeaProject get(final FileObject resource) {
        @Nullable final VirtualFile file = this.resourceService.unresolve(resource);
        if (file == null)
            return null;

        @Nullable final Module module = getModule(file);
        if (module == null)
            return null;
        return get(module);
    }

    /**
     * Gets the {@link Module} for a given {@link VirtualFile}.
     *
     * @param file The file to find.
     * @return The {@link Module} of the file.
     */
    @Nullable
    private Module getModule(final VirtualFile file) {
        final Set<Module> candidates = new HashSet<>();
        for (final Project project : ProjectManager.getInstance().getOpenProjects()) {
            @Nullable final Module module = ModuleUtil.findModuleForFile(file, project);
            if (module != null)
                candidates.add(module);
        }
        if (candidates.size() > 1) {
            this.logger.error(
                    "File {} found in multiple modules. Picking a random one. These modules: {}",
                    file,
                    candidates
            );
        }
        if (!candidates.isEmpty()) {
            return candidates.iterator().next();
        }
        return null;
    }
}
