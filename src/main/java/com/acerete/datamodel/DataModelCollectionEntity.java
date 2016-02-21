package com.acerete.datamodel;

import java.util.List;

import com.acerete.vo.Game;

public class DataModelCollectionEntity {

	private List<Game> collection;
	private Integer previousOffset;
	private Integer nextOffset;
	private Integer limit;
	
	public DataModelCollectionEntity(List<Game> collection, Integer previousOffset, Integer nextOffset, Integer limit) {
		this.collection = collection;
		this.previousOffset = previousOffset;
		this.nextOffset = nextOffset;
		this.limit = limit;
	}

	public List<Game> getCollection() {
		return collection;
	}

	public Integer getPreviousOffset() {
		return previousOffset;
	}

	public Integer getNextOffset() {
		return nextOffset;
	}

	public Integer getLimit() {
		return limit;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModelCollectionEntity other = (DataModelCollectionEntity) obj;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (limit != other.limit)
			return false;
		if (nextOffset != other.nextOffset)
			return false;
		if (previousOffset != other.previousOffset)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataModelCollectionEntity [collection=" + collection + ", previousOffset=" + previousOffset
				+ ", nextOffset=" + nextOffset + ", limit=" + limit + "]";
	}
}
