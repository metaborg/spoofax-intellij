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

package org.metaborg.intellij.idea.project;

import com.google.inject.Inject;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.util.log.ILogger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Project service for IntelliJ IDEA.
 */
public final class IdeaProjectService implements IIdeaProjectService {

    private final IIntelliJResourceService resourceService;
    private final Map<Module, IdeaProject> modules = new HashMap<>();
    @InjectLogger
    private ILogger logger;

    @Inject
    private IdeaProjectService(final IIntelliJResourceService resourceService) {
        this.resourceService = resourceService;
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
