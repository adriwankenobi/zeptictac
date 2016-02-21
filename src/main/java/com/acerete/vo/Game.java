package com.acerete.vo;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.acerete.exceptions.DestinationIsNotEmptyException;
import com.acerete.exceptions.GameIsFinishedException;
import com.acerete.exceptions.NotEnoughPlayersException;
import com.acerete.exceptions.TooManyPlayersException;
import com.acerete.exceptions.WrongTurnException;
import com.acerete.utils.DateUtils;
import com.acerete.utils.RandomUtils;
import com.acerete.vo.deserializers.GameDeserializer;
import com.acerete.vo.serializers.GameSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = GameSerializer.class)
@JsonDeserialize(using = GameDeserializer.class)
public class Game {
	
	public static final String KEY_ID = "id";
	public static final String KEY_FIELD = "field";
	public static final String KEY_PLAYERS = "players";
	public static final String KEY_IS_FINISHED = "isFinished";
	public static final String KEY_TURN_NUMBER = "turnNumber";
	public static final String KEY_NEXT_TURN = "nextTurn";
	public static final String KEY_CREATED_AT = "createdAt";
	
	public static final String ANONYMOUS_PLAYER_NICKNAME = "AnonymousPlayer";
	
	private String id;
	private Field field;
	// Sorted by insertion order, which is turns order
	private LinkedHashMap<String, Player> players;
	
	private boolean isFinished;
	private int turnNumber;
	private String nextTurn;
	
	private Date createdAt;
	
	public Game(String id, Field field, LinkedHashMap<String, Player> players, 
			boolean isFinished, int turnNumber, String nextTurn, Date createdAt) {
		this.id = id;
		this.field = field;
		this.players = players;
		this.isFinished = isFinished;
		this.turnNumber = turnNumber;
		this.nextTurn = nextTurn;
		this.createdAt = createdAt;
	}
	
	public Game(String nickname) {
		this.id = RandomUtils.generateUniqueId();
		this.field = new Field();
		this.players = new LinkedHashMap<String, Player>();
		if (nickname == null) {
			nickname = ANONYMOUS_PLAYER_NICKNAME+"1";
		}
		Player player = new Player(nickname, Field.PLAYER_SYMBOLS.iterator().next());
		this.players.put(player.getId(), player);
		this.nextTurn = player.getId();
		this.isFinished = false;
		this.turnNumber = 0;
		this.createdAt = new Date();
	}
	
	public String getId() {
		return id;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public String getNextTurn() {
		return nextTurn;
	}

	public Collection<Player> getPlayers() {
		return players.values();
	}

	public Field getField() {
		return field;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void join(String nickname) {
		if (players.size() >= Field.PLAYER_SYMBOLS.size()) {
			throw new TooManyPlayersException("Game is full and started already");
		}
		if (nickname == null) {
			nickname = ANONYMOUS_PLAYER_NICKNAME+(players.size()+1);
		}
		Player player = new Player(nickname, Field.PLAYER_SYMBOLS.get(players.size()));
		players.put(player.getId(), player);
	}
	
	public void putMark(Mark mark) {
		if (players.size() != Field.PLAYER_SYMBOLS.size()) {
			throw new NotEnoughPlayersException("Not enough players");
		}
		validateMark(mark);
		char currentSymbol = players.get(mark.getPlayerId()).getSymbol();
		field.set(mark.getX(), mark.getY(), currentSymbol);
		this.turnNumber++;
		// Next turn's player is indexed by: (newTurnNumber % numPlayers) + 1
		// Search by shifting from players linked list
		int shifts = (turnNumber % Field.PLAYER_SYMBOLS.size()) + 1;
		Iterator<Player> it = players.values().iterator();
		while (shifts > 0) {
			Player player = it.next();
			shifts--;
			if (shifts == 0) {
				this.nextTurn = player.getId();
			}
		}
		// Check if game is finished
		this.isFinished = field.matchesWidth(currentSymbol, mark.getX()) || 
				field.matchesHeight(currentSymbol, mark.getY()) || 
				field.matchesFirstDiagonal(currentSymbol, mark.getX(), mark.getY()) ||
				field.matchesSecondDiagonal(currentSymbol, mark.getX(), mark.getY()) ||
				field.isFull();
	}
	
	private void validateMark(Mark mark) {
		if(isFinished) {
			throw new GameIsFinishedException(getId());
		}
		if (!mark.getPlayerId().equals(nextTurn)) {
			throw new WrongTurnException("It is " + getNextTurn() + "'s turn");
		}
		if (field.get(mark.getX(), mark.getY()) != Field.EMPTY_SYMBOL) {
			throw new DestinationIsNotEmptyException("x=" + mark.getX() + ",y=" + mark.getY());
		}
	}

	public static Game copy(Game other) {
		if (other != null) {
			LinkedHashMap<String, Player> players = new LinkedHashMap<String, Player>();
			for (Player player : other.players.values()) {
				players.put(player.getId(), Player.copy(player));
			}
			return new Game(other.id,
							Field.copy(other.field),
							players,
							other.isFinished,
							other.turnNumber,
							other.nextTurn,
							other.createdAt);
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
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", field=" + field + ", players=" + players + ", isFinished=" + isFinished
				+ ", turnNumber=" + turnNumber + ", nextTurn=" + nextTurn + ", createdAt=" + DateUtils.formatIso8601Date(createdAt) + "]";
	}
}
