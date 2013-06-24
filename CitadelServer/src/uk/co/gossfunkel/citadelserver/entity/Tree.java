package uk.co.gossfunkel.citadelserver.entity;

import java.awt.Rectangle;

import uk.co.gossfunkel.citadelserver.level.tile.Coord;
import uk.co.gossfunkel.citadelserver.level.tile.Tile;
import uk.co.gossfunkel.citadelserver.level.Level;

public class Tree {
	
	private Coord coord;
	private Tile tile = Tile.solid;
	private Rectangle boundingBox;
	private Level level;

	private int brokenness = 0;
	
	public Tree(Coord coord, Rectangle bbox) {
		this.coord = coord;
		boundingBox = bbox;
	}
	
	public Coord getCoord() {
		return coord;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public Rectangle getRect() {
		return boundingBox;
	}

	public void damage() {
		System.out.println("brokenness: " + brokenness);
		brokenness++;
		if (brokenness > 100) {
			collapse();
		}
	}
	
	public void collapse() {
		// drop apple
		level.removePanel(coord);
	}

}
