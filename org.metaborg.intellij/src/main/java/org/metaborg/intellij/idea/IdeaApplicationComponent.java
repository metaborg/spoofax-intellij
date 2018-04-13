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

import com.google.inject.Inject;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.metaborg.intellij.configuration.IMetaborgApplicationConfig;
import org.metaborg.intellij.idea.configuration.ConfigurationFileEventListener;
import org.metaborg.intellij.idea.configuration.ConfigurationUtils;
import org.metaborg.intellij.idea.configuration.IdeaMetaborgApplicationConfig;
import org.metaborg.intellij.logging.InjectLogger;
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

        SpoofaxIdeaPlugin.plugin().close();

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
