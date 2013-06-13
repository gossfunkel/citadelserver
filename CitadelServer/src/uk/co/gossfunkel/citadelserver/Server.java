package uk.co.gossfunkel.citadelserver;

import java.awt.Dimension;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.co.gossfunkel.citadelserver.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadelserver.entity.settlement.ConstructionSettlement;
import uk.co.gossfunkel.citadelserver.net.packets.Packet;
import uk.co.gossfunkel.citadelserver.net.packets.Packet.PacketTypes;
import uk.co.gossfunkel.citadelserver.net.packets.Packet00Login;
import uk.co.gossfunkel.citadelserver.net.packets.Packet01Disconnect;
import uk.co.gossfunkel.citadelserver.net.packets.Packet02Move;
import uk.co.gossfunkel.citadelserver.net.packets.Packet03Speech;
import uk.co.gossfunkel.citadelserver.net.packets.Packet04Settlement;

/* The game's main running class
 * 
 */
public class Server extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	// -------------------- variables -----------------------------------------
	
	public static String title = "citadelserver";
	
	private static Game game;
	private static Thread gameThread;
	
	// screen dimensions (16:9) etc
	private static int width = 1024;
	private static int height = (width / 16*9);
	
	// multithreading stuff
	private Thread thread;
	
	// loop stuff
	private boolean running = false;
	
	// UI stuff
	private JLabel one;
	private JTextField txtOne;
	private JButton submitTxtOne;
	private JTextArea outArea;
	private JScrollPane outScroll;
	
	// net stuff
	private DatagramSocket socket;
	
	private List<OnlinePlayer> connectedPlayers;
	private static List<String> speech;
	
	// -------------------- constructors --------------------------------------
	
	public Server() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);	
		setTitle(title);
		getContentPane().setLayout(null);
		
		one = new JLabel("Game not running");
        one.setBounds(10, 10, 90, 21);
        add(one);

        txtOne = new JTextField();
        txtOne.setBounds(105, 10, 90, 21);
        add(txtOne);
        
        submitTxtOne = new JButton("send");
        submitTxtOne.setBounds(200, 10, 40, 21);
        
        outScroll = new JScrollPane();
        outArea = new JTextArea(5, 20);
        outArea.setEditable(false);
        outScroll.setViewportView(outArea);
        
        add(outScroll);
        add(outArea);
		
		pack();
		validate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// -------------------- methods -------------------------------------------
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
	
	public synchronized void start() {
		if (running) return;
		game = new Game(this);
		running = true;

		thread = new Thread(this, "Server");
		thread.start();

		gameThread = new Thread(game, "Game");
		gameThread.start();

		try {
			this.socket = new DatagramSocket(1042);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		speech = new ArrayList<String>(30);
		connectedPlayers = new ArrayList<OnlinePlayer>();
	}
	
	public void run() {
		requestFocus();
		
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
				//TODO find out why this throws
				e.printStackTrace();
				//System.out.println(socket.getLocalPort());
			}
			
			try {
				parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			} catch (UnknownHostException e) {
				System.err.println(e);
			}
			String message = new String(packet.getData());
			//System.out.println("CLIENT> " + new String(packet.getData()));
			if (message.trim().equalsIgnoreCase("ping"))
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
		}
	}
	
	public synchronized void stop() {
		running = false;
		dispose();
		if (!socket.isClosed()) socket.close();
		try {
			game.exit();
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
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.username() + " has connected.");
			OnlinePlayer player = new OnlinePlayer(packet.x(), packet.y(), game, 
						packet.username(), address, port, 
						game.getLevel());
			System.out.println(player);
			addConnection(player, packet);
			break;
		case DISCONNECT: // remove player from connectedPlayers and game
			Packet01Disconnect packet1 = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet1.username() + " has left.");
			removeConnection(packet1);
			break;
		case MOVE: // move the player
			Packet02Move packet2 = new Packet02Move(data);
			handleMovement(packet2);
			packet2.writeData(this);
			break;
		case SAY:
			say(new Packet03Speech(data));
			break;
		case SETTLEMENT:
			construct(new Packet04Settlement(data));
		case INVALID:
		default: //TODO complain about unrecognised format
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
	
	public void setOne(String text) {
		one.setText(text);
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
		outArea.append(str);
	}

}
