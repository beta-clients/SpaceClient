package net.minecraft.protocol;

import java.util.Set;
import net.minecraft.src.Packet;

public class BetaProtocol extends AlphaProtocol {
	private Set<Boolean> clientList;
	private Set<Boolean> serverList;

	public BetaProtocol(int i) {
		super(i);
	}

	public void replacePacket(int i, boolean client, boolean server, Class class1) {
		Packet.replaceMapping(i, client, server, class1);
	}
}
