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


import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.intellij.idea.compilation.IAfterCompileTask;
import org.metaborg.intellij.idea.compilation.IBeforeCompileTask;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import java.util.Set;

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

    @jakarta.inject.Inject @javax.inject.Inject
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
