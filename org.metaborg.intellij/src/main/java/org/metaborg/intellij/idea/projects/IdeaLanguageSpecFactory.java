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
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

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

        // TODO: Use ISpoofaxLanguageSpecPathsService instead.
        final SpoofaxLanguageSpecPaths paths = new SpoofaxLanguageSpecPaths(rootFolder, config);

        return new IdeaLanguageSpec(module, rootFolder, config, paths);
    }
}
