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

package org.metaborg.intellij.idea;

import com.google.inject.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.projects.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;

/**
 * Module-level component.
 */
public final class IdeaModuleComponent implements ModuleComponent {

    private final Module module;
    private ProjectUtils projectUtils;
    private IIdeaProjectFactory projectFactory;
    private IIdeaLanguageSpecFactory languageSpecFactory;
    private IIdeaProjectService projectService;
    private IIdeaLanguageManager languageManager;
    private IIntelliJResourceService resourceService;
    private ConfigurationUtils configurationUtils;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public IdeaModuleComponent(final Module module) {
        this.module = module;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(
            final IIdeaProjectService projectService,
            final IIdeaProjectFactory projectFactory,
            final IIdeaLanguageSpecFactory languageSpecFactory,
            final IIdeaLanguageManager languageManager,
            final IIntelliJResourceService resourceService,
            final ConfigurationUtils configurationUtils,
            final ProjectUtils projectUtils,
            final ISourceTextService sourceTextService,
            final ISpoofaxLanguageSpecConfigService configService) {
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.languageSpecFactory = languageSpecFactory;
        this.languageManager = languageManager;
        this.resourceService = resourceService;
        this.configurationUtils = configurationUtils;
        this.projectUtils = projectUtils;
    }

    /**
     * Occurs when the module is initialized.
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing module: {}", this.module);
        this.logger.info("Initialized module: {}", this.module);
    }

    /**
     * Occurs when the module is opened.
     * <p>
     * Called after {@link #initComponent()} and {@link #moduleAdded()}.
     * <p>
     * This method is not called when modules are created
     * in a {@link com.intellij.ide.util.projectWizard.ModuleBuilder}.
     */
    @Override
    public void projectOpened() {
        this.logger.debug("Opening module: {}", this.module);

        @Nullable final IdeaProject project = registerModule();
        if (project == null)
            return;
        this.configurationUtils.loadAndActivateLanguages(this.module.getProject(),
                this.projectUtils.getCompileDeps(project));

        WriteCommandAction.runWriteCommandAction(this.module.getProject(), () -> {
            if (project instanceof IdeaLanguageSpec) {
                this.logger.debug("Loading languages of language specification: {}", project);
                this.languageManager.loadLanguageSpec((IdeaLanguageSpec)project);
            } else {
                this.logger.debug("Module loading skipped as it's not a language specification project: {}",
                        this.module);
            }
        });

        this.logger.info("Opened module: {}", this.module);
    }

    /**
     * Occurs when the module has been completely loaded and added to the project.
     * <p>
     * Called after {@link #initComponent()}. May be called twice for a module.
     */
    @Override
    public void moduleAdded() {
        this.logger.debug("Adding module: {}", this.module);
        this.logger.info("Added module: {}", this.module);
    }

    /**
     * Occurs when the module is closed.
     */
    @Override
    public void projectClosed() {
        this.logger.debug("Closing module: {}", this.module);

        @Nullable final IdeaProject project = unregisterModule();

        if (project == null)
            return;

        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            if (project instanceof IdeaLanguageSpec) {
                this.logger.debug("Unloading languages of language specification: {}", project);
                this.languageManager.unloadLanguageSpec((IdeaLanguageSpec)project);
            } else {
                this.logger.debug("Module unloading skipped as it's not a language specification project: {}",
                        this.module);
            }
        }));

        this.logger.info("Closed module: {}", this.module);
    }

    /**
     * Occurs when the module is disposed.
     * <p>
     * Called after {@link #projectClosed()}.
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing module: {}", this.module);
        this.logger.info("Disposed module: {}", this.module);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return IdeaModuleComponent.class.getName();
    }

    /**
     * Registers the module as opened.
     *
     * @return The project; or <code>null</code> when it wasn't registered.
     */
    @Nullable
    private IdeaProject registerModule() {
        @Nullable final FileObject rootFolder = getRootDirectory(this.module);
        if (rootFolder == null)
            return null;

        @Nullable IdeaProject project;
        project = this.languageSpecFactory.create(this.module, rootFolder, null);
        if (project == null) {
            project = this.projectFactory.create(this.module, rootFolder, null);
        }

        if (project != null) {
            this.projectService.open(project);
        }

        return project;
    }

    /**
     * Unregisters the module as opened.
     */
    @Nullable
    private IdeaProject unregisterModule() {
        return this.projectService.close(this.module);
    }

    /**
     * Gets the root directory of the module.
     *
     * @param module The module.
     * @return The root directory; or <code>null</code>.
     */
    @Nullable
    private FileObject getRootDirectory(final Module module) {
        try {
            return this.resourceService.resolve(module.getModuleFilePath()).getParent();
        } catch (final FileSystemException e) {
            this.logger.error("Unhandled exception.", e);
        }
        return null;
    }
}
