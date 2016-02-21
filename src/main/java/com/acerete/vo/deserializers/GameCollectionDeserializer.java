package com.acerete.vo.deserializers;

import java.io.IOException;
import java.util.Iterator;

import com.acerete.vo.Game;
import com.acerete.vo.GameCollection;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class GameCollectionDeserializer extends JsonDeserializer<GameCollection> {

	@Override
	public GameCollection deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		JsonNode root = parser.getCodec().readTree(parser);
		JsonNode dataNode = root.get(GameCollection.KEY_DATA);
		Iterator<JsonNode> it = dataNode.iterator();
		GameCollection collection = new GameCollection();
		while (it.hasNext()) {
			JsonNode node = it.next();
			Game game = GameDeserializer.parseGame(node);
			collection.addGame(game);
		}
		JsonNode previousNode = root.get(GameCollection.KEY_PREVIOUS);
		if (previousNode != null) {
			collection.setPrevious(previousNode.asText());
		}
		JsonNode nextNode = root.get(GameCollection.KEY_NEXT);
		if (nextNode != null) {
			collection.setNext(nextNode.asText());
		}
		return collection;
	}
}
