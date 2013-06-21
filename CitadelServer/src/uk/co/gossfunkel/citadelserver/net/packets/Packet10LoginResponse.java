package uk.co.gossfunkel.citadelserver.net.packets;

import java.net.InetAddress;
import java.util.ArrayList;

import uk.co.gossfunkel.citadelserver.Server;
import uk.co.gossfunkel.citadelserver.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadelserver.entity.settlement.Settlement;

public class Packet10LoginResponse extends Packet {
	
	private ArrayList<OnlinePlayer> players;
	private ArrayList<Settlement> settlements;
	private InetAddress ip;
	private int port;
	
	public Packet10LoginResponse(ArrayList<OnlinePlayer> players, 
			ArrayList<Settlement> settlements, InetAddress ip, int port) {
		super(10);
		this.players = players;
		this.settlements = settlements;
		this.ip = ip;
		this.port = port;
		System.out.println("making login response");
	}

	@Override
	public void writeData(Server server) {
		System.out.println("sending login response");
		server.sendData(getData(), ip, port);
	}

	@Override
	public byte[] getData() {
		return ("00" + "," + players.size() + "," + players() + "," +
				settlements.size() + "," + settlements()).getBytes();
	}
	
	private String players() {
		String str = " ";
		for (int i = 0; i < players.size(); i++) {
			str += players.get(i).getRaw() + "+";
		}
		return str;
	}
	
	private String settlements() {
		String str = " ";
		for (int i = 0; i < settlements.size(); i++) {
			str += settlements.get(i).getRaw() + "+";
		}
		return str;
	}
	
	@Override
	public String toString() {
		return ("00" + "," + players.size() + "," + settlements.size());
	}


}
