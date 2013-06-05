package uk.co.gossfunkel.citadelserver.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpawnLevel extends Level {
	
	// -------------------- variables -----------------------------------------

	// -------------------- constructors --------------------------------------
	
	public SpawnLevel(String path) {
		super(path);
		spawnX = 112;
		spawnY = 50;
	}

	// -------------------- methods -------------------------------------------
	
	protected void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(
					SpawnLevel.class.getResource(path));
			
			width = image.getWidth();
			height = image.getHeight();
			tiles = new int[width*height];
			
			image.getRGB(0,  0, width, height, tiles, 0, width);
		} catch (IOException e) {
			System.err.println("Could not load level file! Info:");
			e.printStackTrace();
		}
	}
	
	protected void generateLevel() {
	}
	
	/*@Override
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		// cornerpins
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.getWidth() + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.getHeight() + 16) >> 4;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				if (x+y*width < 0 || x+y*w >= width*width) { 
					Tile.voidTile.render(x, y, screen);
					continue;
				}
				tiles[x+y*width].render(x, y, screen);
			} // end x for
		} // end y for
	}*/

}
