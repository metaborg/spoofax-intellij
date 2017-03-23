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

package org.metaborg.intellij.jps.configuration;

import com.google.inject.*;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.metaborg.intellij.configuration.IMetaborgApplicationConfig;
import org.metaborg.intellij.configuration.MetaborgApplicationConfigState;

import javax.annotation.Nullable;

/**
 * Deserializes the application-wide configuration in JPS.
 */
public final class MetaborgApplicationConfigDeserializer extends JpsGlobalExtensionSerializer {

    private final IJpsMetaborgApplicationConfigFactory configFactory;
    private final IMetaborgConfigService extensionService;

    @Inject
    public MetaborgApplicationConfigDeserializer(final IJpsMetaborgApplicationConfigFactory configFactory,
                                                 final IMetaborgConfigService extensionService) {
        super(IMetaborgApplicationConfig.CONFIG_FILE, IMetaborgApplicationConfig.CONFIG_NAME);
        assert configFactory != null;
        assert extensionService != null;
        this.configFactory = configFactory;
        this.extensionService = extensionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtension(final JpsGlobal global, final Element element) {
        @Nullable final MetaborgApplicationConfigState state = XmlSerializer.deserialize(element,
                MetaborgApplicationConfigState.class);
        setConfig(global, buildConfig(state));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtensionWithDefaultSettings(final JpsGlobal global) {
        setConfig(global, buildConfig(null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveExtension(final JpsGlobal global, final Element element) {
        @Nullable final JpsMetaborgApplicationConfig config = this.extensionService.getGlobalConfiguration(global);
        if (config == null) return;
        XmlSerializer.serializeInto(config.getState(), element);
    }

    /**
     * Sets the global configuration.
     *
     * @param global The global element.
     * @param config The configuration.
     */
    private void setConfig(final JpsGlobal global, final JpsMetaborgApplicationConfig config) {
        this.extensionService.setGlobalConfiguration(global, config);
    }

    /**
     * Builds the configuration object.
     *
     * @param state The configuration state; or <code>null</code> to use the default.
     * @return The configuration object.
     */
    private JpsMetaborgApplicationConfig buildConfig(@Nullable final MetaborgApplicationConfigState state) {
        final JpsMetaborgApplicationConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        return config;
    }
}
