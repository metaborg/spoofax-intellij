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
import org.metaborg.settings.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Deserializes {@link Settings} objects.
 */
/* package private */ final class SettingsXmlDeserializer implements IXmlDeserializer {

    private final ISettingsFactory factory;

    /**
     * Initializes a new instance of the {@link SettingsXmlDeserializer} class.
     *
     * @param factory The settings factory.
     */
    public SettingsXmlDeserializer(final ISettingsFactory factory) {
        Preconditions.checkNotNull(factory);

        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserialize(final Node node, final XmlSettingsFormat format) {

        final Map<ISettingKey<?>, Object> settings = new LinkedHashMap<>();

        Set<SettingDescriptor> descriptors = this.factory.settingDescriptors();
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = node.getChildNodes().item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            Element child = (Element) childNode;
            String fieldName = child.getTagName();
            ISettingKey<?> key = SettingsFormatUtils.getKeyOrDefault(fieldName, descriptors);
            Object value = format.deserialize(child, key.type());
            settings.put(key, value);
        }

        // TODO: Set parent!
        return this.factory.create(settings, null);
    }
}
