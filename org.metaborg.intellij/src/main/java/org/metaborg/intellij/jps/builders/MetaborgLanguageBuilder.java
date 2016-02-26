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

import com.google.common.collect.*;
import com.google.inject.*;
import org.jetbrains.annotations.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.builders.java.*;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.core.*;
import org.metaborg.core.action.*;
import org.metaborg.core.build.*;
import org.metaborg.core.build.dependency.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.configuration.*;
import org.metaborg.intellij.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.spoofax.core.resource.*;
import org.metaborg.spoofax.meta.core.*;
import org.metaborg.util.log.*;

import java.io.*;
import java.util.*;

public class MetaborgLanguageBuilder extends ModuleLevelBuilder {

    private final ILanguageManager languageManager;
    private final IMetaborgConfigService extensionService;
    private final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link MetaborgLanguageBuilder} class.
     */
    @Inject
    public MetaborgLanguageBuilder(final IMetaborgConfigService extensionService,
                                   final ILanguageManager languageManager,
                                   final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder) {
        super(BuilderCategory.SOURCE_GENERATOR);

        this.extensionService = extensionService;
        this.languageManager = languageManager;
        this.jpsSpoofaxMetaBuilder = jpsSpoofaxMetaBuilder;
    }

    @Override
    public ExitCode build(final CompileContext context, final ModuleChunk chunk,
                          final DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirtyFilesHolder,
                          final OutputConsumer outputConsumer)
            throws ProjectBuildException, IOException {

        final List<JpsModule> modules = getModulesToBuild(chunk);
        if (modules.isEmpty()) {
            this.logger.info("No modules with the Metaborg facet found in: {}", chunk.getModules());
            return ExitCode.NOTHING_DONE;
        }

        ExitCode exitCode = ExitCode.NOTHING_DONE;
        try {

            for (final JpsModule module : modules) {
                exitCode = buildModule(module, context);
                if (exitCode == ExitCode.ABORT)
                    return exitCode;
            }

        } catch (final ProjectBuildException e) {
            this.logger.error("An unexpected project build exception occurred.", e);
            throw e;
        } catch (final ProjectException e) {
            this.logger.error("An unexpected project exception occurred.", e);
            throw new ProjectBuildException(e);
        } catch (final Exception e) {
            this.logger.error("An unexpected exception occurred.", e);
            throw new ProjectBuildException(e);
        }

        return exitCode;
    }

    private List<JpsModule> getModulesToBuild(final ModuleChunk chunk) {
        final List<JpsModule> modules = new ArrayList<>();
        for (final JpsModule module : chunk.getModules()) {

            @Nullable final JpsMetaborgModuleFacetConfig facetConfiguration
                    = this.extensionService.getFacetConfiguration(module);

            // The module must have a Metaborg facet and NOT have a Metaborg configuration,
            // as a Metaborg configuration indicates it's a Language Specification project
            // which is treated differently.

            if (facetConfiguration == null) continue;

            if (this.extensionService.hasModuleConfiguration(module)) {
                this.logger.warn("Language Specification module should not have Metaborg facet: {}", module);
                continue;
            }

            modules.add(module);
        }
        return modules;
    }

    private ExitCode buildModule(final JpsModule module, final CompileContext context) throws Exception {
        final LanguageSpecBuildInput metaInput = this.jpsSpoofaxMetaBuilder.getLanguageSpecBuildInput(module);

        @Nullable final JpsMetaborgApplicationConfig configuration
                = this.extensionService.getGlobalConfiguration(context.getProjectDescriptor().getModel().getGlobal());

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

        this.jpsSpoofaxMetaBuilder.regularBuild(metaInput, context);

        this.logger.info("MetaborgLanguageBuilder invoked.");
        return ExitCode.OK;

    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Metaborg Language Builder";
    }

    @Override
    public List<String> getCompilableFileExtensions() {
        // TODO: Return list of all language extensions.
        return Lists.newArrayList("str");
    }

}
