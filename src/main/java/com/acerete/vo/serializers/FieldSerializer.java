package com.acerete.vo.serializers;

import java.io.IOException;

import com.acerete.vo.Field;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class FieldSerializer extends JsonSerializer<Field> {

	@Override
	public void serialize(Field field, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartArray();
		writeField(field, generator);
		generator.writeEndArray();
	}
	
	public static void writeField(Field field, JsonGenerator generator) throws IOException, JsonProcessingException {
		for (int i = 0; i < Field.SIZE; i++) {
			generator.writeStartArray();
			for (int j = 0; j < Field.SIZE; j++) {
				generator.writeObject(field.get(i, j));
			}
			generator.writeEndArray();
		}
	}
}
