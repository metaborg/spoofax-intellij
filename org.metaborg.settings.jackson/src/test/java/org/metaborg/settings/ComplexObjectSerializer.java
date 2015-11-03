package org.metaborg.settings;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public final class ComplexObjectSerializer extends StdSerializer<ComplexObject> {

    public ComplexObjectSerializer() {
        super(ComplexObject.class);
    }

    @Override
    public void serialize(
            final ComplexObject value,
            final JsonGenerator generator,
            final SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("name", value.name);
        generator.writeNumberField("value", value.value);
        generator.writeEndObject();
    }
}
