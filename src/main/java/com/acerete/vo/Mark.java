package com.acerete.vo;

import com.acerete.vo.deserializers.MarkDeserializer;
import com.acerete.vo.serializers.MarkSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = MarkSerializer.class)
@JsonDeserialize(using = MarkDeserializer.class)
public class Mark {

	public static final String KEY_PLAYER_ID = "playerId";
	public static final String KEY_X = "x";
	public static final String KEY_Y = "y";
	
	private final String playerId;
	private final int x;
	private int y;
	
	public Mark(String playerId, int x, int y) {
		this.playerId = playerId;
		this.x = x;
		this.y = y;
	}
	
	public String getPlayerId() {
		return playerId;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mark other = (Mark) obj;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Move [playerId=" + playerId + ", x=" + x + ", y=" + y + "]";
	}
}
