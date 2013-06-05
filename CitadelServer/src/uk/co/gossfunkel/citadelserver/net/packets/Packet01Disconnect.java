package uk.co.gossfunkel.citadelserver.net.packets;

import uk.co.gossfunkel.citadelserver.Server;

public class Packet01Disconnect extends Packet {
	
	private String username;
	
	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}
	
	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("01" + username).getBytes();
	}
	
	public String username() {
		return username;
	}
	
	@Override
	public String toString() {
		return ("02" + username);
	}

}
