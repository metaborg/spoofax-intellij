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

package org.metaborg.intellij.idea;

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.compilation.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * IntelliJ IDEA project service.
 */
public final class IdeaProjectComponent implements ProjectComponent {

    private final Project project;
    private Set<IBeforeCompileTask> beforeCompileTasks;
    private Set<IAfterCompileTask> afterCompileTasks;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public IdeaProjectComponent(final Project project) {
        this.project = project;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final Set<IBeforeCompileTask> beforeCompileTasks,
                        final Set<IAfterCompileTask> afterCompileTasks) {
        this.beforeCompileTasks = beforeCompileTasks;
        this.afterCompileTasks = afterCompileTasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing project: {}", this.project);

        setupCompileTasks();
        this.logger.info("Initialized project: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void projectOpened() {
        this.logger.debug("Opening project: {}", this.project);

        this.logger.info("Opened project: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void projectClosed() {
        this.logger.debug("Closing project: {}", this.project);

        this.logger.info("Closed project: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing project: {}", this.project);

        this.logger.info("Disposed project: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return IdeaProjectComponent.class.getName();
    }

    /**
     * Setups the compile tasks.
     */
    private void setupCompileTasks() {
        this.logger.debug("Registering compile tasks for project: {}", this.project);
        final CompilerManager compilerManager = CompilerManager.getInstance(this.project);

        for (final IBeforeCompileTask task : this.beforeCompileTasks) {
            compilerManager.addBeforeTask(task);
        }

        for (final IAfterCompileTask task : this.afterCompileTasks) {
            compilerManager.addAfterTask(task);
        }

        this.logger.info("Registered compile tasks for project: {}", this.project);
    }
}
