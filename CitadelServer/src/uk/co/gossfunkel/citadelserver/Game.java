package uk.co.gossfunkel.citadelserver;

import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadelserver.entity.settlement.ConstructionSettlement;
import uk.co.gossfunkel.citadelserver.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadelserver.entity.settlement.Settlement;
import uk.co.gossfunkel.citadelserver.level.Level;
import uk.co.gossfunkel.citadelserver.level.SpawnLevel;
import uk.co.gossfunkel.citadelserver.level.TileCoordinate;
import uk.co.gossfunkel.citadelserver.level.tile.Tile;

public class Game implements Runnable {
	
	private boolean running = false;
	
	private Server server;
	
	private Timer timer;
	private Level level;
	
	private static List<Settlement> settlements;
	private static List<ConstructionSettlement> consettlements;
	private List<OnlinePlayer> players;
	private static List<Integer> settx;
	private static List<Integer> setty;
	
	// day stuff
	private int ticker = 0;
	private boolean day;
	private int hour;
	private int days;
	private int month = 0; 
	@SuppressWarnings("unused")
	private int year = 0;
	
	public Game(Server server) {
		this.server = server;
		
		settlements = new ArrayList<Settlement>();
		consettlements = new ArrayList<ConstructionSettlement>();
		players = new ArrayList<OnlinePlayer>();
		settx = new ArrayList<Integer>();
		setty = new ArrayList<Integer>();
		timer = new Timer();

		setLevel(new SpawnLevel("/garden.png"));
	}

	@Override
	public void run() {
		running = true;
		server.setOne("Game running");
		while (running) {
			timer.tick();
			if (System.currentTimeMillis() - getTimer().getSecond() > 1000) {
				// every second, add a second, print fps and mod title with fps
				getTimer().accumulateSecond();
				getTimer().resetTick();
				ticker++;
			}
			while (getTimer().getDelta() >= 1) {
				// every time delta goes greater than one, update and supertick
				update();
				getTimer().superTick();
			}
			if (ticker > 30) {
				ticker = 0;
				getTimer().hourTick();
			}
			
		}
	}

	public void stop() {
		running = false;
	}
	
	public void update() {
		//TODO update UI
		
		if (getTimer().getDay() != day) {
			day = getTimer().getDay();
		}
		if (getTimer().getDay()) {
			if (getTimer().getHour() != hour) {
				hour = getTimer().getHour();
				if (hour == 0) {
					// new day
					days++;
					for (Settlement s : settlements) {
						s.levelUp();
					}
				}
			} 
		} else {
			if (getTimer().getHour() != hour) {
				hour = getTimer().getHour();
			}
		}
		if (days == 31) {
			// new month
			month++;
			days = 0;
		}
		if (month == 12) {
			// new year
			year++;
			month = 0;
		}
		
		getLevel().update();
		for (int i = 0; i < settlements.size(); i++) {
			settlements.get(i).update();
		}
	}

	public void build(int xm, int ym, OnlinePlayer player) {
		if (settx.contains(TileCoordinate.scale(xm)) 
				&& setty.contains(TileCoordinate.scale(ym))) {
			// say to player("Square already filled");
		} else {
			if (level.getTile(TileCoordinate.scale(xm), 
					TileCoordinate.scale(ym)).equals(Tile.water) ||
					level.getTile(TileCoordinate.scale(xm), 
							TileCoordinate.scale(ym)).equals(Tile.solid)) {
				// say to player("Illegal position");
			} else {
				Settlement genset = new Settlement(xm, ym, player.username());
				settlements.add(genset);
				settx.add(genset.x());
				setty.add(genset.y());
			} // end inner else
		} // end outer else
	} // end build
	
	public void removeConSett(ConstructionSettlement cs) {
		consettlements.remove(cs);
	}
	
	public void addConSett(ConstructionSettlement cs) {
		consettlements.add(cs);
	}
	
	public void addSett(Settlement s) {
		settlements.add(s);
		settx.add(s.x());
		setty.add(s.y());
	}

	Level getLevel() {
		return level;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}

	public Timer getTimer() {
		return timer;
	}
	
	public ArrayList<OnlinePlayer> getPlayers() {
		return (ArrayList<OnlinePlayer>) players;
	}
	
	public String getPlayerLocation(String playerName) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).username().equals(playerName)) {
				return players.get(i).getLocation();
			}
		}
		return null;
	}
	
	public ArrayList<Settlement> getSettlements() {
		return (ArrayList<Settlement>) settlements;
	}
	
	public void addPlayer(OnlinePlayer player) {
		players.add(player);
	}

}
