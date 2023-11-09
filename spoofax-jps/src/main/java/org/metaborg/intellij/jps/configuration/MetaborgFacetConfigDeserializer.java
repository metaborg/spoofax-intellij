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
import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer;
import org.metaborg.intellij.configuration.MetaborgModuleFacetConfigState;
import org.metaborg.intellij.facets.MetaborgFacetConstants;

import jakarta.annotation.Nullable;

/**
 * Deserializes facet-specific configuration in JPS.
 */
public final class MetaborgFacetConfigDeserializer extends JpsFacetConfigurationSerializer<JpsMetaborgModuleFacetConfig> {

    private final IJpsMetaborgModuleFacetConfigFactory configFactory;

    /**
     * Initializes a new instance of the {@link MetaborgFacetConfigDeserializer} class.
     */
    @jakarta.inject.Inject @javax.inject.Inject
    public MetaborgFacetConfigDeserializer(final IJpsMetaborgModuleFacetConfigFactory configFactory) {
        super(JpsMetaborgModuleFacetConfig.ROLE, MetaborgFacetConstants.ID, MetaborgFacetConstants.NAME);

        this.configFactory = configFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpsMetaborgModuleFacetConfig loadExtension(final Element element,
                                                         final String name,
                                                         final JpsElement parent,
                                                         final JpsModule module) {
        @Nullable final MetaborgModuleFacetConfigState state
                = XmlSerializer.deserialize(element, MetaborgModuleFacetConfigState.class);
        return buildConfig(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveExtension(final JpsMetaborgModuleFacetConfig config,
                                 final Element element,
                                 final JpsModule module) {
        XmlSerializer.serializeInto(config.getState(), element);
    }

    /**
     * Builds the configuration object.
     *
     * @param state The configuration state; or <code>null</code> to use the default.
     * @return The configuration object.
     */
    private JpsMetaborgModuleFacetConfig buildConfig(@Nullable final MetaborgModuleFacetConfigState state) {
        final JpsMetaborgModuleFacetConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        return config;
    }
}
