package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet1Login extends Packet {
	public int protocolVersion;
	public String username;
	public String password;
	public long mapSeed;
	public byte dimension;

	public Packet1Login() {
	}

	public Packet1Login(String s, String s1, int i) {
		this.username = s;
		this.password = s1;
		this.protocolVersion = i;
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.protocolVersion = datainputstream.readInt();
		this.username = datainputstream.readUTF();
		this.password = datainputstream.readUTF();
		this.mapSeed = datainputstream.readLong();
		this.dimension = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(this.protocolVersion);
		dataoutputstream.writeUTF(this.username);
		dataoutputstream.writeUTF(this.password);
		dataoutputstream.writeLong(this.mapSeed);
		dataoutputstream.writeByte(this.dimension);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 4 + this.username.length() + this.password.length() + 4 + 5;
	}
}
