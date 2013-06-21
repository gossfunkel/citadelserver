package uk.co.gossfunkel.citadelserver.net.packets;

import uk.co.gossfunkel.citadelserver.Server;

public abstract class Packet {

	public static enum PacketTypes {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), SAY(03), 
			SETTLEMENT(04), LOGINRESPONSE(10);
		
		private int packetId;
		private PacketTypes(int pid) {
			packetId = pid;
		}
		
		public int getId() {
			return packetId;
		}
	}
	
	public byte packetId;
	
	public Packet(int pid) {
		packetId = (byte) pid;
	}
	
	public abstract void writeData(Server server);
	public abstract byte[] getData();
	
	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public static PacketTypes lookupPacket(String pid) {
		try {
			return lookupPacket(Integer.parseInt(pid));
		} catch (NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
	
	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return PacketTypes.INVALID;
	}
	
	@Override
	public String toString() {
		return "PACKET";
	}
	
}
