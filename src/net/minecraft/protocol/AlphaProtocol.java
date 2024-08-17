package net.minecraft.protocol;

import net.minecraft.src.Packet;

public class AlphaProtocol extends Protocol {
	private int version;

	public AlphaProtocol(int i) {
		this.version = i;
	}

	public int version() {
		return this.version;
	}

	public void replacePacket(int i, Class class1) {
		Packet.replaceMapping(i, class1);
	}
}
