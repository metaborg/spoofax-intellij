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
        @Nullable final MetaborgProjectConfigState state = XmlSerializer.deserialize(element, MetaborgProjectConfigState.class);
        loadExtensionWithState(project, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtensionWithDefaultSettings(final JpsProject project) {
        loadExtensionWithState(project, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveExtension(final JpsProject project, final Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }

    // TODO: Rename
    // TODO: Document
    private void loadExtensionWithState(
            final JpsProject project,
            @Nullable final MetaborgProjectConfigState state) {
        final JpsMetaborgProjectConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        this.extensionService.setConfiguration(project, config);
    }

}
