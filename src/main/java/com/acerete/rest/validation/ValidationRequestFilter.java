package com.acerete.rest.validation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.acerete.datamodel.DataModel;
import com.acerete.exceptions.DestinationIsNotEmptyException;
import com.acerete.exceptions.GameIsFinishedException;
import com.acerete.exceptions.IndexOutOfFieldException;
import com.acerete.exceptions.NoContentException;
import com.acerete.exceptions.NotEnoughPlayersException;
import com.acerete.exceptions.TooManyPlayersException;
import com.acerete.exceptions.WrongParameterFormatException;
import com.acerete.exceptions.WrongTurnException;
import com.acerete.services.ZeptictacServiceImpl;
import com.acerete.utils.RandomUtils;
import com.acerete.vo.Field;
import com.acerete.vo.Game;
import com.acerete.vo.Mark;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Support to validate calls
 */
@Validated
@Priority(Priorities.USER)
public class ValidationRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String method = requestContext.getMethod();
		List<String> parametersGameId = requestContext.getUriInfo().getPathParameters().get("id");
		if ((method.equals("GET") || method.equals("PUT") || method.equals("PATCH"))
				&& parametersGameId != null && !parametersGameId.isEmpty()) {
			
			String gameId = parametersGameId.iterator().next();
			
			if (!gameId.matches(RandomUtils.NUMBERS_AND_LOWER_LETTERS_REGEX)) {
				// Validate input format
				throw new WrongParameterFormatException("Game id should be only numbers and lower case letters");
			}
			
			Game game = ZeptictacServiceImpl.getInstance().getGameById(gameId);
			if (game == null) {
				// No content for this game id, abort
				throw new NoContentException("No game for this gameId: " + gameId);
			}
				
			// Validate PATCH game (join game)
			if (method.equals("PATCH")) {
				if (game.getPlayers().size() >= Field.PLAYER_SYMBOLS.size()) {
					// Game is full and started
					throw new TooManyPlayersException(gameId);
				}
			}
			
			// Validate PUT game and JSON body (put mark)
			if (method.equals("PUT")) {
				
				if (game.isFinished()) {
					// Game is finished
					throw new GameIsFinishedException(gameId);
				}
				
				if (game.getPlayers().size() != Field.PLAYER_SYMBOLS.size()) {
					// Game is not ready yet, not enough players yet
					throw new NotEnoughPlayersException(gameId);
				}
				
				// Get JSON mark from body
				ObjectMapper mapper = new ObjectMapper();
				InputStream inputStream = requestContext.getEntityStream();
				Mark mark = mapper.readValue(inputStream, Mark.class);
				
				// Replace inputStream since Jersey has already read it
				requestContext.setEntityStream(new ByteArrayInputStream(mapper.writeValueAsBytes(mark)));
				
				if (!game.getNextTurn().equals(mark.getPlayerId())) {
					// Not your turn to play
					throw new WrongTurnException("It is " + game.getNextTurn() + "'s turn");
				}
				
				if (mark.getX() < 0 || mark.getX() >= Field.SIZE || mark.getY() < 0 || mark.getY() >= Field.SIZE) {
					// Mark is out of limits
					throw new IndexOutOfFieldException(Field.INDEX_ERROR_MESSAGE);
				}
				
				if (game.getField().get(mark.getX(), mark.getY()) != Field.EMPTY_SYMBOL) {
					// Destination cell is not empty
					throw new DestinationIsNotEmptyException("x=" + mark.getX() + ",y=" + mark.getY());
				}
			}
		}
		
		if (method.equals("POST") || method.equals("PUT")) {
			List<String> parametersNickname = requestContext.getUriInfo().getQueryParameters().get("nickname");
			if (parametersNickname != null && !parametersNickname.isEmpty()) {
				String nickname = parametersNickname.iterator().next();
				if (!nickname.matches(RandomUtils.NUMBERS_AND_LOWER_LETTERS_REGEX)) {
					// Validate input format
					throw new WrongParameterFormatException("Nickname should be only numbers and lower case letters");
				}
			}
		}
		if (method.equals("GET") && parametersGameId == null) {
			List<String> parametersOffset = requestContext.getUriInfo().getQueryParameters().get("offset");
			if (parametersOffset != null && !parametersOffset.isEmpty()) {
				String offset = parametersOffset.iterator().next();
				try {
					// Validate input format
					int value = Integer.parseInt(offset);
					if (value < 0 || value >= ZeptictacServiceImpl.getInstance().getAllGamesSize()) {
						throw new WrongParameterFormatException("Wrong offset parameter");
					}
				}
				catch (NumberFormatException ex) {
					throw new WrongParameterFormatException("Offset should be a number");
				}
			}
			List<String> parametersLimit = requestContext.getUriInfo().getQueryParameters().get("limit");
			if (parametersLimit != null && !parametersLimit.isEmpty()) {
				String limit = parametersLimit.iterator().next();
				try {
					// Validate input format
					int value = Integer.parseInt(limit);
					if (value < DataModel.MIN_PAGINATION_LIMIT_DEFAULT || value > DataModel.MAX_PAGINATION_LIMIT_DEFAULT) {
						throw new WrongParameterFormatException("Wrong limit parameter");
					}
				}
				catch (NumberFormatException ex) {
					throw new WrongParameterFormatException("Limit should be a number");
				}
			}
		}
	}
}
