/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.serialization;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;

import javax.annotation.Nullable;

public final class SpoofaxProjectSerializer extends JpsProjectExtensionSerializer {

    @NotNull
    public static final String NAME = "SpoofaxProjectService";
    @NotNull
    public static final String CONFIG_FILE = "SpoofaxProject.xml";

    public SpoofaxProjectSerializer() {
        super(CONFIG_FILE, NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtension(final JpsProject project, final Element element) {
        final SpoofaxProjectState state = XmlSerializer.deserialize(element, SpoofaxProjectState.class);
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
        SpoofaxExtensionService.getInstance().setConfiguration(project, config);
    }

}
