package uk.co.gossfunkel.citadelserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadelserver.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadelserver.entity.settlement.ConstructionSettlement;
import uk.co.gossfunkel.citadelserver.net.packets.Packet;
import uk.co.gossfunkel.citadelserver.net.packets.Packet.PacketTypes;
import uk.co.gossfunkel.citadelserver.net.packets.Packet00Login;
import uk.co.gossfunkel.citadelserver.net.packets.Packet01Disconnect;
import uk.co.gossfunkel.citadelserver.net.packets.Packet02Move;
import uk.co.gossfunkel.citadelserver.net.packets.Packet03Speech;
import uk.co.gossfunkel.citadelserver.net.packets.Packet04Settlement;
import uk.co.gossfunkel.citadelserver.net.packets.Packet10LoginResponse;
import uk.co.gossfunkel.citadelserver.net.packets.PacketXXInvalid;

/* The game's main running class
 * 
 */
public class Server implements Runnable {
	
	// -------------------- variables -----------------------------------------
	
	private Game game;
	private GUI gui;
	
	// multithreading stuff
	private Thread thread;
	
	// loop stuff
	private boolean running = false;
	
	// net stuff
	private DatagramSocket socket;
	
	private List<OnlinePlayer> connectedPlayers;
	private static List<String> speech;
	
	// -------------------- constructors --------------------------------------
	
	public Server() {
		try {
			this.socket = new DatagramSocket(1042);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		speech = new ArrayList<String>(30);
		connectedPlayers = new ArrayList<OnlinePlayer>();
	}
	
	// -------------------- methods -------------------------------------------
	
	public void addGame(Game game) {
		this.game = game;
	}
	
	public void addGui(GUI gui) {
		this.gui = gui;
	}
	
	public void run() {
		running = true;
		try {
			// sleep to stop socket receiving null packets
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.out.println("interupted");
		}
		while (running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			} catch (Exception e) {
				System.out.println("Socket error: " + e);
			} 
				// if server takes too long to start up,
				//   can result in a nullpointer
			String message = new String(packet.getData());
			if (message.trim().equalsIgnoreCase("ping"))
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
			else {
				try {
					parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
				} catch (UnknownHostException e) {
					System.err.println(e);
				}
			}
			//System.out.println("CLIENT> " + message);
		}
	}
	
	public synchronized void stop() {
		running = false;
		if (!socket.isClosed()) socket.close();
		try {
			new Thread(){
	            public void run() {
	                System.exit(0);
	            }
			}.start();
			thread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void say(String usnm, String str) {
		speech.add(str);
		new Packet03Speech(usnm, str).writeData(this);
		if (speech.size() > 29) speech.remove(0);
	}

	private void say(Packet03Speech pack) {
		pack.writeData(this);
		postOutput(pack.str());
		speech.add(pack.str());
		if (speech.size() > 29) speech.remove(0);
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) throws UnknownHostException {
		String message = new String(data).trim();
		PacketTypes type;
		try {
			if (message.length() < 2) type = PacketTypes.INVALID;
			else type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		} catch (NumberFormatException e) {
			type = PacketTypes.INVALID;
		}
		switch (type) {
		case LOGIN: // log new player in from host to new OnlinePlayer
			Packet00Login packet = new Packet00Login(data);
			postOutput("[" + address.getHostAddress() + ":" + port + 
					"] " + packet.username() + " has connected.");
			OnlinePlayer player = new OnlinePlayer(packet.x(), packet.y(), game, 
						packet.username(), address, port, 
						game.getLevel());
			game.addPlayer(player);
			addConnection(player, packet);
			/* response is not being seen
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			System.out.println("address: " + port);
			Packet10LoginResponse response = new Packet10LoginResponse(
				game.getPlayers(), game.getSettlements(), address, port);
			response.writeData(this);
			break;
		case DISCONNECT: // remove player from connectedPlayers and game
			Packet01Disconnect packet1 = new Packet01Disconnect(data);
			postOutput("[" + address.getHostAddress() + ":" + port + 
								"] " + packet1.username() + " has left.");
			removeConnection(packet1);
			break;
		case MOVE: // move the player
			Packet02Move packet2 = new Packet02Move(data);
			handleMovement(packet2);
			packet2.writeData(this);
			break;
		case SAY:
			Packet03Speech packet3 = new Packet03Speech(data);
			say(packet3);
			System.out.println("saying " + packet3.str());
			break;
		case SETTLEMENT:
			construct(new Packet04Settlement(data));
		case INVALID:
		default: //TODO complain about unrecognised format
			postOutput("INVALID PACKET: " + new PacketXXInvalid(data).str());
			break;
		}
	}

	private void removeConnection(Packet01Disconnect packet) {
		connectedPlayers.remove(packet.username());
	}

	private void handleMovement(Packet02Move packet) {
		String usnm = packet.username();
		if (getOnlinePlayer(usnm) != null) {
			// player exists
			getOnlinePlayer(usnm).move(packet.x(), packet.y());
			packet.writeData(this);
		} else {
			//TODO deal with it.
			System.err.println("shit");
		}
	}

	public void addConnection(OnlinePlayer player, Packet00Login packet) {
		if (player != null) {
			OnlinePlayer p = getOnlinePlayer(player.username());
			if (p != null) {
				// if the player has already connected, update
				if (p.ip == null) p.ip = player.ip;
				if (p.port == -1) p.port = player.port;
				System.out.println("p " +p);
			} else {
				connectedPlayers.add(player);
				game.getLevel().addEntity(player);
				System.out.println("player " + player);
			}
		}
	}
	
	private OnlinePlayer getOnlinePlayer(String usnm) {
		for (OnlinePlayer p : connectedPlayers) {
			if (p != null) {
				if (usnm.equalsIgnoreCase(p.username())) {
					return p;
				}
			}
		}
		return null;
	}
	
	/*
	private int getOnlinePlayerIndex(String usnm) {
		int i = 0;
		for (OnlinePlayer p : connectedPlayers) {
			if (p != null) {
				if (usnm.equalsIgnoreCase(p.username())){
					break;
				}
			}
			i++;
		}
		return i;
	}
	*/

	public void sendData(byte[] data, InetAddress ip, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		// broadcast
		for (OnlinePlayer p : connectedPlayers) {
			if (p != null) {
				sendData(data, p.ip, p.port);
			}
		}
	}
	
	private void construct(Packet04Settlement p) {
		ConstructionSettlement gensett = 
				new ConstructionSettlement(game, p.x(), p.y(), p.username());
		game.addConSett(gensett);
	}
	
	/* send output to outputBox
	 * 
	 */
	public void postOutput(String str) {
		gui.add(str);
	}
	
	public void parseInput(String str) {
		str.trim();
		//if (!str.equals("")) postOutput(str);
		if (str.startsWith("/")) {
			// parse as command
			str.toLowerCase();
			if (str.indexOf(" ") > 0) {
				switch (str.substring(1, str.indexOf(" "))) {
				case "say":
					Packet03Speech pack = new Packet03Speech("SERVER", str.substring(4));
					say(pack);
					break;
				case "disconnect": //TODO disconnect player stated
					postOutput("disconnecting " + str.substring(str.indexOf(" ")));
					break;
				case "teleport":
				case	 "tp": //TODO teleport stated player
					postOutput("teleporting " + str.substring(str.indexOf(" ")));
					break;
				case "location":
					String player = str.substring(str.indexOf(" ")).trim();
					String location = game.getPlayerLocation(player);
					if (location != null)
						postOutput(player + " is at " + location + ".");
					else 
						postOutput("Player " + player + " could not be found!");
					break;
				default: 
					postOutput("Command not recognised: " + 
								str.substring(1, str.indexOf(" "))); 
					break;
				}
			} else {
				switch (str.substring(1)) {
				case "exit": //TODO quit cleanly
					postOutput("Server going down.");
					stop();
					break;
				default: postOutput("Command not recognised: " + 
										str.substring(1)); break;
				}
			}
		} else {
			Packet03Speech pack = new Packet03Speech("SERVER", str);
			say(pack);
		}
	}
	
	public void setOne(String str) {
		gui.setOne(str);
	}

}
