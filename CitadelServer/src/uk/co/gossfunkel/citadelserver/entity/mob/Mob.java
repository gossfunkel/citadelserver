package uk.co.gossfunkel.citadelserver.entity.mob;

import uk.co.gossfunkel.citadelserver.entity.Entity;
import uk.co.gossfunkel.citadelserver.level.tile.Tile;

public abstract class Mob extends Entity {
	
	// -------------------- variables -----------------------------------------
	
	protected int dir = 0;
	protected boolean moving = false;
	protected boolean swimming = false;
	protected Tile[] tile;
	
	// -------------------- methods -------------------------------------------
	
	public void update() {}

	protected void move(int xa, int ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return;
		}
		if (xa > 0) dir = 1;
		if (xa < 0) dir = 3;
		if (ya > 0) dir = 0;
		if (ya < 0) dir = 2;
		
		x += xa;
		y += ya;
	}

	// -------------------- getters -------------------------------------------
	
	
}
