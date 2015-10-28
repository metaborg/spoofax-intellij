/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Inject;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.factories.IProjectFactory;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.project.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.project.IntelliJProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.slf4j.Logger;

import javax.annotation.Nullable;

/**
 * Module component that handles module events.
 */
public final class SpoofaxModuleComponent implements ModuleComponent {

    @NotNull
    private final Module module;
    @InjectLogger
    private Logger logger;
    @NotNull
    private IIntelliJProjectService projectService;
    @NotNull
    private IProjectFactory projectFactory;
    @NotNull
    private IIntelliJResourceService resourceService;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxModuleComponent(@NotNull final Module module) {
        this.module = module;
        IdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(
            @NotNull final IIntelliJProjectService projectService,
            @NotNull final IProjectFactory projectFactory,
            @NotNull final IIntelliJResourceService resourceService) {
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.resourceService = resourceService;
    }

    @NotNull
    public String getComponentName() {
        return "SpoofaxModuleComponent";
    }

    /**
     * Occurs when the module is initialized.
     */
    public void initComponent() {
        logger.info("Module {} init.", this.module);
    }

    /**
     * Occurs when the module is disposed.
     * <p>
     * Called after {@link #projectClosed()}.
     */
    public void disposeComponent() {
        logger.info("Module {} dispose.", this.module);
        // TODO: insert component disposal logic here
    }

    /**
     * Occurs when the module is opened.
     * <p>
     * Called after {@link #initComponent()} and {@link #moduleAdded()}.
     * <p>
     * This method is not called when modules are created
     * in a {@link com.intellij.ide.util.projectWizard.ModuleBuilder}.
     */
    public void projectOpened() {
        logger.info("Module {} opened.", this.module);

        FileObject root = getRootDirectory(this.module);
        if (root == null)
            return;
        IntelliJProject project = this.projectFactory.create(this.module, root);
        this.projectService.open(project);
    }

    /**
     * Occurs when the module is closed.
     */
    public void projectClosed() {
        logger.info("Module {} closed.", this.module);

        this.projectService.close(this.module);
    }

    /**
     * Occurs when the module has been completely loaded and added to the project.
     * <p>
     * Called after {@link #initComponent()}. May be called twice for a module.
     */
    public void moduleAdded() {
        logger.info("Module {} added.", this.module);
    }

    /**
     * Gets the root directory of the module.
     *
     * @param module The module.
     * @return The root directory; or <code>null</code>.
     */
    @Nullable
    private FileObject getRootDirectory(@NotNull final Module module) {
        try {
            return this.resourceService.resolve(module.getModuleFilePath()).getParent();
        } catch (FileSystemException e) {
            logger.error("Unhandled exception.", e);
        }
        return null;
    }
}
