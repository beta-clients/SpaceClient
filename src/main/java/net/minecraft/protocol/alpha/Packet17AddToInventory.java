package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet17AddToInventory extends Packet {
	public int id;
	public int count;
	public int durability;

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.id = datainputstream.readShort();
		this.count = datainputstream.readByte();
		this.durability = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeShort(this.id);
		dataoutputstream.writeByte(this.count);
		dataoutputstream.writeShort(this.durability);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
