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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Preconditions;
import org.metaborg.settings.ISettingKey;
import org.metaborg.settings.ISettingsFactory;
import org.metaborg.settings.Settings;

import java.io.IOException;

/**
 * Serializes {@link Settings} objects.
 */
/* package private */ final class SettingsJacksonSerializer extends StdSerializer<Settings> {

    private final ISettingsFactory factory;

    /**
     * Initializes a new instance of the {@link SettingsJacksonSerializer} class.
     *
     * @param factory The settings factory.
     */
    public SettingsJacksonSerializer(final ISettingsFactory factory) {
        super(Settings.class);
        Preconditions.checkNotNull(factory);

        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(
            final Settings settings,
            final JsonGenerator generator,
            final SerializerProvider provider) throws
            IOException {
        Preconditions.checkNotNull(settings);
        Preconditions.checkNotNull(generator);
        Preconditions.checkNotNull(provider);

        generator.writeStartObject();
        for (ISettingKey<?> key : settings.getAllLocalSettings()) {
            generator.writeFieldName(key.name());
            generator.writeObject(settings.getLocalSetting(key));
        }
        generator.writeEndObject();
    }
}
