package com.acerete.vo;

import java.util.ArrayList;
import java.util.List;

import com.acerete.vo.deserializers.GameCollectionDeserializer;
import com.acerete.vo.serializers.GameCollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = GameCollectionSerializer.class)
@JsonDeserialize(using = GameCollectionDeserializer.class)
public class GameCollection {

	public static final String KEY_DATA = "data";
	public static final String KEY_PREVIOUS = "previuos";
	public static final String KEY_NEXT = "next";
	
	private List<Game> data;
	private String previous;
	private String next;
	
	public GameCollection() {
		this(new ArrayList<Game>());
	}
	
	public GameCollection(List<Game> data) {
		this.data = data;
		this.previous = null;
		this.next = null;
	}

	public List<Game> getData() {
		return data;
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	public int size() {
		return data.size();
	}

	public String getPrevious() {
		return previous;
	}

	public String getNext() {
		return next;
	}
	
	public void addGame(Game game) {
		data.add(game);
	}
	
	public void addAllGames(List<Game> game) {
		data.addAll(game);
	}
	
	public void setPrevious(String previous) {
		this.previous = previous;
	}
	
	public void setNext(String next) {
		this.next = next;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameCollection other = (GameCollection) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (previous == null) {
			if (other.previous != null)
				return false;
		} else if (!previous.equals(other.previous))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameCollection [data=" + data + ", previous=" + previous + ", next=" + next + "]";
	}
}
