package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet16BlockItemSwitch extends Packet {
	public int unused;
	public int id;

	public Packet16BlockItemSwitch() {
	}

	public Packet16BlockItemSwitch(int i, int j) {
		this.unused = i;
		this.id = j;
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.unused = datainputstream.readInt();
		this.id = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(this.unused);
		dataoutputstream.writeShort(this.id);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 6;
	}
}
