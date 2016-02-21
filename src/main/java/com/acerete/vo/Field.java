package com.acerete.vo;

import java.util.Arrays;
import java.util.List;

import com.acerete.config.ZeptictacConfig;
import com.acerete.exceptions.IndexOutOfFieldException;
import com.acerete.vo.deserializers.FieldDeserializer;
import com.acerete.vo.serializers.FieldSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = FieldSerializer.class)
@JsonDeserialize(using = FieldDeserializer.class)
public class Field {

	public static final int MIN_DEFAULT_SIZE = 3;
	public static final int SIZE = ZeptictacConfig.getFieldSize();
	
	public static final char DEFAULT_EMPTY_SYMBOL = '-';
	public static final char EMPTY_SYMBOL = ZeptictacConfig.getEmptySymbol();
	
	public static final List<Character> DEFAULT_PLAYER_SYMBOLS = Arrays.asList('X', 'O');
	public static final List<Character> PLAYER_SYMBOLS = ZeptictacConfig.getPlayerSymbols();
	
	public static final String INDEX_ERROR_MESSAGE = "Max SIZE: " + SIZE;
	
	private Character[][] field;

	public Field(Character[][] field) {
		this.field = field;
	}
	
	public Field() {
		this.field = new Character[SIZE][SIZE];
	}

	public char get(int i, int j) throws IndexOutOfFieldException {
		validate(i, j);
		Character symbol = field[i][j];
		return symbol == null ? EMPTY_SYMBOL : symbol;
	}
	
	public void set(int i, int j, char symbol) throws IndexOutOfFieldException {
		validate(i, j);
		field[i][j] = symbol;
	}
	
	private void validate(int i, int j) throws IndexOutOfFieldException {
		if (i < 0 || i >= SIZE || j < 0 || j >= SIZE) {
			throw new IndexOutOfFieldException(INDEX_ERROR_MESSAGE);
		}
	}

	public boolean matchesWidth(char symbol, int x) {
		for (int i = 0; i < Field.SIZE; i++) {
			if (get(x, i) != symbol) {
				return false;
			}
		}
		return true;
	}
	
	public boolean matchesHeight(char symbol, int x) {
		for (int i = 0; i < Field.SIZE; i++) {
			if (get(i, x) != symbol) {
				return false;
			}
		}
		return true;
	}
	
	public boolean matchesFirstDiagonal(char symbol, int x, int y) {
		if (x != y) {
			return false;
		}
		for (int i = 0; i < Field.SIZE; i++) {
			if (get(i, i) != symbol) {
				return false;
			}
		}
		return true;
	}
	
	public boolean matchesSecondDiagonal(char symbol, int x, int y) {
		if (x != Field.SIZE - y - 1) {
			return false;
		}
		for (int i = 0; i < Field.SIZE; i++) {
			if (get(i, Field.SIZE - i - 1) != symbol) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isFull() {
		for (int i = 0; i < Field.SIZE; i++) {
			for (int j = 0; j < Field.SIZE; j++) {
				if (get(i, j) == Field.EMPTY_SYMBOL) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static Field copy(Field other) {
		if (other != null) {
			Character[][] field = new Character[SIZE][SIZE];
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					field[i][j] = other.field[i][j]; 
				}
			}
			return new Field(field);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (!Arrays.deepEquals(field, other.field))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Field [field=" + Arrays.deepToString(field) + "]";
	}

}
