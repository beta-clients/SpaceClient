package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet9 extends Packet {
	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public void readPacketData(DataInputStream datainputstream) {
	}

	public void writePacketData(DataOutputStream dataoutputstream) {
	}

	public int getPacketSize() {
		return 0;
	}
}
