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

package org.metaborg.intellij.jps.serialization;

import com.google.inject.*;
import com.intellij.util.xmlb.*;
import org.jdom.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.serialization.*;

import javax.annotation.*;

/**
 * Deserializes project-wide configuration in JPS.
 */
public final class SpoofaxProjectSerializer extends JpsProjectExtensionSerializer {

    public static final String NAME = "SpoofaxProjectService";
    public static final String CONFIG_FILE = "SpoofaxProject.xml";

    private final SpoofaxExtensionService extensionService;

    @Inject
    public SpoofaxProjectSerializer(final SpoofaxExtensionService extensionService) {
        super(CONFIG_FILE, NAME);
        this.extensionService = extensionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtension(final JpsProject project, final Element element) {
        @Nullable final SpoofaxProjectState state = XmlSerializer.deserialize(element, SpoofaxProjectState.class);
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

    private void loadExtensionWithState(
            final JpsProject project,
            @Nullable final SpoofaxProjectState state) {
        final SpoofaxProjectConfig config = new SpoofaxProjectConfig();
        if (state != null)
            config.loadState(state);
        this.extensionService.setConfiguration(project, config);
    }

}
