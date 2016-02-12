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

// TODO: Rename to: MetaborgApplicationConfigDeserializer
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
        @Nullable final MetaborgApplicationConfigState state = XmlSerializer.deserialize(element, MetaborgApplicationConfigState.class);
        loadExtensionWithState(global, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtensionWithDefaultSettings(final JpsGlobal global) {
        loadExtensionWithState(global, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveExtension(final JpsGlobal jpsGlobal, final Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }

    private void loadExtensionWithState(
            final JpsGlobal global,
            @Nullable final MetaborgApplicationConfigState state) {
        final JpsMetaborgApplicationConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        this.extensionService.setConfiguration(global, config);
    }
}
