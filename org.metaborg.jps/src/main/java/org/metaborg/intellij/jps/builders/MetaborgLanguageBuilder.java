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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.configuration.IMetaborgConfigService;
import org.metaborg.intellij.jps.configuration.JpsMetaborgModuleFacetConfig;
import org.metaborg.intellij.languages.ILanguageManager;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.spoofax.meta.core.build.*;
import org.metaborg.util.log.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildStarted(final CompileContext context) {
        this.logger.info("Build started!");
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
                this.jpsSpoofaxMetaBuilder.getOrCreateProject(module);

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
        final ProjectBuildInput metaInput = this.jpsSpoofaxMetaBuilder.getProjectBuildInput(module);

        this.jpsSpoofaxMetaBuilder.beforeBuild(metaInput, context);
        this.jpsSpoofaxMetaBuilder.regularBuild(metaInput, context);
        this.jpsSpoofaxMetaBuilder.afterBuild(metaInput, context);

        return ExitCode.OK;

    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Metaborg Language Builder";
    }

    @Override
    public List<String> getCompilableFileExtensions() {
        // We want all files.
        return null;
    }

}
