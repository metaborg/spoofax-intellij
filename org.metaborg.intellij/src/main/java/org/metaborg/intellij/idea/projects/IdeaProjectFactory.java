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
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.*;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

public final class IdeaProjectFactory implements IIdeaProjectFactory {

    private final IProjectConfigService projectConfigService;
    private final ISourceTextService sourceTextService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link IdeaProjectFactory} class.
     */
    @Inject
    private IdeaProjectFactory(final IProjectConfigService projectConfigService,
                               final ISourceTextService sourceTextService) {
        this.projectConfigService = projectConfigService;
        this.sourceTextService = sourceTextService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaProject create(final Module module,
                              final FileObject rootFolder,
                              @Nullable IProjectConfig config) {

        final ModuleType moduleType = ModuleType.get(module);
        if (moduleType != MetaborgModuleType.getModuleType() && moduleType != JavaModuleType.getModuleType()) {
            this.logger.info("Project is not a Language Specification or Java project: {}", module);
            return null;
        }

        final ConfigRequest<IProjectConfig> configRequest = this.projectConfigService.get(rootFolder);
        if(!configRequest.valid()) {
            this.logger.error(
                    "An error occurred while retrieving the configuration for the project at: {}", rootFolder);
            configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
            return null;
        }

        if (config == null) {
            config = configRequest.config();
        }

        if(config == null) {
            this.logger.info("Project has no Metaborg configuration: {}", rootFolder);
            return null;
        }

        return new IdeaProject(module, rootFolder, config);
    }
}
