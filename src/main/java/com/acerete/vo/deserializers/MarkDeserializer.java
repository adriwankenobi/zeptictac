package com.acerete.vo.deserializers;

import java.io.IOException;

import com.acerete.vo.Mark;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class MarkDeserializer extends JsonDeserializer<Mark> {

	@Override
	public Mark deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		JsonNode root = parser.getCodec().readTree(parser);
		String playerId = root.get(Mark.KEY_PLAYER_ID).asText();
		int x = root.get(Mark.KEY_X).asInt();
		int y = root.get(Mark.KEY_Y).asInt();
		return new Mark(playerId, x, y);
	}
}
