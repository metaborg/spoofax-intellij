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

package org.metaborg.intellij.jps.configuration;

import com.google.inject.*;
import com.intellij.util.xmlb.*;
import org.jdom.*;
import org.jetbrains.annotations.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.module.*;
import org.jetbrains.jps.model.serialization.facet.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.facets.*;

import javax.annotation.*;
import javax.annotation.Nullable;

/**
 * Deserializes facet-specific configuration in JPS.
 */
public final class MetaborgFacetConfigDeserializer extends JpsFacetConfigurationSerializer<JpsMetaborgModuleFacetConfig> {

    private final IJpsMetaborgModuleFacetConfigFactory configFactory;

    /**
     * Initializes a new instance of the {@link MetaborgFacetConfigDeserializer} class.
     */
    @Inject
    public MetaborgFacetConfigDeserializer(final IJpsMetaborgModuleFacetConfigFactory configFactory) {
        super(JpsMetaborgModuleFacetConfig.ROLE, MetaborgFacetType.ID, MetaborgFacetType.NAME);

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
