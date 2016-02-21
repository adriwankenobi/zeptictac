package com.acerete.vo.deserializers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.acerete.utils.DateUtils;
import com.acerete.vo.Field;
import com.acerete.vo.Game;
import com.acerete.vo.Player;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GameDeserializer extends JsonDeserializer<Game> {

	@Override
	public Game deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		JsonNode root = parser.getCodec().readTree(parser);
		return parseGame(root);
	}
	
	public static Game parseGame(JsonNode node) throws IOException, JsonProcessingException {
		String gameId = node.get(Game.KEY_ID).asText();
		JsonNode fieldNode = node.get(Game.KEY_FIELD);
		Field field = FieldDeserializer.parseField(fieldNode);
		JsonNode playersNode = node.get(Game.KEY_PLAYERS);
		Iterator<JsonNode> itPlayers = playersNode.iterator();
		LinkedHashMap<String, Player> players = new LinkedHashMap<String, Player>();
		while (itPlayers.hasNext()) {
			JsonNode playerNode = itPlayers.next();
			Player player = PlayerDeserializer.parsePlayer(playerNode);
			players.put(player.getId(), player);
		}
		int turnNumber = node.get(Game.KEY_TURN_NUMBER).asInt();
		String nextTurn = node.get(Game.KEY_NEXT_TURN).asText();
		boolean isFinished = node.get(Game.KEY_IS_FINISHED).asBoolean();
		try {
			Date createdAt = DateUtils.parseIso8601Date(node.get(Game.KEY_CREATED_AT).asText());
			return new Game(gameId, field, players, isFinished, turnNumber, nextTurn, createdAt);
		} catch (ParseException e) {
			throw new IOException(e.getMessage(), e.getCause());
		}
	}
}
