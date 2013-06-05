package uk.co.gossfunkel.citadelserver.entity.settlement;

import uk.co.gossfunkel.citadelserver.entity.Entity;
import uk.co.gossfunkel.citadelserver.level.TileCoordinate;

public class Settlement extends Entity {
	
	protected int level;

	public Settlement(int x, int y) {
		System.out.println("Making new Hamlet\nx:" + x + ", y:" + y);
		this.x = TileCoordinate.round(x);
		this.y = TileCoordinate.round(y);
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void update() {
		
	}
	
	public void levelUp() {
		level++;
	}
}
