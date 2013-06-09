package uk.co.gossfunkel.citadelserver.net.packets;

import uk.co.gossfunkel.citadelserver.Server;

public class Packet04Settlement extends Packet {
	
	private String username;
	private int x, y;
	
	public Packet04Settlement(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");
		username = dataArray[0].trim();
		x = Integer.parseInt(dataArray[1].trim());
		y = Integer.parseInt(dataArray[2].trim());
	}
	
	public Packet04Settlement(String username, int x, int y) {
		super(02);
		this.username = username.trim();
		this.x = x;
		this.y = y;
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("04" + username + "," + x + "," + y).getBytes();
	}
	
	public String username() {
		return username;
	} 
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	@Override
	public String toString() {
		return ("04" + username + "," + x + "," + y);
	}
	
	

}
