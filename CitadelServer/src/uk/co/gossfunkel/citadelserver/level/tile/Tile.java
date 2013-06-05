package uk.co.gossfunkel.citadelserver.level.tile;

public class Tile {

	// -------------------- variables -----------------------------------------
	
	public int x, y;
	
	// -------------------- tiles ---------------------------------------------

	public static Tile voidTile = new VoidTile();
	public static Tile solid = new SolidTile();
	public static Tile water = new WaterTile();
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public String toString() {
		return ("TILE ---\n" + "\nx: " + x + ", y: " + y + "\n---");
	}
	
	public boolean solid() {
		return false;
	}
	
}
