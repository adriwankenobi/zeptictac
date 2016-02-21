package com.acerete.vo.serializers;

import java.io.IOException;

import com.acerete.vo.Player;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PlayerSerializer extends JsonSerializer<Player> {

	@Override
	public void serialize(Player player, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		writePlayer(player, generator);
		generator.writeEndObject();
	}
	
	public static void writePlayer(Player player, JsonGenerator generator) throws IOException, JsonProcessingException {
		generator.writeStringField(Player.KEY_ID, player.getId());
		generator.writeStringField(Player.KEY_NICKNAME, player.getNickname());
		generator.writeStringField(Player.KEY_SYMBOL, String.valueOf(player.getSymbol()));
	}

}
