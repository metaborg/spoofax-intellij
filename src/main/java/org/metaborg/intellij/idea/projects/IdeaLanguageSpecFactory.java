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


import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import jakarta.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.ConfigRequest;
import org.metaborg.core.messages.StreamMessagePrinter;
import org.metaborg.core.source.ISourceTextService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfigService;
import org.metaborg.util.log.ILogger;

import jakarta.annotation.Nullable;

public final class IdeaLanguageSpecFactory implements IIdeaLanguageSpecFactory {

    private final ISourceTextService sourceTextService;
    private final ISpoofaxLanguageSpecConfigService configService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link IdeaLanguageSpecFactory} class.
     */
    @Inject
    public IdeaLanguageSpecFactory(
            final ISourceTextService sourceTextService,
            final ISpoofaxLanguageSpecConfigService configService) {
        this.sourceTextService = sourceTextService;
        this.configService = configService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaLanguageSpec create(final Module module,
                                   final FileObject rootFolder,
                                   @Nullable ISpoofaxLanguageSpecConfig config) {

        final ModuleType moduleType = ModuleType.get(module);
        if (moduleType != MetaborgModuleType.getModuleType()) {
            this.logger.info("Module is not a language specification: {}", module);
            return null;
        }

        @Nullable final ConfigRequest<ISpoofaxLanguageSpecConfig> configRequest = this.configService.get(rootFolder);
        if(configRequest == null || !configRequest.valid()) {
            this.logger.error(
                    "Errors occurred when retrieving language specification configuration from project at: {}",
                    rootFolder);
            if (configRequest != null) {
                configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
            }
            return null;
        }

        if (config == null) {
            config = configRequest.config();
        }

        if(config == null) {
            this.logger.error(
                    "Could not get the configuration of the project: {}",
                    rootFolder);
            return null;
        }

        return new IdeaLanguageSpec(module, rootFolder, config);
    }
}
