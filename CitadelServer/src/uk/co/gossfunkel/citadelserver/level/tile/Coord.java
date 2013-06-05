package uk.co.gossfunkel.citadelserver.level.tile;

public class Coord {
    int x;
    int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(float x2, float y2) {
        this.x = (int) x2;
        this.y = (int) y2;
	}

	public boolean equals(Object o) {
        Coord c = (Coord) o;
        return c.x == x && c.y == y;
    }

    public int hashCode() {
        return new Integer(x + "0" + y);
    }
    
    public int x() {
    	return x;
    }
    
    public int y() {
    	return y;
    }
    
    public int xc() {
    	return x+1;
    }
    
    public int yc() {
    	return y+1;
    }
}