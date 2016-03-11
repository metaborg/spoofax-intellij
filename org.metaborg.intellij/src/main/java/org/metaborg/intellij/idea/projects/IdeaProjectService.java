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
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.resources.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project service for IntelliJ IDEA.
 */
public final class IdeaProjectService implements IIdeaProjectService {

    private final IIntelliJResourceService resourceService;
    private final Map<Module, IdeaProject> modules = new HashMap<>();
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link IdeaProjectService} class.
     */
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
