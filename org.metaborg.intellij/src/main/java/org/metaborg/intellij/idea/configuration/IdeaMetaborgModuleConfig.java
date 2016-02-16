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

package org.metaborg.intellij.idea.configuration;

import com.google.inject.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.core.project.configuration.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.projects.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

/**
 * Module-level configuration of the plugin.
 */
@State(
        name = IMetaborgModuleConfig.CONFIG_NAME,
        storages = {
                @Storage(file = StoragePathMacros.MODULE_FILE)
        }
)
public final class IdeaMetaborgModuleConfig implements IMetaborgModuleConfig, ModuleComponent,
        PersistentStateComponent<MetaborgModuleConfigState> {

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private MetaborgModuleConfigState state;
    private final Module module;
    private IIdeaLanguageManager languageManager;
    private ProjectUtils projectUtils;
    private IIdeaProjectService projectService;
    private IIdeaProjectFactory projectFactory;
    private ILanguageSpecService languageSpecService;
    private ILanguageSpecConfigService configService;
    private IIntelliJResourceService resourceService;
    private ConfigurationUtils configurationUtils;
    @InjectLogger
    private ILogger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.state.myName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String value) {
        this.state.myName = value;
    }

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public IdeaMetaborgModuleConfig(final Module module) {
        this.module = module;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        loadState(new MetaborgModuleConfigState());
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(
            final IIdeaProjectService projectService,
            final IIdeaProjectFactory projectFactory,
            final IIntelliJResourceService resourceService,
            final IIdeaLanguageManager languageManager,
            final ILanguageSpecService languageSpecService,
            final ILanguageSpecConfigService configService,
            final ConfigurationUtils configurationUtils,
            final ProjectUtils projectUtils) {
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.resourceService = resourceService;
        this.languageManager = languageManager;
        this.languageSpecService = languageSpecService;
        this.configService = configService;
        this.configurationUtils = configurationUtils;
        this.projectUtils = projectUtils;
    }

    /**
     * Occurs when the module is initialized.
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing module configuration for: {}", this.module);
        this.logger.info("Initialized module configuration for: {}", this.module);
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
        this.logger.debug("Opening module configuration for: {}", this.module);

        registerModule();
        this.configurationUtils.loadAndActivateLanguages(this.module.getProject(), getCompileDependencies());

        this.logger.info("Opened module configuration for: {}", this.module);
    }

    /**
     * Occurs when the module has been completely loaded and added to the project.
     * <p>
     * Called after {@link #initComponent()}. May be called twice for a module.
     */
    @Override
    public void moduleAdded() {
        this.logger.debug("Adding module configuration for: {}", this.module);
        this.logger.info("Added module configuration for: {}", this.module);
    }

    /**
     * Occurs when the module is closed.
     */
    @Override
    public void projectClosed() {
        this.logger.debug("Closing module configuration for: {}", this.module);

        unregisterModule();

        this.logger.info("Closed module configuration for: {}", this.module);
    }

    /**
     * Occurs when the module is disposed.
     * <p>
     * Called after {@link #projectClosed()}.
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing module configuration for: {}", this.module);
        this.logger.info("Disposed module configuration for: {}", this.module);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return IdeaMetaborgModuleConfig.class.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgModuleConfigState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * This method is only called if the configuration has changed.
     */
    @Override
    public void loadState(final MetaborgModuleConfigState state) {
        this.state = state;
        // Initialize fields that depend on state here.
    }

    /**
     * Registers the module as opened.
     */
    private void registerModule() {
        @Nullable final FileObject root = getRootDirectory(this.module);
        if (root == null)
            return;
        final IdeaProject project = this.projectFactory.create(this.module, root);
        this.projectService.open(project);
    }

    /**
     * Unregisters the module as opened.
     */
    private void unregisterModule() {
        this.projectService.close(this.module);
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

    /**
     * Gets the compile dependencies of the module.
     *
     * @return The compile dependencies.
     */
    private Collection<LanguageIdentifier> getCompileDependencies() {
        @Nullable final IdeaProject project = this.projectService.get(this.module);
        @Nullable final ILanguageSpec languageSpec = this.languageSpecService.get(project);
        if (languageSpec == null) {
            this.logger.error("Can't get language specification for project: {}", project);
            return Collections.emptyList();
        }
        return this.projectUtils.getCompileDependencies(languageSpec);
    }
}
