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

package org.metaborg.settings.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Preconditions;
import org.metaborg.settings.Settings;
import org.metaborg.settings.ISettingsFactory;
import org.metaborg.settings.SettingDescriptor;
import org.metaborg.settings.SettingKey;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

/**
 * Deserializes {@link Settings} objects.
 */
/* package private */ final class SettingsJacksonDeserializer extends StdDeserializer<Settings> {

    private final ISettingsFactory factory;

    /**
     * Initializes a new instance of the {@link SettingsJacksonDeserializer} class.
     *
     * @param factory The settings factory.
     */
    public SettingsJacksonDeserializer(final ISettingsFactory factory) {
        super(Settings.class);
        Preconditions.checkNotNull(factory);

        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Settings deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        Preconditions.checkNotNull(parser);
        Preconditions.checkNotNull(context);

        final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
        final Settings parent = (Settings)context.findInjectableValue("parent", null, null);
        final JsonNode root = mapper.readTree(parser);
        final Map<SettingKey, Object> settings = new LinkedHashMap<>();
        final TypeFactory typeFactory = mapper.getTypeFactory();

        Set<SettingDescriptor> descriptors = this.factory.settingDescriptors();
        Iterator<String> fieldNames = root.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode node = root.get(fieldName);
            assert node != null;
            SettingKey key = getKeyOrDefault(fieldName, descriptors);
            Object value = mapper.readValue(node.traverse(mapper), typeFactory.constructType(key.type()));
            settings.put(key, value);
        }

        return this.factory.create(settings, parent);
    }

    /**
     * Gets the setting key for the specified field.
     *
     * @param fieldName The field name.
     * @param descriptors The set of descriptors.
     * @return The setting key.
     */
    private SettingKey getKeyOrDefault(final String fieldName, final Set<SettingDescriptor> descriptors) {
        SettingDescriptor descriptor = findDescriptor(fieldName, descriptors);
        if (descriptor != null) {
            // Known field.
            return descriptor.key();
        } else {
            // Unknown field, conserve.
            return new SettingKey(fieldName, Object.class);
        }
    }

    /**
     * Gets a descriptor with the specified name.
     *
     * @param name The name of the descriptor; or <code>null</code>.
     * @param descriptors The descriptors.
     * @return The descriptor with the specified name; or <code>null</code>.
     */
    @Nullable
    private SettingDescriptor findDescriptor(@Nullable String name, Set<SettingDescriptor> descriptors) {
        if (name == null)
            return null;

        for (SettingDescriptor descriptor : descriptors) {
            if (descriptor.key().name().equals(name))
                return descriptor;
        }
        return null;
    }
}
