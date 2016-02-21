package com.acerete.vo.serializers;

import java.io.IOException;

import com.acerete.vo.Mark;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MarkSerializer extends JsonSerializer<Mark> {

	@Override
	public void serialize(Mark mark, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		generator.writeStringField(Mark.KEY_PLAYER_ID, mark.getPlayerId());
		generator.writeNumberField(Mark.KEY_X, mark.getX());
		generator.writeNumberField(Mark.KEY_Y, mark.getY());
		generator.writeEndObject();
	}
}
