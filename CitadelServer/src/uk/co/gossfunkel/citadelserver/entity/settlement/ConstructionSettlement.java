package uk.co.gossfunkel.citadelserver.entity.settlement;

import uk.co.gossfunkel.citadelserver.Game;
import uk.co.gossfunkel.citadelserver.entity.Entity;
import uk.co.gossfunkel.citadelserver.level.TileCoordinate;
import uk.co.gossfunkel.citadelserver.level.tile.Tile;

public class ConstructionSettlement extends Entity {
	
	private Game game;
	protected Tile sprite;
	protected int level;
	private int progress = 0;
	private String maker;

	public ConstructionSettlement(Game game, int x, int y, String usnm) {
		this.game = game;
		maker = usnm;
		this.x = TileCoordinate.round(x);
		this.y = TileCoordinate.round(y);
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void update() {
		progress++;
		if (progress > 1000) complete();
	}
	
	private void complete() {
		Settlement s = new Settlement(x, y, maker);
		game.addSett(s);
	}

}
