package net.minecraft.protocol;

import net.minecraft.client.Minecraft;
import net.minecraft.protocol.beta.Packet1Login;
import net.minecraft.protocol.beta.Packet23VehicleSpawn;
import net.minecraft.protocol.beta.Packet9Respawn;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityFish;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.StatList;
import net.minecraft.src.WorldClient;

public class NetHandlerBeta {
	public static NetClientHandler netHandler;

	public static void handle(Packet p, NetClientHandler h, Minecraft mc) {
		netHandler = h;
		if(p instanceof Packet1Login) {
			handleLogin((Packet1Login)p, h, mc);
		}

		if(p instanceof Packet9Respawn) {
			handleRespawn((Packet9Respawn)p, h, mc);
		}

		if(p instanceof Packet23VehicleSpawn) {
			handleVehicleSpawn((Packet23VehicleSpawn)p, h, mc);
		}

	}

	public static void handleLogin(Packet1Login packet1login, NetClientHandler h, Minecraft mc) {
		mc.playerController = new PlayerControllerMP(mc, h);
		mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
		h.worldClient = new WorldClient(h, packet1login.mapSeed, packet1login.dimension);
		h.worldClient.multiplayerWorld = true;
		mc.changeWorld1(h.worldClient);
		mc.displayGuiScreen(new GuiDownloadTerrain(h));
		mc.thePlayer.entityId = packet1login.protocolVersion;
	}

	public static void handleRespawn(Packet9Respawn packet9respawn, NetClientHandler h, Minecraft mc) {
		mc.respawn(true, 0);
	}

	public static void handleVehicleSpawn(Packet23VehicleSpawn packet23vehiclespawn, NetClientHandler h, Minecraft mc) {
		double d = (double)packet23vehiclespawn.xPosition / 32.0D;
		double d1 = (double)packet23vehiclespawn.yPosition / 32.0D;
		double d2 = (double)packet23vehiclespawn.zPosition / 32.0D;
		Object obj = null;
		if(packet23vehiclespawn.type == 10) {
			obj = new EntityMinecart(h.worldClient, d, d1, d2, 0);
		}

		if(packet23vehiclespawn.type == 11) {
			obj = new EntityMinecart(h.worldClient, d, d1, d2, 1);
		}

		if(packet23vehiclespawn.type == 12) {
			obj = new EntityMinecart(h.worldClient, d, d1, d2, 2);
		}

		if(packet23vehiclespawn.type == 90) {
			obj = new EntityFish(h.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 60) {
			obj = new EntityArrow(h.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 61) {
			obj = new EntitySnowball(h.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 62) {
			obj = new EntityEgg(h.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 1) {
			obj = new EntityBoat(h.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 50) {
			obj = new EntityTNTPrimed(h.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 70) {
			obj = new EntityFallingSand(h.worldClient, d, d1, d2, Block.sand.blockID);
		}

		if(packet23vehiclespawn.type == 71) {
			obj = new EntityFallingSand(h.worldClient, d, d1, d2, Block.gravel.blockID);
		}

		if(obj != null) {
			((Entity)obj).serverPosX = packet23vehiclespawn.xPosition;
			((Entity)obj).serverPosY = packet23vehiclespawn.yPosition;
			((Entity)obj).serverPosZ = packet23vehiclespawn.zPosition;
			((Entity)obj).rotationYaw = 0.0F;
			((Entity)obj).rotationPitch = 0.0F;
			((Entity)obj).entityId = packet23vehiclespawn.entityId;
			h.worldClient.func_712_a(packet23vehiclespawn.entityId, (Entity)obj);
		}

	}
}
