package com.acerete.rest.resources;

import static org.junit.Assert.assertEquals;

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
import com.acerete.vo.Field;
import com.acerete.vo.Game;
import com.acerete.vo.Mark;
import com.acerete.vo.Player;

public class TestTech extends SetupTest {
	
	@Test
	public void TestWrongPath() throws Exception {
		Response response = target("/wrong").request().get();
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void TestGZIPEncoded() throws Exception {
		Response response = target("/v1/games").request().header("Accept-Encoding", "gzip").get();
		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		assertEquals("gzip", response.getHeaderString("Content-Encoding"));
	}
	
	@Test
	public void TestUnauthorizedPlayer() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Mark mark = new Mark("someWrongId", 0, 0);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(mark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
	}
	
	@Test
	public void TestWrongOffsetFormat() throws Exception {
		Response response = target("/v1/games").queryParam("offset", "wrongformat").request().get();
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void TestWrongOffset() throws Exception {
		String wrongOffset = String.valueOf(ZeptictacServiceImpl.getInstance().getAllGamesSize() + 1);
		Response response = target("/v1/games").queryParam("offset", wrongOffset).request().get();
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void TestWrongLimitFormat() throws Exception {
		Response response = target("/v1/games").queryParam("limit", "wrongformat").request().get();
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void TestWrongLimit() throws Exception {
		String wrongLimit = String.valueOf(DataModel.MAX_PAGINATION_LIMIT_DEFAULT + 1);
		Response response = target("/v1/games").queryParam("limit", wrongLimit).request().get();
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void TestGetNoContent() throws Exception {
		Response response = target("/v1/games/unexistinggameid").request().get();
		assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
	}
	
	@Test
	public void TestWrongNickname() throws Exception {
		Response response = target("/v1/games").queryParam("nickname", "Wrong_Name").request().post(null);
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void TestPutNoContent() throws Exception {
		Mark mark = new Mark("foo", 0, 0);
		Response response = target("/v1/games/unexistinggameid").request().put(Entity.entity(mark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_NO_CONTENT, response.getStatus());
	}
	
	@Test
	public void TestGameIsFinished() throws Exception {
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
		marks.add(new Mark(player1.getId(), 0, 2));
		putMarks(game.getId(), marks);
		Mark anotherMark = new Mark(player1.getId(), 2, 2); // Game over but another attempt
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(anotherMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
	}
	
	@Test
	public void TestGameFull() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Response response = target("/v1/games/"+game.getId()).request().method("PATCH");
		assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
	}
	
	@Test
	public void TestGameNotEnoughPlayers() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Player player = game.getPlayers().iterator().next();
		Mark mark = new Mark(player.getId(), 0, 0);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(mark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
	}
	
	@Test
	public void TestWrongTurn() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Game readyGame = ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Iterator<Player> it = readyGame.getPlayers().iterator();
		it.next();
		Mark mark = new Mark(it.next().getId(), 0, 0);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(mark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
	}
	
	@Test
	public void TestOutOfBoundaries() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		Mark mark = new Mark(game.getNextTurn(), Field.SIZE + 1, Field.SIZE + 1);
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(mark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
	}
	
	@Test
	public void TestDestinationNotEmpty() throws Exception {
		Game game = ZeptictacServiceImpl.getInstance().createNewGame();
		ZeptictacServiceImpl.getInstance().joinGame(game.getId());
		Mark firstMark = new Mark(game.getNextTurn(), 0, 0);
		Game gameAfterFirstMark = ZeptictacServiceImpl.getInstance().putMarkInGameByPlayerId(game.getId(), firstMark);
		Mark secondMark = new Mark(gameAfterFirstMark.getNextTurn(), firstMark.getX(), firstMark.getY());
		Response response = target("/v1/games/"+game.getId()).request().put(Entity.entity(secondMark, MediaType.APPLICATION_JSON));
		assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
	}
}
