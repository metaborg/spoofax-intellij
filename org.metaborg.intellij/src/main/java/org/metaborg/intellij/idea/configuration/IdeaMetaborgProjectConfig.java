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
import com.intellij.openapi.project.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Project-level configuration of the plugin.
 */
@State(
        name = IMetaborgProjectConfig.CONFIG_NAME,
        storages = {
                @Storage(file = StoragePathMacros.PROJECT_FILE),
                @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + IMetaborgProjectConfig.CONFIG_FILE,
                        scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public final class IdeaMetaborgProjectConfig implements IMetaborgProjectConfig, ProjectComponent,
        PersistentStateComponent<MetaborgProjectConfigState> {

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    private MetaborgProjectConfigState state;
    private final Project project;
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
    public IdeaMetaborgProjectConfig(final Project project) {
        this.project = project;
        SpoofaxIdeaPlugin.injector().injectMembers(this);
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        loadState(new MetaborgProjectConfigState());
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing project configuration for: {}", this.project);
        this.logger.info("Initialized project configuration for: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void projectOpened() {
        this.logger.debug("Opening project configuration for: {}", this.project);

        this.logger.info("Opened project configuration for: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void projectClosed() {
        this.logger.debug("Closing project configuration for: {}", this.project);

        this.logger.info("Closed project configuration for: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing project configuration for: {}", this.project);
        this.logger.info("Disposed project configuration for: {}", this.project);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return IdeaMetaborgProjectConfig.class.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MetaborgProjectConfigState getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     *
     * This method is only called if the configuration has changed.
     */
    @Override
    public void loadState(final MetaborgProjectConfigState state) {
        this.state = state;
        // Initialize fields that depend on state here.
    }

}
