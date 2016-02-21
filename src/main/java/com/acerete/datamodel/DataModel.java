package com.acerete.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acerete.config.ZeptictacConfig;
import com.acerete.exceptions.WrongParameterFormatException;
import com.acerete.vo.Game;

public class DataModel {

	/**
	 * Very inefficient but in real life this layer would be a database.
	 */
	
	public static final int MIN_PAGINATION_LIMIT_DEFAULT = 1;
	public static final int MAX_PAGINATION_LIMIT_DEFAULT = 20;
	
	public static final int PAGINATION_LIMIT_DEFAULT = 5;
	public static final int PAGINATION_LIMIT = ZeptictacConfig.getPaginationLimit();
	
	private Map<String, DataModelEntity> model;
	
	public DataModel() {
		this.model = new ConcurrentHashMap<String, DataModelEntity>();
	}

	public int getSize() {
		return model.size();
	}
	
	public DataModelCollectionEntity getAllDataModels(Integer offsetIn, Integer limitIn) {
		// This is so bad it hurts :D
		
		// Get all games
		List<Game> all = new ArrayList<Game>();
		Iterator<DataModelEntity> it = model.values().iterator();
		while (it.hasNext()) {
			all.add(it.next().getGame());
		}
		
		// Sort them by creation date
		all.sort((game1, game2) -> game1.getCreatedAt().compareTo(game2.getCreatedAt()));
		
		// Define offset
		int offset = 0;
		if (offsetIn != null) {
			if (offsetIn < 0 || offsetIn >= all.size()) {
				throw new WrongParameterFormatException("Wrong offset");
			}
			offset = offsetIn;
		}
		
		// Define limit
		int limit = PAGINATION_LIMIT;
		if (limitIn != null) {
			if (limitIn < MIN_PAGINATION_LIMIT_DEFAULT || limitIn > MAX_PAGINATION_LIMIT_DEFAULT) {
				throw new WrongParameterFormatException("Wrong limit");
			}
			limit = limitIn;
		}
		
		int first = offset;
		int last = first + Math.min(limit, all.size() - offset);
		
		// Get only according to pagination offset/limit and set the next and previous values
		List<Game> requested = new ArrayList<Game>(all.subList(first, last));
		
		Integer previousOffset = null;
		if (offset != 0) {
			previousOffset = Math.max(0, offset - limit);
		}
		Integer nextOffset = null;
		if (offset + limit < all.size()) {
			nextOffset = Math.min(offset + limit, all.size() - 1);
		}
		return new DataModelCollectionEntity(requested, previousOffset, nextOffset, limitIn);
	}
	
	public DataModelEntity getDataEntityById(String id) {
		return model.get(id);
	}

	public void addOrUpdateDataEntity(DataModelEntity entity) {
		model.put(entity.getId(), entity);
	}
	
	@Override
	public String toString() {
		return "Model [model=" + model + "]";
	}
}
