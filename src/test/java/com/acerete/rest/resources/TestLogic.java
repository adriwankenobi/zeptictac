package com.acerete.rest.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.acerete.datamodel.DataModel;
import com.acerete.services.ZeptictacServiceImpl;
import com.acerete.test.SetupTest;
import com.acerete.utils.DateUtils;
import com.acerete.vo.Field;
import com.acerete.vo.Game;
import com.acerete.vo.GameCollection;
import com.acerete.vo.Mark;
import com.acerete.vo.Player;

public class TestLogic extends SetupTest {
	
	@Test
	public void TestValidAndEmpty() throws Exception {
		Response response = target("/v1/games").request().get();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		GameCollection collection = response.readEntity(GameCollection.class);
		assertTrue(collection.isEmpty());
		assertNull(collection.getPrevious());
		assertNull(collection.getNext());
	}
	
	@Test
	public void TestPaginationNext() throws Exception {
		for (int i = 0; i < DataModel.PAGINATION_LIMIT+1; i++) {
			ZeptictacServiceImpl.getInstance().createNewGame();
		}
		Response response = target("/v1/games").request().get();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		GameCollection collection = response.readEntity(GameCollection.class);
		assertEquals(DataModel.PAGINATION_LIMIT, collection.getData().size());
		assertNull(collection.getPrevious()); 
		assertEquals(getBaseUri()+"v1/games?offset=5", collection.getNext());
	}
	
	@Test
	public void TestPaginationPrevious() throws Exception {
		for (int i = 0; i < DataModel.PAGINATION_LIMIT+1; i++) {
			ZeptictacServiceImpl.getInstance().createNewGame();
		}
		Response response = target("/v1/games").queryParam("offset", "4").request().get();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		GameCollection collection = response.readEntity(GameCollection.class);
		assertEquals(2, collection.getData().size());
		assertEquals(getBaseUri()+"v1/games", collection.getPrevious()); 
		assertNull(collection.getNext());
	}
	
	@Test
	public void TestPaginationLimit() throws Exception {
		for (int i = 0; i < DataModel.PAGINATION_LIMIT+1; i++) {
			ZeptictacServiceImpl.getInstance().createNewGame();
		}
		Response response = target("/v1/games").queryParam("offset", "1").queryParam("limit", "3").request().get();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		GameCollection collection = response.readEntity(GameCollection.class);
		assertEquals(3, collection.getData().size());
		assertEquals(getBaseUri()+"v1/games?limit=3", collection.getPrevious()); 
		assertEquals(getBaseUri()+"v1/games?offset=4&limit=3", collection.getNext());
	}
	
	@Test
	public void TestCreateGame() throws Exception {
		Response response = target("/v1/games").request().post(null);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game game = response.readEntity(Game.class);
		assertFalse(game.isFinished());
		assertEquals(0, game.getTurnNumber());
		for (int i = 0; i < Field.SIZE; i++) {
			for (int j = 0; j < Field.SIZE; j++) {
				assertEquals(Field.EMPTY_SYMBOL, game.getField().get(i, j));
			}
		}
		assertEquals(1, game.getPlayers().size());
		Player player = game.getPlayers().iterator().next();
		assertEquals(player.getNickname(), Game.ANONYMOUS_PLAYER_NICKNAME+"1");
		assertEquals(player.getSymbol(), Field.PLAYER_SYMBOLS.iterator().next().charValue());
	}
	
	@Test
	public void TestCreateGameWithNickname() throws Exception {
		String myName = "myname";
		Response response = target("/v1/games").queryParam("nickname", myName).request().post(null);
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game game = response.readEntity(Game.class);
		assertFalse(game.isFinished());
		assertEquals(0, game.getTurnNumber());
		for (int i = 0; i < Field.SIZE; i++) {
			for (int j = 0; j < Field.SIZE; j++) {
				assertEquals(Field.EMPTY_SYMBOL, game.getField().get(i, j));
			}
		}
		assertEquals(1, game.getPlayers().size());
		Player player = game.getPlayers().iterator().next();
		assertEquals(player.getNickname(), myName);
		assertEquals(player.getSymbol(), Field.PLAYER_SYMBOLS.iterator().next().charValue());
	}
	
	@Test
	public void TestJoinGame() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Response response = target("/v1/games/"+game.getId()).request().method("PATCH");
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertFalse(responseGame.isFinished());
		assertEquals(0, responseGame.getTurnNumber());
		for (int i = 0; i < Field.SIZE; i++) {
			for (int j = 0; j < Field.SIZE; j++) {
				assertEquals(Field.EMPTY_SYMBOL, responseGame.getField().get(i, j));
			}
		}
		assertEquals(2, responseGame.getPlayers().size());
		Iterator<Player> itPlayers = responseGame.getPlayers().iterator();
		Iterator<Character> itSymbols = Field.PLAYER_SYMBOLS.iterator();
		for (int i = 0; i < responseGame.getPlayers().size(); i++) {
			assertEquals(itSymbols.next().charValue(), itPlayers.next().getSymbol());
		}
	}
	
	@Test
	public void TestGetMyEmptyGame() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Response response = target("/v1/games/"+game.getId()).request().get();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(game.getId(), responseGame.getId());
		assertEquals(DateUtils.formatIso8601Date(game.getCreatedAt()), DateUtils.formatIso8601Date(responseGame.getCreatedAt()));
		assertEquals(game.getNextTurn(), responseGame.getNextTurn());
		assertEquals(game.getTurnNumber(), responseGame.getTurnNumber());
		assertEquals(game.isFinished(), responseGame.isFinished());
		assertEquals(game.getPlayers().size(), responseGame.getPlayers().size());
		Iterator<Player> itPlayers = game.getPlayers().iterator();
		Iterator<Player> itPlayersResponse = responseGame.getPlayers().iterator();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			assertEquals(itPlayers.next(), itPlayersResponse.next());
		}
		for (int i = 0; i < Field.SIZE; i++) {
			for (int j = 0; j < Field.SIZE; j++) {
				assertEquals(game.getField().get(i, j), responseGame.getField().get(i, j));
			}
		}
	}
	
	@Test
	public void TestPutMark() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		Player player = it.next();
		Mark mark = new Mark(player.getId(), 0, 0);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(mark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(readyGame.getId(), responseGame.getId());
		assertEquals(DateUtils.formatIso8601Date(readyGame.getCreatedAt()), DateUtils.formatIso8601Date(responseGame.getCreatedAt()));
		assertEquals(it.next().getId(), responseGame.getNextTurn());
		assertEquals(readyGame.getTurnNumber()+1, responseGame.getTurnNumber());
		assertEquals(readyGame.isFinished(), responseGame.isFinished());
		assertEquals(readyGame.getPlayers().size(), responseGame.getPlayers().size());
		for (int i = 0; i < Field.SIZE; i++) {
			for (int j = 0; j < Field.SIZE; j++) {
				if (i == mark.getX() && j == mark.getY()) {
					assertEquals(readyGame.getField().get(i, j), Field.EMPTY_SYMBOL);
					assertEquals(responseGame.getField().get(i, j), player.getSymbol());
				}
				else {
					assertEquals(readyGame.getField().get(i, j), responseGame.getField().get(i, j));
				}	
			}
		}
	}
	
	@Test
	public void TestPutLastMarkWidth() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		Player player1 = it.next();
		Player player2 = it.next();
		List<Mark> marks = new ArrayList<Mark>();
		marks.add(new Mark(player1.getId(), 0, 0)); // Player 1 wins width
		marks.add(new Mark(player2.getId(), 1, 0)); // dummy
		marks.add(new Mark(player1.getId(), 0, 1));
		marks.add(new Mark(player2.getId(), 2, 0)); // dummy
		putMarks(game.getId(), marks);
		Mark finalMark = new Mark(player1.getId(), 0, 2);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(finalMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(marks.size()+1, responseGame.getTurnNumber());
		assertTrue(responseGame.isFinished());
	}
	
	@Test
	public void TestPutLastMarkHeight() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		Player player1 = it.next();
		Player player2 = it.next();
		List<Mark> marks = new ArrayList<Mark>();
		marks.add(new Mark(player1.getId(), 0, 0)); // Player 1 wins height
		marks.add(new Mark(player2.getId(), 0, 1)); // dummy
		marks.add(new Mark(player1.getId(), 1, 0));
		marks.add(new Mark(player2.getId(), 0, 2)); // dummy
		putMarks(game.getId(), marks);
		Mark finalMark = new Mark(player1.getId(), 2, 0);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(finalMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(marks.size()+1, responseGame.getTurnNumber());
		assertTrue(responseGame.isFinished());
	}
	
	@Test
	public void TestPutLastMarkFirstDiagonal() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		Player player1 = it.next();
		Player player2 = it.next();
		List<Mark> marks = new ArrayList<Mark>();
		marks.add(new Mark(player1.getId(), 0, 0)); // Player 1 wins 1st diagonal
		marks.add(new Mark(player2.getId(), 0, 1)); // dummy
		marks.add(new Mark(player1.getId(), 1, 1));
		marks.add(new Mark(player2.getId(), 0, 2)); // dummy
		putMarks(game.getId(), marks);
		Mark finalMark = new Mark(player1.getId(), 2, 2);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(finalMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(marks.size()+1, responseGame.getTurnNumber());
		assertTrue(responseGame.isFinished());
	}
	
	@Test
	public void TestPutLastMarkSecondDiagonal() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		Player player1 = it.next();
		Player player2 = it.next();
		List<Mark> marks = new ArrayList<Mark>();
		marks.add(new Mark(player1.getId(), 0, 2)); // Player 1 wins 2nd diagonal
		marks.add(new Mark(player2.getId(), 0, 1)); // dummy
		marks.add(new Mark(player1.getId(), 1, 1));
		marks.add(new Mark(player2.getId(), 0, 0)); // dummy
		putMarks(game.getId(), marks);
		Mark finalMark = new Mark(player1.getId(), 2, 0);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(finalMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(marks.size()+1, responseGame.getTurnNumber());
		assertTrue(responseGame.isFinished());
	}
	
	@Test
	public void TestPutLastMarkFull() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		Player player1 = it.next();
		Player player2 = it.next();
		List<Mark> marks = new ArrayList<Mark>();
		marks.add(new Mark(player1.getId(), 0, 0)); // Blocked combination
		marks.add(new Mark(player2.getId(), 0, 1));
		marks.add(new Mark(player1.getId(), 0, 2));
		marks.add(new Mark(player2.getId(), 1, 0));
		marks.add(new Mark(player1.getId(), 1, 2));
		marks.add(new Mark(player2.getId(), 1, 1));
		marks.add(new Mark(player1.getId(), 2, 0));
		marks.add(new Mark(player2.getId(), 2, 2));
		putMarks(game.getId(), marks);
		Mark finalMark = new Mark(player1.getId(), 2, 1);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(finalMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Game responseGame = response.readEntity(Game.class);
		assertEquals(marks.size()+1, responseGame.getTurnNumber());
		assertTrue(responseGame.isFinished());
	}
}
