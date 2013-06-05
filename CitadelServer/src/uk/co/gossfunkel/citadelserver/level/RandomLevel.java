package uk.co.gossfunkel.citadelserver.level;

import java.util.Random;

//import uk.co.gossfunkel.citadel.graphics.Screen;

public class RandomLevel extends Level {

	// -------------------- variables -----------------------------------------
	
	private static final Random random = new Random();

	// -------------------- constructors --------------------------------------

	public RandomLevel(int width, int height) {
		super(width, height);
	}

	// -------------------- methods -------------------------------------------
	
	/* randomly generate a level
	 * 
	 */
	protected void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x+y*width] = makeRandom();
			} // end x for
		} // end y for
	} // end generateLevel
	
	private int makeRandom() {
		int num = random.nextInt(6);
		if (num == 1 || num == 2 || num == 3) {
			num = random.nextInt(4);
			if (num == 1 || num == 2 || num == 3) {
				num = random.nextInt(4);
				if (num == 1 || num == 2) {
					num = random.nextInt(4);
					if (num == 2) {
						num = random.nextInt(4);
					}
				}
			}
		}
		return num;
	}
	
}
