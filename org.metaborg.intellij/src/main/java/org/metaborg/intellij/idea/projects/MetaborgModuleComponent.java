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
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.*;
import org.apache.commons.vfs2.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.annotation.*;

/**
 * Module component that handles Metaborg module events.
 */
public final class MetaborgModuleComponent implements ModuleComponent {

    private final Module module;
    @InjectLogger
    private ILogger logger;
    private IIdeaProjectService projectService;
    private IIdeaProjectFactory projectFactory;
    private IIntelliJResourceService resourceService;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgModuleComponent(final Module module) {
        this.module = module;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(
            final IIdeaProjectService projectService,
            final IIdeaProjectFactory projectFactory,
            final IIntelliJResourceService resourceService) {
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.resourceService = resourceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComponentName() {
        return MetaborgModuleComponent.class.getName();
    }

    /**
     * Occurs when the module is initialized.
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing Metaborg module: {}", this.module);
        this.logger.info("Initialized Metaborg module: {}", this.module);
    }

    /**
     * Occurs when the module is disposed.
     * <p>
     * Called after {@link #projectClosed()}.
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing Metaborg module: {}", this.module);
        this.logger.info("Disposed Metaborg module: {}", this.module);
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
        this.logger.debug("Opening Metaborg module: {}", this.module);

        @Nullable final FileObject root = getRootDirectory(this.module);
        if (root == null)
            return;
        final IdeaProject project = this.projectFactory.create(this.module, root);
        this.projectService.open(project);

        this.logger.info("Opened Metaborg module: {}", this.module);
    }

    /**
     * Occurs when the module is closed.
     */
    @Override
    public void projectClosed() {
        this.logger.debug("Closing Metaborg module: {}", this.module);

        this.projectService.close(this.module);

        this.logger.info("Closed Metaborg module: {}", this.module);
    }

    /**
     * Occurs when the module has been completely loaded and added to the project.
     * <p>
     * Called after {@link #initComponent()}. May be called twice for a module.
     */
    @Override
    public void moduleAdded() {
        this.logger.info("Module {} added.", this.module);
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
