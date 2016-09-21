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

import com.google.inject.Inject;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;

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
