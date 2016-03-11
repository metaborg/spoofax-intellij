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
import org.apache.commons.vfs2.*;
import org.metaborg.core.config.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

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
            final ConfigRequest<IProjectConfig> configRequest = this.projectConfigService.get(artifactRoot);
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
