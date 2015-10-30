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

package org.metaborg.core.project.settings.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.core.project.settings.Settings;
import org.metaborg.core.project.settings.ISettingsFactory;
import org.metaborg.core.project.settings.SettingDescriptor;
import org.metaborg.core.project.settings.SettingKey;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

/**
 * Deserializes {@link Settings} objects.
 */
/* package private */ final class SettingsJacksonDeserializer extends StdDeserializer<Settings> {

    @NotNull
    private final ISettingsFactory factory;
    @InjectLogger
    private Logger logger;

    /**
     * Initializes a new instance of the {@link SettingsJacksonDeserializer} class.
     *
     * @param factory The settings factory.
     */
    public SettingsJacksonDeserializer(@NotNull final ISettingsFactory factory) {
        super(Settings.class);
        Preconditions.checkNotNull(factory);

        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Settings deserialize(@NotNull final JsonParser parser, @NotNull final DeserializationContext context) throws IOException {
        Preconditions.checkNotNull(parser);
        Preconditions.checkNotNull(context);

        final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
        final Settings parent = (Settings)context.findInjectableValue("parent", null, null);
        final JsonNode root = mapper.readTree(parser);
        final Map<SettingKey<?>, Object> settings = new LinkedHashMap<>();

        Set<SettingDescriptor> descriptors = this.factory.settingDescriptors();
        Iterator<String> fieldNames = root.fieldNames();
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            JsonNode node = root.get(field);
            assert node != null;
            SettingDescriptor descriptor = findDescriptor(field, descriptors);
            if (descriptor != null) {
                // Known field.
                SettingKey<?> key = descriptor.key();
                Object value = mapper.readValue(node.traverse(mapper), key.type());
                settings.put(key, value);
            } else {
                // Unknown field, conserve.
                Object value = mapper.readValue(node.traverse(mapper), Object.class);
                settings.put(new SettingKey<>(field, Object.class), value);
            }
        }

        return this.factory.create(settings, parent);
    }

    /**
     * Gets a descriptor with the specified name.
     *
     * @param name The name of the descriptor; or <code>null</code>.
     * @param descriptors The descriptors.
     * @return The descriptor with the specified name; or <code>null</code>.
     */
    @Nullable
    private SettingDescriptor findDescriptor(@Nullable String name, @NotNull Set<SettingDescriptor> descriptors) {
        if (name == null)
            return null;

        for (SettingDescriptor descriptor : descriptors) {
            if (descriptor.key().name().equals(name))
                return descriptor;
        }
        return null;
    }
}
