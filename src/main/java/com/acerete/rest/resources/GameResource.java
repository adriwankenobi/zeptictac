package com.acerete.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.acerete.rest.authorization.Authorized;
import com.acerete.rest.config.PATCH;
import com.acerete.rest.decoration.Decorated;
import com.acerete.rest.validation.Validated;
import com.acerete.services.ZeptictacService;
import com.acerete.services.ZeptictacServiceImpl;
import com.acerete.vo.Game;
import com.acerete.vo.GameCollection;
import com.acerete.vo.Mark;

@Path("/v1/games")
@Validated
@Decorated
public class GameResource {

	private final ZeptictacService zeptictacService = ZeptictacServiceImpl.getInstance();
	
	/**
	 * Get all games
	 * @return game collection
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GameCollection getAllGames(@QueryParam("offset") Integer offset, 
									  @QueryParam("limit") Integer limit,
									  @Context UriInfo uriInfo) {
		return zeptictacService.getAllGames(offset, limit, uriInfo);
	}
	
	/**
	 * Get game by id
	 * @param id
	 * @return game
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Game getGame(@PathParam("id") String id) {
		return zeptictacService.getGameById(id);
	}
	
	/**
	 * Create new game
	 * @param nickname
	 * @return game
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Game createNewGame(@QueryParam("nickname") String nickname) {
		return zeptictacService.createNewGame(nickname);
	}
	
	/**
	 * Join game
	 * @param gameId
	 * @param nickname
	 * @return
	 */
	@PATCH
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Game joinGame(@PathParam("id") String gameId, @QueryParam("nickname") String nickname) {
		return zeptictacService.joinGame(gameId, nickname);
	}
	
	/**
	 * Put a mark in this game
	 * @param gameId
	 * @param mark
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Authorized
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Game putMark(@PathParam("id") String gameId, Mark mark) {
		return zeptictacService.putMarkInGameByPlayerId(gameId, mark);
	}
	
}
