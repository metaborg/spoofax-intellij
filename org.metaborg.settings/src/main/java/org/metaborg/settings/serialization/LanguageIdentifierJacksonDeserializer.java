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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.LanguageIdentifier;

import java.io.IOException;

/**
 * Deserializer for the {@link LanguageIdentifier} class.
 */
/* package private */ final class LanguageIdentifierJacksonDeserializer extends StdDeserializer<LanguageIdentifier> {

    public LanguageIdentifierJacksonDeserializer() {
        super(LanguageIdentifier.class);
    }

    @Override
    public LanguageIdentifier deserialize(
            @NotNull final JsonParser parser, @NotNull final DeserializationContext context) throws IOException {
        Preconditions.checkNotNull(parser);
        Preconditions.checkNotNull(context);

        final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
        final JsonNode root = mapper.readTree(parser);

        return LanguageIdentifier.parse(root.asText());
    }
}
