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

package org.metaborg.settings.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.settings.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Settings serializer/deserializer using FasterXML's Jackson library.
 */
public class JacksonSettingsFormat extends SettingsFormat {

    @NotNull private final ISettingsFactory settingsFactory;
    @NotNull private final SimpleModule module;
    @NotNull private final ObjectMapper mapper;

    public JacksonSettingsFormat(@NotNull final ISettingsFactory settingsFactory, @NotNull final
                                                JsonFactory jsonFactory) {
        Preconditions.checkNotNull(settingsFactory);
        Preconditions.checkNotNull(jsonFactory);

        this.settingsFactory = settingsFactory;
        this.module = new SimpleModule();
        this.module.addDeserializer(Settings.class, new SettingsJacksonDeserializer(this.settingsFactory));
        this.module.addSerializer(Settings.class, new SettingsJacksonSerializer(this.settingsFactory));
        // TODO: Move these (de)serializers to annotations.
        this.module.addDeserializer(LanguageIdentifier.class, new LanguageIdentifierJacksonDeserializer());
        this.module.addSerializer(LanguageIdentifier.class, new LanguageIdentifierJacksonSerializer());

        this.mapper = new ObjectMapper(jsonFactory);
        this.mapper.registerModule(this.module);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final Settings readFromStream(@NotNull final InputStream input, @Nullable Settings parent) throws IOException {
        Preconditions.checkNotNull(input);

        InjectableValues values = new InjectableValues.Std().addValue("parent", parent);
        return this.mapper.reader(values).forType(Settings.class).readValue(input);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void writeToStream(@NotNull final OutputStream output, @NotNull final Settings settings) throws IOException {
        Preconditions.checkNotNull(output);
        Preconditions.checkNotNull(settings);

        this.mapper.writeValue(output, settings);
    }
}
