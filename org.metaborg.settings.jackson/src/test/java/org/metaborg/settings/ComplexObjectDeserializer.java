package org.metaborg.settings;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public final class ComplexObjectDeserializer extends StdDeserializer<ComplexObject> {

    public ComplexObjectDeserializer() {
        super(ComplexObject.class);
    }

    @Override
    public ComplexObject deserialize(
            final JsonParser parser, final DeserializationContext context) throws IOException {
        final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
        final JsonNode root = mapper.readTree(parser);

        String name = root.get("name").asText();
        int value = root.get("value").asInt();
        return new ComplexObject(name, value);
    }

}
