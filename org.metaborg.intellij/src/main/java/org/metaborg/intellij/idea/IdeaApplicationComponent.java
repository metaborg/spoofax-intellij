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

import com.google.inject.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Application-level component.
 *
 * This component will perform any start-up actions (such as loading and activating configured languages).
 */
public final class IdeaApplicationComponent implements ApplicationComponent {

    private IMetaborgApplicationConfig configuration;
    private ConfigurationUtils configurationUtils;
    private ConfigurationFileEventListener configurationFileEventListener;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public IdeaApplicationComponent() {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final IMetaborgApplicationConfig configuration,
                        final ConfigurationUtils configurationUtils,
                        final ConfigurationFileEventListener configurationFileEventListener) {
        this.configuration = configuration;
        this.configurationUtils = configurationUtils;
        this.configurationFileEventListener = configurationFileEventListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initComponent() {
        this.logger.debug("Initializing application.");

        this.configurationUtils.loadAndActivateLanguages(null, this.configuration.getLoadedLanguages());

        VirtualFileManager.getInstance().addVirtualFileListener(this.configurationFileEventListener);

        this.logger.info("Initialized application.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeComponent() {
        this.logger.debug("Disposing application.");
        this.logger.info("Disposed application.");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getComponentName() {
        return IdeaMetaborgApplicationConfig.class.getName();
    }
}
