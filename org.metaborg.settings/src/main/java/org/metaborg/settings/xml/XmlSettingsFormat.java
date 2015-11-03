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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings serializer/deserializer using Java's built-in XML support.
 */
public class XmlSettingsFormat implements ISettingsFormat {

    private final ISettingsFactory settingsFactory;
    private final Map<Type, SerializerPair> serializers = new HashMap<>();

    /**
     * Initializes a new instance of the {@link XmlSettingsFormat} class.
     *
     * @param settingsFactory The settings factory.
     */
    public XmlSettingsFormat(final ISettingsFactory settingsFactory) {
        Preconditions.checkNotNull(settingsFactory);

        this.settingsFactory = settingsFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Settings read(final InputStream input, @Nullable Settings parent) throws IOException {
        Preconditions.checkNotNull(input);

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            Element root = doc.getDocumentElement();
            root.normalize();

            return (Settings)deserialize(root, Settings.class);

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void write(final OutputStream output, final Settings settings) throws IOException {
        Preconditions.checkNotNull(output);
        Preconditions.checkNotNull(settings);

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            doc.appendChild(serialize(settings, doc, Settings.class));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes an object.
     *
     * @param value The value.
     * @param doc The document.
     * @param type The type.
     * @return The object.
     */
    public <T> Node serialize(final T value, final Document doc, Type type) {
        IXmlSerializer serializer = getSerializer(type);
        assert serializer != null;
        return serializer.serialize(value, doc, this);
    }

    /**
     * Deserializes an object.
     *
     * @param node The node.
     * @param type The type.
     * @return The object.
     */
    public <T> T deserialize(final Node node, Type type) {
        IXmlDeserializer deserializer = getDeserializer(type);
        assert deserializer != null;
        return (T)deserializer.deserialize(node, this);
    }

    /**
     * Adds a serializer/deserializer pair for a type.
     *
     * @param serializer The serializer; or <code>null</code>.
     * @param deserializer The deserializer; or <code>null</code>.
     * @param <T> The type.
     */
    public final <T> void addSerializerDeserializer(@Nullable final IXmlSerializer serializer, @Nullable final IXmlDeserializer deserializer) {
        addSerializerDeserializer(serializer, deserializer, null);
    }

    /**
     * Adds a serializer/deserializer pair for a specific type.
     *
     * @param serializer The serializer; or <code>null</code>.
     * @param deserializer The deserializer; or <code>null</code>.
     * @param type The type.
     * @param <T> The type.
     */
    public final <T> void addSerializerDeserializer(@Nullable final IXmlSerializer serializer, @Nullable final IXmlDeserializer deserializer, Class<T> type) {
        Preconditions.checkNotNull(type);
        this.serializers.put(type, new SerializerPair(serializer, deserializer));
    }

    /**
     * Gets the serializer for the type.
     *
     * @param type The type.
     * @return The deserializer; or <code>null</code>.
     */
    @Nullable
    private IXmlSerializer getSerializer(Type type) {
        SerializerPair pair = this.serializers.get(type);
        if (pair == null)
            return null;
        return pair.serializer;
    }

    /**
     * Gets the deserializer for the type.
     *
     * @param type The type.
     * @return The deserializer; or <code>null</code>.
     */
    @Nullable
    private IXmlDeserializer getDeserializer(Type type) {
        SerializerPair pair = this.serializers.get(type);
        if (pair == null)
            return null;
        return pair.deserializer;
    }

    /**
     * A serializer/deserializer pair.
     */
    private static class SerializerPair {
        /**
         * The serializer; or <code>null</code>.
         */
        @Nullable
        public final IXmlSerializer serializer;
        /**
         * The deserializer; or <code>null</code>.
         */
        @Nullable
        public final IXmlDeserializer deserializer;

        /**
         * Initializes a new instance of the {@link org.metaborg.settings.xml.XmlSettingsFormat.SerializerPair} class.
         * @param serializer The serializer; or <code>null</code>.
         * @param deserializer The deserializer; or <code>null</code>.
         */
        public SerializerPair(@Nullable final IXmlSerializer serializer, @Nullable final IXmlDeserializer deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }
    }
}
