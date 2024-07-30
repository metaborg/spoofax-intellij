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


import jakarta.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import jakarta.annotation.Nullable;

public final class ArtifactProjectFactory implements IArtifactProjectFactory {

    private final ISourceTextService sourceTextService;
    private final IProjectConfigService projectConfigService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public ArtifactProjectFactory(final ISourceTextService sourceTextService,
                                  final IProjectConfigService projectConfigService) {
        this.sourceTextService = sourceTextService;
        this.projectConfigService = projectConfigService;
    }

    @Override
    public ArtifactProject create(final FileObject artifactRoot,
                                  @Nullable IProjectConfig config) {

        if (config == null) {
            final ConfigRequest<? extends IProjectConfig> configRequest = this.projectConfigService.get(artifactRoot);
            if (!configRequest.valid()) {
                this.logger.error("Errors occurred when retrieving project configuration from project directory: {}",
                        artifactRoot);
                configRequest.reportErrors(new StreamMessagePrinter(this.sourceTextService, false, false, this.logger));
                return null;
            }

            config = configRequest.config();
            if (config == null) {
                this.logger.error("Got no configuration for artifact at: {}", artifactRoot);
                return null;
            }
        }
        return new ArtifactProject(artifactRoot, config);
    }

}
