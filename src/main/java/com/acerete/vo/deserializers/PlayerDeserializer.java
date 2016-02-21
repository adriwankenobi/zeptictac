package com.acerete.vo.deserializers;

import java.io.IOException;

import com.acerete.vo.Player;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PlayerDeserializer extends JsonDeserializer<Player> {

	@Override
	public Player deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		JsonNode root = parser.getCodec().readTree(parser);
		return parsePlayer(root);
	}
	
	public static Player parsePlayer(JsonNode node) {
		String playerId = node.get(Player.KEY_ID).asText();
		String nickname = node.get(Player.KEY_NICKNAME).asText();
		char symbol = node.get(Player.KEY_SYMBOL).asText().charAt(0);
		return new Player(playerId, nickname, symbol);
	}

}
