package uk.co.gossfunkel.citadelserver.net.packets;

import uk.co.gossfunkel.citadelserver.Server;

public class Packet03Speech extends Packet {
	
	private String username;
	private String str;
	
	public Packet03Speech(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		username = dataArray[0].trim();
		str = dataArray[1];
		int i = dataArray.length -2;
		while (i > 2) {
			str += dataArray[i+2];
			i--;
		}
	}
	
	public Packet03Speech(String username, String str) {
		super(03);
		this.username = username.trim();
		this.str = str;
	}
	
	public String username() {
		return username;
	}
	
	public String str() {
		return str;
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return (username + "," + str).getBytes();
	}
	
	@Override
	public String toString() {
		return ("03" + username + "," + str);
	}

}
