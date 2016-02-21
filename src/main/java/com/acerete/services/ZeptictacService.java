package com.acerete.services;

import javax.ws.rs.core.UriInfo;

import com.acerete.vo.Game;
import com.acerete.vo.GameCollection;
import com.acerete.vo.Mark;

public interface ZeptictacService {

	/**
	 * Get all games size
	 * @return size
	 */
	public int getAllGamesSize();
	
	/**
	 * Get all games
	 * @param offset
	 * @param limit
	 * @param uri
	 * @return collection
	 */
	public GameCollection getAllGames(Integer offset, Integer limit, UriInfo uriInfo);
	
	/**
	 * Get game by id
	 * @param id
	 * @return game
	 */
	public Game getGameById(String id);
	
	/**
	 * Create new game
	 * @param optional nickname
	 * @return game
	 */
	public Game createNewGame();
	public Game createNewGame(String nickname);
	
	/**
	 * Join game
	 * @param gameId
	 * @param optional nickname
	 * @return
	 */
	public Game joinGame(String gameId);
	public Game joinGame(String gameId, String nickname);
	
	/**
	 * Player puts a mark into cell (x,y)
	 * @param gameId
	 * @param mark
	 * @return
	 */
	public Game putMarkInGameByPlayerId(String gameId, Mark mark);

	/**
	 * Clear model
	 * WARNING: For test case purposes only
	 */
	public void clearModel();
}
