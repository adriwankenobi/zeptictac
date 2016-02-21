package com.acerete.vo.deserializers;

import java.io.IOException;
import java.util.Iterator;

import com.acerete.vo.Field;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class FieldDeserializer extends JsonDeserializer<Field> {

	@Override
	public Field deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		JsonNode root = parser.getCodec().readTree(parser);
		return parseField(root);
	}

	public static Field parseField(JsonNode node) throws IOException, JsonProcessingException {
		Iterator<JsonNode> itWidth = node.iterator();
		Field field = new Field();
		int i = 0;
		while (itWidth.hasNext()) {
			JsonNode widthNode = itWidth.next();
			Iterator<JsonNode> itHeight = widthNode.iterator();
			int j = 0;
			while (itHeight.hasNext()) {
				JsonNode cellNode = itHeight.next();
				char symbol = cellNode.asText().charAt(0);
				field.set(i, j, symbol);
				j++;
			}
			i++;
		}
		return field;
	}
}
