package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet15Place extends Packet {
	public int id;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int direction;

	public Packet15Place() {
	}

	public Packet15Place(int i, int j, int k, int l, int i1) {
		this.id = i;
		this.xPosition = j;
		this.yPosition = k;
		this.zPosition = l;
		this.direction = i1;
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.id = datainputstream.readShort();
		this.xPosition = datainputstream.readInt();
		this.yPosition = datainputstream.read();
		this.zPosition = datainputstream.readInt();
		this.direction = datainputstream.read();
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeShort(this.id);
		dataoutputstream.writeInt(this.xPosition);
		dataoutputstream.write(this.yPosition);
		dataoutputstream.writeInt(this.zPosition);
		dataoutputstream.write(this.direction);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 12;
	}
}
