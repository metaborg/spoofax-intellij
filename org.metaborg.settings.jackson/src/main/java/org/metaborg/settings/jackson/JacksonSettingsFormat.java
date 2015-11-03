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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Preconditions;
import org.metaborg.settings.ISettingsFactory;
import org.metaborg.settings.Settings;
import org.metaborg.settings.ISettingsFormat;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO: Move to Metaborg Core

/**
 * Settings serializer/deserializer using FasterXML's Jackson library.
 */
public class JacksonSettingsFormat implements ISettingsFormat {

    private final ISettingsFactory settingsFactory;
    private final SimpleModule module;
    private final ObjectMapper mapper;

    /**
     * Initializes a new instance of the {@link JacksonSettingsFormat} class.
     *
     * @param settingsFactory The settings factory.
     * @param jsonFactory The JSON factory.
     */
    public JacksonSettingsFormat(final ISettingsFactory settingsFactory, final
                                                JsonFactory jsonFactory) {
        Preconditions.checkNotNull(settingsFactory);
        Preconditions.checkNotNull(jsonFactory);

        this.settingsFactory = settingsFactory;
        this.module = new SimpleModule();
        this.module.addDeserializer(Settings.class, new SettingsJacksonDeserializer(this.settingsFactory));
        this.module.addSerializer(Settings.class, new SettingsJacksonSerializer(this.settingsFactory));

        this.mapper = new ObjectMapper(jsonFactory);
        this.mapper.registerModule(this.module);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Settings read(final InputStream input, @Nullable Settings parent) throws IOException {
        Preconditions.checkNotNull(input);

        InjectableValues values = new InjectableValues.Std().addValue("parent", parent);
        return this.mapper.reader(values).forType(Settings.class).readValue(input);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void write(final OutputStream output, final Settings settings) throws IOException {
        Preconditions.checkNotNull(output);
        Preconditions.checkNotNull(settings);

        this.mapper.writeValue(output, settings);
    }

    /**
     * Adds a serializer/deserializer pair for a type.
     *
     * @param serializer The serializer; or <code>null</code>.
     * @param deserializer The deserializer; or <code>null</code>.
     * @param <T> The type.
     */
    public final <T> void addSerializerDeserializer(@Nullable final JsonSerializer<T> serializer, @Nullable final JsonDeserializer<? extends T> deserializer) {
        addSerializerDeserializer(serializer, deserializer, null);
    }

    /**
     * Adds a serializer/deserializer pair for a specific type.
     *
     * @param serializer The serializer; or <code>null</code>.
     * @param deserializer The deserializer; or <code>null</code>.
     * @param type The type; or <code>null</code>.
     * @param <T> The type.
     */
    public final <T> void addSerializerDeserializer(@Nullable final JsonSerializer<T> serializer, @Nullable final JsonDeserializer<? extends T> deserializer, @Nullable Class<T> type) {
        if (type == null)
            type = serializer.handledType();
        if (type == null || type == Object.class)
            throw new IllegalArgumentException("No (valid) type has been specified by either the caller or the serializer.");
        if (serializer != null)
            this.module.addSerializer(type, serializer);
        if (deserializer != null)
            this.module.addDeserializer(type, deserializer);
    }

}
