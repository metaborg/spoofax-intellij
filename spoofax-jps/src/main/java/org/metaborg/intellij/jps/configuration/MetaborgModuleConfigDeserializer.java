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
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.intellij.configuration.IMetaborgModuleConfig;
import org.metaborg.intellij.configuration.MetaborgModuleConfigState;
import org.metaborg.intellij.jps.JpsMetaborgModuleType;
import org.metaborg.intellij.projects.MetaborgModuleConstants;

import javax.annotation.Nullable;

/**
 * Deserializes module-specific configuration in JPS.
 */
public final class MetaborgModuleConfigDeserializer extends JpsModulePropertiesSerializer<JpsMetaborgModuleConfig> {

    private final IJpsMetaborgModuleConfigFactory configFactory;

    /**
     * Initializes a new instance of the {@link MetaborgModuleConfigDeserializer} class.
     */
    @Inject
    public MetaborgModuleConfigDeserializer(final JpsMetaborgModuleType moduleType,
                                            final IJpsMetaborgModuleConfigFactory configFactory) {
        super(moduleType, MetaborgModuleConstants.ModuleID, IMetaborgModuleConfig.CONFIG_NAME);
        this.configFactory = configFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JpsMetaborgModuleConfig loadProperties(@Nullable final Element element) {
        @Nullable final MetaborgModuleConfigState state;
        if (element != null) {
            state = XmlSerializer.deserialize(element, MetaborgModuleConfigState.class);
        } else {
            state = null;
        }
        return buildConfig(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveProperties(final JpsMetaborgModuleConfig config, final Element element) {
        XmlSerializer.serializeInto(config.getState(), element);
    }

    /**
     * Builds the configuration object.
     *
     * @param state The configuration state; or <code>null</code> to use the default.
     * @return The configuration object.
     */
    private JpsMetaborgModuleConfig buildConfig(@Nullable final MetaborgModuleConfigState state) {
        final JpsMetaborgModuleConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        return config;
    }

}
