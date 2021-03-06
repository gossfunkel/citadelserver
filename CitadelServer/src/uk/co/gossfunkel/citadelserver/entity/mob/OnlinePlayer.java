package uk.co.gossfunkel.citadelserver.entity.mob;

import java.net.InetAddress;
import java.util.ArrayList;

import uk.co.gossfunkel.citadelserver.Game;
import uk.co.gossfunkel.citadelserver.entity.Entity;
import uk.co.gossfunkel.citadelserver.level.Level;
import uk.co.gossfunkel.citadelserver.level.tile.Tile;

public class OnlinePlayer extends Mob {

	protected Game game;
	protected final String username;
	int xa, ya;
	boolean flip;
	boolean speaking = false;
	boolean mining = false;
	boolean shooting = false;
	String saying;
	int fire = 10;
	public InetAddress ip;
	public int port;

	public OnlinePlayer(Game game, String username,
			InetAddress ip, int port, Level level) {
		this.game = game;
		this.level = level;
		this.username = username;
		nearEntities = new ArrayList<Entity>();
		this.ip = ip;
		this.port = port;
		
		init();
	}
	
	// spawn locale constructor
	public OnlinePlayer(int x, int y, Game game, String username, 
			InetAddress ip, int port, Level level) {
		this(game, username, ip, port, level);
		teleport(x, y); 
	}
	
	public void init() {
		level.addEntity(this);
		//if (game.server != null) game.server.addConnection((OnlinePlayer)player);
	}

	@Override
	public void update() {
		xa = 0;
		ya = 0;
		
		if (level.findTileAt(x+SIZE/2, y+SIZE/2).equals(Tile.water))
			swimming = true;
		else swimming = false;

		if (xa != 0 || ya != 0) { //-----
			move(xa, ya);
			moving = true;
		} else moving = false; //--------
		
		//TODO if (shooting) ;
	}
	
	public String username() {
		return username;
	}
	
	@Override
	public void move(int xa, int ya) {
		super.move(xa, ya);
	}
	
	public String toString() {
		String string = "";
		string += username();
		string += "; " + x + ", " + y;
		return string;
	}

	public String getRaw() {
		return (x + "_" + y + "_" + username);
	}

	public String getLocation() {
		return ("x:" + x + ", y:" + y);
	}
	
}
