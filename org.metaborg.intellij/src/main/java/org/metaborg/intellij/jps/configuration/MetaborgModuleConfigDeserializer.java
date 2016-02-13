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
import org.jetbrains.jps.model.module.*;
import org.jetbrains.jps.model.serialization.module.*;
import org.metaborg.intellij.configuration.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.jps.*;

import javax.annotation.*;

/**
 * Deserializes module-specific configuration in JPS.
 */
public final class MetaborgModuleConfigDeserializer extends JpsModulePropertiesSerializer<JpsMetaborgModuleConfig> {

    private final IJpsMetaborgModuleConfigFactory configFactory;

    @Inject
    public MetaborgModuleConfigDeserializer(final IJpsMetaborgModuleConfigFactory configFactory) {
        super(JpsMetaborgModuleType.INSTANCE, MetaborgModuleType.ID, IMetaborgModuleConfig.CONFIG_NAME);
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
            state = new MetaborgModuleConfigState();
        }
        // TODO: Abstract
        final JpsMetaborgModuleConfig config = this.configFactory.create();
        if (state != null)
            config.loadState(state);
        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveProperties(final JpsMetaborgModuleConfig config, final Element element) {
        XmlSerializer.serializeInto(config.getState(), element);
    }

}
