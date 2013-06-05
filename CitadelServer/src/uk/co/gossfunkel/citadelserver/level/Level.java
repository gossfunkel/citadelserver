package uk.co.gossfunkel.citadelserver.level;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import uk.co.gossfunkel.citadelserver.entity.Entity;
import uk.co.gossfunkel.citadelserver.level.tile.Coord;
import uk.co.gossfunkel.citadelserver.level.tile.Tile;
import uk.co.gossfunkel.citadelserver.entity.Tree;
import uk.co.gossfunkel.citadelserver.entity.mob.OnlinePlayer;

public class Level {
	
	// -------------------- variables -----------------------------------------

	protected static int spawnX, spawnY;
	protected static int width, height;
	protected static int[] tiles;
	protected static Tile[] ttiles;
	protected List<Entity> entities = new ArrayList<Entity>();
	protected static List<Tree> trees = new ArrayList<Tree>();
	
	// -------------------- constructors --------------------------------------
	
	public Level(int width, int height) {
		
		Level.width = width;
		Level.height = height;
		tiles = new int[width*height];
		
		generateLevel();
		populate();
		
	}
	
	public Level(String path) {
		loadLevel(path);
		generateLevel();
		populate();
	}
	
	// -------------------- methods -------------------------------------------
	
	// randomly generate a level
	protected void generateLevel() {}
	
	// load a level from file
	protected void loadLevel(String path) {}
	
	public void update() {
		for (int i = 0; i < getEntities().size(); i++) {
			if (getEntities().get(i).isRemoved()) getEntities().remove(i);
			else getEntities().get(i).update();
		}
	}
	
	public Integer[] getCoordXs() {
		List<Integer> coord = new ArrayList<Integer>(trees.size());
		for (int i = 0; i < coord.size(); i++) {
			coord.add(trees.get(i).getCoord().x());
		}
		return coord.toArray(new Integer[coord.size()]);
	}
	
	public Integer[] getCoordYs() {
		List<Integer> coord = new ArrayList<Integer>(trees.size());
		for (int i = 0; i < coord.size(); i++) {
			coord.add(trees.get(i).getCoord().y());
		}
		return coord.toArray(new Integer[coord.size()]);
	}
	
	public Coord[] getCoords() {
		List<Coord> coord = new ArrayList<Coord>(trees.size());
		for (int i = 0; i < coord.size(); i++) {
			coord.add(trees.get(i).getCoord());
		}
		return coord.toArray(new Coord[coord.size()]);
	}
	
	/* translate colours into tile values
	 * grass  = 00FF00
	 * flower = FFFF00
	 * rock   = 7F7F00
	 */
	public Tile getTile(int x, int y) {
		switch (tiles[x+y*width]) {
			case 0xFF7F7F00: return Tile.solid;
			case 0xFF0000FF: return Tile.water;
			default: return Tile.voidTile;
		}
	}
	
	protected void populate() {
		ttiles = new Tile[tiles.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				switch (tiles[x+y*width]) {
				case 0xFF7F7F00: ttiles[x+y*width] = Tile.solid; break;
				case 0xFF0000FF: ttiles[x+y*width] = Tile.water; break;
				case 0xFF700000: trees.add(new Tree(new Coord(x, y), 
											new Rectangle(x, y+1, 2, 1)));
				default: ttiles[x+y*width] = Tile.voidTile;
				}
			}
		}
	}
	
	public static Tile[] findTouching(int x, int y, int size) {
		int x1 = TileCoordinate.scale(x);
		int y1 = TileCoordinate.scale(y);
		return new Tile[]{ttiles[x1+y1*width], ttiles[(x1+1)+y1*width], 
				ttiles[x1+(y1+1)*width], ttiles[(x1+1)+(y1+1)*width]};
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void addEntity(Entity e) {
		getEntities().add(e);
	}
	
	public Tree getTree(int i) {
		return trees.get(i);
	}
	
	public Tree getTree(Coord key) {
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getCoord().equals(key))
				return trees.get(i);
		}
		return null;
	}
	
	public Tree getTree(Rectangle rect) {
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getRect().equals(rect))
				return trees.get(i);
		}
		return null;
	}
	
	public Tree[] getTrees() {
		Tree[] treeArray = new Tree[trees.size()];
		for (int i = 0; i < trees.size(); i++) {
			treeArray[i] = trees.get(i);
		}
		return treeArray;
	}
	
	public Rectangle[] getTreeRects() {
		Rectangle[] treeArray = new Rectangle[trees.size()];
		for (int i = 0; i < trees.size(); i++) {
			treeArray[i] = trees.get(i).getRect();
		}
		return treeArray;
	}
	
	public int treeLength() {
		return trees.size();
	}
	
	public Tile findTileAt(int a, int b) {
		a = TileCoordinate.scale(a);
		b = TileCoordinate.scale(b);
		return getTile(a, b);
	}
	
	public void removePanel(Coord key) {
		trees.remove(getTree(key));
	}

	public void removeOnlinePlayer(String username) {
		int i = 0;
		for (Entity e : getEntities()) {
			if (e instanceof OnlinePlayer && ((OnlinePlayer)e).username().equals(username)) {
				break;
			}
			i++;
		}
		getEntities().remove(i);
	}
	
	private int getOnlinePlayerIndex(String usnm) {
		int index = 0;
		for (Entity e: getEntities()) {
			if (e instanceof OnlinePlayer && ((OnlinePlayer)e).username().equals(usnm)) {
				break;
			}
			index++;
		}
		return index;
	}
	
	public void movePlayer(String username, int x, int y) {
		//TODO this is broken
		int xa = 0, ya = 0;
		OnlinePlayer p = (OnlinePlayer) getEntities().get(getOnlinePlayerIndex(username));
		if (y < p.y()) ya--;
		if (y > p.y()) ya++;
		if (x < p.x()) xa--;
		if (x > p.x()) xa++;
		p.move(xa, ya);
		if (x != p.x() || y != p.y()) p.teleport(x, y);
	}
	
	public synchronized List<Entity> getEntities() {
		return this.entities;
	}
	
	public int getSpawnX() {
		return spawnX;
	}
	
	public int getSpawnY() {
		return spawnY;
	}

}
