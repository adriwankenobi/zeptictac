package com.acerete.vo;

import com.acerete.utils.RandomUtils;
import com.acerete.vo.deserializers.PlayerDeserializer;
import com.acerete.vo.serializers.PlayerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PlayerSerializer.class)
@JsonDeserialize(using = PlayerDeserializer.class)
public class Player {

	public static final String KEY_ID = "id";
	public static final String KEY_NICKNAME = "nickname";
	public static final String KEY_SYMBOL = "symbol";
	
	private final String id;
	private final String nickname;
	private final char symbol;
	
	public Player(String nickname, char symbol) {
		this.id = RandomUtils.generateUniqueId();
		this.nickname = nickname;
		this.symbol = symbol;
	}
	
	public Player(String id, String nickname, char symbol) {
		this.id = id;
		this.nickname = nickname;
		this.symbol = symbol;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public char getSymbol() {
		return symbol;
	}

	public static Player copy(Player other) {
		if (other != null) {
			return new Player(other.id, other.nickname, other.symbol);
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
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", nickname=" + nickname + ", symbol=" + symbol + "]";
	}
}
