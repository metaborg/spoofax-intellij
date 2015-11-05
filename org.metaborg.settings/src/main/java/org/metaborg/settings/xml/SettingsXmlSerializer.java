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

package org.metaborg.settings.xml;

import com.google.common.base.Preconditions;
import org.metaborg.settings.ISettingKey;
import org.metaborg.settings.ISettingsFactory;
import org.metaborg.settings.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Serializes {@link Settings} objects.
 */
/* package private */ final class SettingsXmlSerializer implements IXmlSerializer {

    private final ISettingsFactory factory;

    /**
     * Initializes a new instance of the {@link SettingsXmlSerializer} class.
     *
     * @param factory The settings factory.
     */
    public SettingsXmlSerializer(final ISettingsFactory factory) {
        Preconditions.checkNotNull(factory);

        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node serialize(final Object value, final Document doc, final XmlSettingsFormat format) {
        Element element = doc.createElement("settings");

        Settings settings = (Settings) value;
        for (ISettingKey<?> key : settings.getAllLocalSettings()) {

            Element child = doc.createElement(key.name());

            child.appendChild(format.serialize(settings.getLocalSetting(key), doc, key.type()));
            element.appendChild(child);
        }

        return element;
    }
}
