package net.minecraft.protocol.beta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet9Respawn extends Packet {
	public void processPacket(NetHandler nethandler) {
		nethandler.handleBetaOverride(this);
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
	}

	public int getPacketSize() {
		return 0;
	}
}
