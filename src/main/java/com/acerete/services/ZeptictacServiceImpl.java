package com.acerete.services;


import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.acerete.config.ZeptictacConfig;
import com.acerete.datamodel.DataModel;
import com.acerete.datamodel.DataModelCollectionEntity;
import com.acerete.datamodel.DataModelEntity;
import com.acerete.exceptions.ServerConfigException;
import com.acerete.vo.Game;
import com.acerete.vo.GameCollection;
import com.acerete.vo.Mark;

public class ZeptictacServiceImpl implements ZeptictacService {

	// Thread safe lazy initialization singleton
	private static class LazyHolder {
		private static final ZeptictacServiceImpl INSTANCE = new ZeptictacServiceImpl();
	}
		
	public static ZeptictacService getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private DataModel model = new DataModel();
	
	@Override
	public int getAllGamesSize() {
		return model.getSize();
	}
	
	@Override
	public GameCollection getAllGames(Integer offset, Integer limit, UriInfo uriInfo) {
		DataModelCollectionEntity collection = model.getAllDataModels(offset, limit);
		GameCollection gameCollection = new GameCollection(collection.getCollection());
		UriBuilder previousUriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		UriBuilder nextUriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		if (collection.getPreviousOffset() != null) {
			if (collection.getPreviousOffset() != 0) {
				setQueryParam(previousUriBuilder, "offset", collection.getPreviousOffset());
			}
			setQueryParam(previousUriBuilder, "limit", collection.getLimit());
			gameCollection.setPrevious(previousUriBuilder.build().toString());
		}
		if (collection.getNextOffset() != null) {
			setQueryParam(nextUriBuilder, "offset", collection.getNextOffset());
			setQueryParam(nextUriBuilder, "limit", collection.getLimit());
			gameCollection.setNext(nextUriBuilder.build().toString());
		}
		return gameCollection;
	}
	
	private void setQueryParam(UriBuilder builder, String tag, Integer value) {
		if (value != null) {
			builder.queryParam(tag, value);
		}
	}

	@Override
	public Game getGameById(String id) {
		DataModelEntity entity = model.getDataEntityById(id);
		if (entity != null) {
			return entity.getGame();
		}
		return null;
	}

	@Override
	public Game createNewGame() {
		return createNewGame(null);
	}
	
	@Override
	public Game createNewGame(String nickname) {
		Game created = new Game(nickname);
		model.addOrUpdateDataEntity(new DataModelEntity(created));
		return created;
	}

	@Override
	public Game joinGame(String gameId) {
		return joinGame(gameId, null);
	}
	
	@Override
	public Game joinGame(String gameId, String nickname) {
		Game game = model.getDataEntityById(gameId).getGame();
		game.join(nickname);
		model.addOrUpdateDataEntity(new DataModelEntity(game));
		return game;
	}
	
	@Override
	public Game putMarkInGameByPlayerId(String gameId, Mark mark) {
		Game game = model.getDataEntityById(gameId).getGame();
		game.putMark(mark);
		model.addOrUpdateDataEntity(new DataModelEntity(game));
		return game;
	}
	
	@Override
	public void clearModel() {
		if (!ZeptictacConfig.isTest()) {
			throw new ServerConfigException("Cannot be executed outside test cases");
		}
		model = new DataModel();
	}
}
