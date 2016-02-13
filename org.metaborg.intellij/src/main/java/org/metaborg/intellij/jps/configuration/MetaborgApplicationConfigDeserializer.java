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
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.serialization.*;
import org.metaborg.intellij.configuration.*;

import javax.annotation.*;

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
        final JpsMetaborgApplicationConfig config = this.extensionService.getConfiguration(global);
        XmlSerializer.serializeInto(config.getState(), element);
    }

    /**
     * Sets the global configuration.
     *
     * @param global The global element.
     * @param config The configuration.
     */
    private void setConfig(final JpsGlobal global, final JpsMetaborgApplicationConfig config) {
        this.extensionService.setConfiguration(global, config);
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
