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

package org.metaborg.intellij.projects;

import com.google.inject.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.core.project.configuration.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * Project utility functions.
 */
public final class ProjectUtils {

    private final ILanguageSpecService languageSpecService;
    private final ILanguageSpecConfigService configService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public ProjectUtils(final ILanguageSpecConfigService configService,
                        final ILanguageSpecService languageSpecService) {
        this.configService = configService;
        this.languageSpecService = languageSpecService;
    }

    /**
     * Gets the compile dependencies of the module.
     *
     * @return The compile dependencies.
     */
    public Collection<LanguageIdentifier> getCompileDependencies(@Nullable final ILanguageSpec languageSpec) {
        @Nullable final ILanguageSpecConfig config;
        try {
            config = this.configService.get(languageSpec);
        } catch (final IOException e) {
            this.logger.error("Can't get configuration for language specification: {}", e, languageSpec);
            return Collections.emptyList();
        }
        if (config == null) {
            this.logger.error("Got no configuration for language specification: {}", languageSpec);
            return Collections.emptyList();
        }
        return config.compileDependencies();
    }


    /**
     * Gets the compile dependencies of the project.
     *
     * @param project The project.
     * @return The compile dependencies.
     */
    public Collection<LanguageIdentifier> getCompileDependencies(@Nullable final IdeaProject project) {
        @Nullable final ILanguageSpec languageSpec = this.languageSpecService.get(project);
        if (languageSpec == null) {
            this.logger.error("Can't get language specification for project: {}", project);
            return Collections.emptyList();
        }
        return getCompileDependencies(languageSpec);
    }

}
