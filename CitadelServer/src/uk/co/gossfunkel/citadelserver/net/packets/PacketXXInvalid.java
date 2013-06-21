package uk.co.gossfunkel.citadelserver.net.packets;

import uk.co.gossfunkel.citadelserver.Server;

public class PacketXXInvalid extends Packet {
	
	byte[] data;
	String str;

	public PacketXXInvalid(byte[] data) {
		super(-1);
		this.data = data;
		str = data.toString();
	}

	@Override
	public void writeData(Server server) {
		System.err.println("Writing invalid packet!");
	}

	@Override
	public byte[] getData() {
		return data;
	}
	
	public String str() {
		return str;
	}

}
