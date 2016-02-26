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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.core.build.dependency.*;
import org.metaborg.core.build.paths.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.jps.configuration.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.projects.*;
import org.metaborg.spoofax.core.processing.*;
import org.metaborg.spoofax.core.project.*;
import org.metaborg.spoofax.core.project.configuration.*;
import org.metaborg.spoofax.meta.core.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * Builder executed before Java compilation.
 */
@Singleton
public final class SpoofaxPreBuilder extends MetaborgMetaBuilder2<SpoofaxPreTarget> {

    private final ILanguageManager languageManager;
    private final IMetaborgConfigService extensionService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link SpoofaxPreBuilder} class.
     */
    @Inject
    public SpoofaxPreBuilder(
            final SpoofaxPreTargetType targetType,
            final IJpsProjectService projectService,
            final ILanguageSpecService languageSpecService,
            final ISpoofaxLanguageSpecConfigService spoofaxLanguageSpecConfigService,
            final ILanguageManager languageManager,
            final ISpoofaxLanguageSpecPathsService pathsService,
            final IMetaborgConfigService extensionService,
            final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder) {
        super(targetType, jpsSpoofaxMetaBuilder, projectService, languageSpecService, pathsService, spoofaxLanguageSpecConfigService);

        this.languageManager = languageManager;
        this.extensionService = extensionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getPresentableName() {
        return "Spoofax pre-Java builder";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doBuild(
            final LanguageSpecBuildInput metaInput,
            final SpoofaxPreTarget target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPreTarget> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws Exception {

        @Nullable final JpsMetaborgApplicationConfig configuration = this.extensionService.getGlobalConfiguration(
                context.getProjectDescriptor().getModel().getGlobal());

        if (configuration != null) {
            final Set<LanguageIdentifier> appLanguages = configuration.getLoadedLanguages();
            this.logger.debug("Loading application languages: {}", appLanguages);
            this.languageManager.discoverRange(appLanguages);
            this.logger.info("Loaded application languages: {}", appLanguages);
        } else {
            this.logger.warn("No application configuration found.");
        }

        final Collection<LanguageIdentifier> languages = metaInput.config.compileDependencies();
        this.logger.debug("Loading module languages: {}", languages);
        this.languageManager.discoverRange(languages);
        this.logger.info("Loaded module languages: {}", languages);

        this.jpsSpoofaxMetaBuilder.clean(metaInput, context);
        this.jpsSpoofaxMetaBuilder.initialize(metaInput, context);
        this.jpsSpoofaxMetaBuilder.generateSources(metaInput, context);
        this.jpsSpoofaxMetaBuilder.regularBuild(metaInput, context);
        this.jpsSpoofaxMetaBuilder.compilePreJava(metaInput, context);
    }

}