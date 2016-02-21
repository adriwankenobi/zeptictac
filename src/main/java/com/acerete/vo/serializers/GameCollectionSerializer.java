package com.acerete.vo.serializers;

import java.io.IOException;

import com.acerete.vo.Game;
import com.acerete.vo.GameCollection;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GameCollectionSerializer extends JsonSerializer<GameCollection> {

	@Override
	public void serialize(GameCollection collection, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		generator.writeArrayFieldStart(GameCollection.KEY_DATA);
		for (Game game : collection.getData()) {
			generator.writeStartObject();
			GameSerializer.writeGame(game, generator);
			generator.writeEndObject();
		}
	    generator.writeEndArray();
	    if (collection.getPrevious() != null) {
	    	generator.writeStringField(GameCollection.KEY_PREVIOUS, collection.getPrevious());
	    }
	    if (collection.getNext() != null) {
	    	generator.writeStringField(GameCollection.KEY_NEXT, collection.getNext());
	    }	
	    generator.writeEndObject();
	}

}
