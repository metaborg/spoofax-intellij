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
import com.intellij.util.xmlb.*;
import org.jdom.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.serialization.*;
import org.metaborg.intellij.configuration.*;

import javax.annotation.*;

/**
 * Deserializes project-wide configuration in JPS.
 */
public final class MetaborgProjectConfigDeserializer extends JpsProjectExtensionSerializer {

    private final IJpsMetaborgProjectConfigFactory configFactory;
    private final IMetaborgConfigService extensionService;

    @Inject
    public MetaborgProjectConfigDeserializer(final IJpsMetaborgProjectConfigFactory configFactory,
                                             final IMetaborgConfigService extensionService) {
        super(IMetaborgProjectConfig.CONFIG_FILE, IMetaborgProjectConfig.CONFIG_NAME);
        this.configFactory = configFactory;
        this.extensionService = extensionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtension(final JpsProject project, final Element element) {
        @Nullable final MetaborgProjectConfigState state = XmlSerializer.deserialize(element,
                MetaborgProjectConfigState.class);
        setConfig(project, buildConfig(state));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtensionWithDefaultSettings(final JpsProject project) {
        setConfig(project, buildConfig(null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveExtension(final JpsProject project, final Element element) {
        @Nullable final JpsMetaborgProjectConfig config = this.extensionService.getProjectConfiguration(project);
        if (config == null) return;
        XmlSerializer.serializeInto(config.getState(), element);
    }

    /**
     * Sets the configuration of the project.
     *
     * @param project The project.
     * @param config The configuration.
     */
    private void setConfig(final JpsProject project, final JpsMetaborgProjectConfig config) {
        this.extensionService.setProjectConfiguration(project, config);
    }

    /**
     * Builds the configuration object.
     *
     * @param state The configuration state; or <code>null</code> to use the default.
     * @return The configuration object.
     */
    private JpsMetaborgProjectConfig buildConfig(@Nullable final MetaborgProjectConfigState state) {
        final JpsMetaborgProjectConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        return config;
    }

}
