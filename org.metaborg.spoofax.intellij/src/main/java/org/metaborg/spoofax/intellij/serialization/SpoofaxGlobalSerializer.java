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
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;

import javax.annotation.Nullable;

/**
 * Deserializes application-wide configuration in JPS.
 */
public final class SpoofaxGlobalSerializer extends JpsGlobalExtensionSerializer {

    public static final String NAME = "SpoofaxGlobalService";
    public static final String CONFIG_FILE = "Spoofax.xml";

    public SpoofaxGlobalSerializer() {
        super(CONFIG_FILE, NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loadExtension(final JpsGlobal global, final Element element) {
        @Nullable final SpoofaxGlobalState state = XmlSerializer.deserialize(element, SpoofaxGlobalState.class);
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
            @Nullable final SpoofaxGlobalState state) {
        final SpoofaxGlobalConfig config = new SpoofaxGlobalConfig();
        if (state != null)
            config.loadState(state);
        SpoofaxExtensionService.getInstance().setConfiguration(global, config);
    }
}
