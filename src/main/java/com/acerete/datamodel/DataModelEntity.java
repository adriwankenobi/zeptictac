package com.acerete.datamodel;

import com.acerete.vo.Game;

public class DataModelEntity {

	private Game game;

	public DataModelEntity(Game game) {
		this.game = Game.copy(game);
	}
	
	public String getId() {
		return game.getId();
	}
	
	public Game getGame() {
		return Game.copy(game);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModelEntity other = (DataModelEntity) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataModelEntity [game=" + game + "]";
	}
}