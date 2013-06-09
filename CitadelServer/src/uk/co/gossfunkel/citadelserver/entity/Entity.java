package uk.co.gossfunkel.citadelserver.entity;

import java.util.List;
import java.util.Random;
import uk.co.gossfunkel.citadelserver.level.Level;

public abstract class Entity {
	
	// -------------------- variables -----------------------------------------
	 
	protected int x, y;
	private boolean removed = false;
	protected Level level;
	protected final Random random = new Random();
	protected int SIZE;
	protected List<Entity> nearEntities;
	
	// -------------------- methods -------------------------------------------
	
	public void update() {}
	
	// -------------------- getters -------------------------------------------
	
	public boolean isRemoved() {
		return removed;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int getSIZE() {
		return SIZE;
	}

	// -------------------- setters -------------------------------------------

	public void remove() {
		removed = true;
	}
	
	public void addNear(Entity e) {
		nearEntities.add(e);
	}
	
	public void teleport(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
