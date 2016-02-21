package com.acerete.vo.serializers;

import java.io.IOException;

import com.acerete.utils.DateUtils;
import com.acerete.vo.Game;
import com.acerete.vo.Player;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GameSerializer extends JsonSerializer<Game> {
	
	@Override
	public void serialize(Game game, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		writeGame(game, generator);
		generator.writeEndObject();
	}
	
	public static void writeGame(Game game, JsonGenerator generator) throws IOException, JsonProcessingException {
		generator.writeStringField(Game.KEY_ID, game.getId());
		generator.writeArrayFieldStart(Game.KEY_FIELD);
		FieldSerializer.writeField(game.getField(), generator);
		generator.writeEndArray();
		generator.writeArrayFieldStart(Game.KEY_PLAYERS);
		for (Player player : game.getPlayers()) {
			generator.writeStartObject();
			PlayerSerializer.writePlayer(player, generator);
			generator.writeEndObject();
		}
		generator.writeEndArray();
		generator.writeBooleanField(Game.KEY_IS_FINISHED, game.isFinished());
		generator.writeNumberField(Game.KEY_TURN_NUMBER, game.getTurnNumber());
		generator.writeStringField(Game.KEY_NEXT_TURN, game.getNextTurn());
		generator.writeStringField(Game.KEY_CREATED_AT, DateUtils.formatIso8601Date(game.getCreatedAt()));
	}
}
