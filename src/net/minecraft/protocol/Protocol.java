package net.minecraft.protocol;

import net.minecraft.protocol.alpha.Packet15Place;
import net.minecraft.protocol.alpha.Packet16BlockItemSwitch;
import net.minecraft.protocol.alpha.Packet17AddToInventory;
import net.minecraft.protocol.alpha.Packet1Login2;
import net.minecraft.protocol.alpha.Packet21PickupSpawn;
import net.minecraft.protocol.alpha.Packet24MobSpawn;
import net.minecraft.protocol.alpha.Packet59ComplexEntity;
import net.minecraft.protocol.alpha.Packet5PlayerInventory;
import net.minecraft.protocol.alpha.Packet8;
import net.minecraft.protocol.alpha.Packet9;
import net.minecraft.protocol.beta.Packet102WindowClick;
import net.minecraft.protocol.beta.Packet1Login;
import net.minecraft.protocol.beta.Packet23VehicleSpawn;
import net.minecraft.protocol.beta.Packet9Respawn;
import net.minecraft.src.Packet;

public abstract class Protocol {
	public static final BetaProtocol BETA_14 = new BetaProtocol(14);
	public static final BetaProtocol BETA_13 = new BetaProtocol(13);
	public static final BetaProtocol BETA_11 = new BetaProtocol(11);
	public static final BetaProtocol BETA_10 = new BetaProtocol(10);
	public static final BetaProtocol BETA_9 = new BetaProtocol(9);
	public static final BetaProtocol BETA_8 = new BetaProtocol(8);
	public static final BetaProtocol BETA_7 = new BetaProtocol(7);
	public static final AlphaProtocol ALPHA_6 = new AlphaProtocol(6);
	public static final AlphaProtocol ALPHA_5 = new AlphaProtocol(5);
	public static final AlphaProtocol ALPHA_3 = new AlphaProtocol(3);
	public static final AlphaProtocol ALPHA_2 = new AlphaProtocol(2);

	public static void addPackets(Protocol protocol) {
		Packet.resetPacketList();
		if(protocol != BETA_14 && protocol != BETA_13) {
			if(protocol == BETA_11) {
				((BetaProtocol)protocol).replacePacket(9, true, true, Packet9Respawn.class);
				((BetaProtocol)protocol).replacePacket(23, true, false, Packet23VehicleSpawn.class);
			}

			if(protocol == BETA_10 || protocol == BETA_9 || protocol == BETA_8 || protocol == BETA_7) {
				protocol.replacePacket(9, Packet9Respawn.class);
				protocol.replacePacket(23, Packet23VehicleSpawn.class);
				protocol.replacePacket(1, Packet1Login.class);
				protocol.replacePacket(102, Packet102WindowClick.class);
			}

			if(protocol == ALPHA_6 || protocol == ALPHA_3 || protocol == ALPHA_5) {
				protocol.replacePacket(1, net.minecraft.protocol.alpha.Packet1Login.class);
				protocol.replacePacket(5, Packet5PlayerInventory.class);
				protocol.replacePacket(8, Packet8.class);
				protocol.replacePacket(9, Packet9.class);
				protocol.replacePacket(15, Packet15Place.class);
				protocol.replacePacket(16, Packet16BlockItemSwitch.class);
				protocol.replacePacket(17, Packet17AddToInventory.class);
				protocol.replacePacket(21, Packet21PickupSpawn.class);
				protocol.replacePacket(23, net.minecraft.protocol.alpha.Packet23VehicleSpawn.class);
				protocol.replacePacket(24, Packet24MobSpawn.class);
				protocol.replacePacket(59, Packet59ComplexEntity.class);
			}

			if(protocol == ALPHA_2) {
				protocol.replacePacket(1, Packet1Login2.class);
				protocol.replacePacket(5, Packet5PlayerInventory.class);
				protocol.replacePacket(8, Packet8.class);
				protocol.replacePacket(9, Packet9.class);
				protocol.replacePacket(15, Packet15Place.class);
				protocol.replacePacket(16, Packet16BlockItemSwitch.class);
				protocol.replacePacket(17, Packet17AddToInventory.class);
				protocol.replacePacket(21, Packet21PickupSpawn.class);
				protocol.replacePacket(23, net.minecraft.protocol.alpha.Packet23VehicleSpawn.class);
				protocol.replacePacket(24, Packet24MobSpawn.class);
				protocol.replacePacket(59, Packet59ComplexEntity.class);
			}

		}
	}

	public abstract int version();

	public abstract void replacePacket(int var1, Class var2);
}
