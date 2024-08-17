package net.minecraft.protocol;

import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import net.minecraft.protocol.alpha.Packet16BlockItemSwitch;
import net.minecraft.protocol.alpha.Packet17AddToInventory;
import net.minecraft.protocol.alpha.Packet1Login;
import net.minecraft.protocol.alpha.Packet1Login2;
import net.minecraft.protocol.alpha.Packet21PickupSpawn;
import net.minecraft.protocol.alpha.Packet23VehicleSpawn;
import net.minecraft.protocol.alpha.Packet24MobSpawn;
import net.minecraft.protocol.alpha.Packet59ComplexEntity;
import net.minecraft.protocol.alpha.Packet5PlayerInventory;
import net.minecraft.protocol.alpha.Packet8;
import net.minecraft.protocol.alpha.Packet9;
import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityFish;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.TileEntity;
import net.minecraft.src.WorldClient;

public class NetHandlerAlpha {
	public static NetClientHandler netHandler;

	public static void handle(Packet p, NetClientHandler h, Minecraft mc) {
		netHandler = h;
		if(p instanceof Packet1Login) {
			handleLogin((Packet1Login)p, h, mc);
		}

		if(p instanceof Packet1Login2) {
			handleLogin2((Packet1Login2)p, h, mc);
		}

		if(p instanceof Packet5PlayerInventory) {
			handlePlayerInventory((Packet5PlayerInventory)p, h, mc);
		}

		if(p instanceof Packet8) {
			handlePacket8((Packet8)p, h, mc);
		}

		if(p instanceof Packet9) {
			handlePacket9((Packet9)p, h, mc);
		}

		if(p instanceof Packet16BlockItemSwitch) {
			handleBlockItemSwitch((Packet16BlockItemSwitch)p, h, mc);
		}

		if(p instanceof Packet17AddToInventory) {
			handleAddToInventory((Packet17AddToInventory)p, h, mc);
		}

		if(p instanceof Packet21PickupSpawn) {
			handlePickupSpawn((Packet21PickupSpawn)p, h, mc);
		}

		if(p instanceof Packet23VehicleSpawn) {
			handleVehicleSpawn((Packet23VehicleSpawn)p, h, mc);
		}

		if(p instanceof Packet24MobSpawn) {
			handleMobSpawn((Packet24MobSpawn)p, h, mc);
		}

		if(p instanceof Packet59ComplexEntity) {
			handleComplexEntity((Packet59ComplexEntity)p, h, mc);
		}

	}

	public static void handleLogin2(Packet1Login2 packet, NetClientHandler handler, Minecraft mc) {
		mc.playerController = new PlayerControllerMP(mc, handler);
		handler.worldClient = new WorldClient(handler, 0L, 0);
		handler.worldClient.multiplayerWorld = true;
		mc.changeWorld1(handler.worldClient);
		mc.thePlayer.dimension = 0;
		mc.displayGuiScreen(new GuiDownloadTerrain(handler));
	}

	public static void handleLogin(Packet1Login packet, NetClientHandler handler, Minecraft mc) {
		mc.playerController = new PlayerControllerMP(mc, handler);
		handler.worldClient = new WorldClient(handler, packet.mapSeed, packet.dimension);
		handler.worldClient.multiplayerWorld = true;
		mc.changeWorld1(handler.worldClient);
		mc.thePlayer.dimension = packet.dimension;
		mc.displayGuiScreen(new GuiDownloadTerrain(handler));
		mc.thePlayer.entityId = packet.protocolVersion;
	}

	public static void handlePlayerInventory(Packet5PlayerInventory packet, NetClientHandler handler, Minecraft mc) {
		EntityPlayerSP entityplayersp = mc.thePlayer;
		if(packet.type == -1) {
			entityplayersp.inventory.mainInventory = packet.stacks;
		}

		if(packet.type == -2) {
			((ContainerPlayer)entityplayersp.craftingInventory).craftMatrix.stackList = packet.stacks;
		}

		if(packet.type == -3) {
			entityplayersp.inventory.armorInventory = packet.stacks;
		}

	}

	public static void handlePacket8(Packet8 packet, NetClientHandler handler, Minecraft mc) {
		mc.thePlayer.setHealth(packet.healthMP);
	}

	public static void handlePacket9(Packet9 packet, NetClientHandler handler, Minecraft mc) {
		mc.respawn(true, 0);
	}

	public static void handleBlockItemSwitch(Packet16BlockItemSwitch packet, NetClientHandler handler, Minecraft mc) {
		Entity entity = handler.getEntityByID(packet.unused);
		if(entity != null) {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int i = packet.id;
			if(i == 0) {
				entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
			} else {
				entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = new ItemStack(i, 1, 0);
			}

		}
	}

	public static void handleAddToInventory(Packet17AddToInventory packet, NetClientHandler handler, Minecraft mc) {
		mc.thePlayer.inventory.addItemStackToInventory(new ItemStack(packet.id, packet.count, packet.durability));
	}

	public static void handlePickupSpawn(Packet21PickupSpawn packet, NetClientHandler handler, Minecraft mc) {
		double d = (double)packet.xPosition / 32.0D;
		double d1 = (double)packet.yPosition / 32.0D;
		double d2 = (double)packet.zPosition / 32.0D;
		EntityItem entityitem = new EntityItem(handler.worldClient, d, d1, d2, new ItemStack(packet.itemId, packet.count, 0));
		entityitem.motionX = (double)packet.rotation / 128.0D;
		entityitem.motionY = (double)packet.pitch / 128.0D;
		entityitem.motionZ = (double)packet.roll / 128.0D;
		entityitem.serverPosX = packet.xPosition;
		entityitem.serverPosY = packet.yPosition;
		entityitem.serverPosZ = packet.zPosition;
		handler.worldClient.func_712_a(packet.entityId, entityitem);
	}

	public static void handleVehicleSpawn(Packet23VehicleSpawn packet, NetClientHandler handler, Minecraft mc) {
		double d = (double)packet.xPosition / 32.0D;
		double d1 = (double)packet.yPosition / 32.0D;
		double d2 = (double)packet.zPosition / 32.0D;
		Object obj = null;
		if(packet.type == 10) {
			obj = new EntityMinecart(handler.worldClient, d, d1, d2, 0);
		}

		if(packet.type == 11) {
			obj = new EntityMinecart(handler.worldClient, d, d1, d2, 1);
		}

		if(packet.type == 12) {
			obj = new EntityMinecart(handler.worldClient, d, d1, d2, 2);
		}

		if(packet.type == 90) {
			obj = new EntityFish(handler.worldClient, d, d1, d2);
		}

		if(packet.type == 60) {
			obj = new EntityArrow(handler.worldClient, d, d1, d2);
		}

		if(packet.type == 61) {
			obj = new EntitySnowball(handler.worldClient, d, d1, d2);
		}

		if(packet.type == 1) {
			obj = new EntityBoat(handler.worldClient, d, d1, d2);
		}

		if(packet.type == 50) {
			obj = new EntityTNTPrimed(handler.worldClient, d, d1, d2);
		}

		if(obj != null) {
			((Entity)obj).serverPosX = packet.xPosition;
			((Entity)obj).serverPosY = packet.yPosition;
			((Entity)obj).serverPosZ = packet.zPosition;
			((Entity)obj).rotationYaw = 0.0F;
			((Entity)obj).rotationPitch = 0.0F;
			((Entity)obj).entityId = packet.entityId;
			handler.worldClient.func_712_a(packet.entityId, (Entity)obj);
		}

	}

	public static void handleMobSpawn(Packet24MobSpawn packet, NetClientHandler handler, Minecraft mc) {
		double d = (double)packet.xPosition / 32.0D;
		double d1 = (double)packet.yPosition / 32.0D;
		double d2 = (double)packet.zPosition / 32.0D;
		float f = (float)(packet.yaw * 360) / 256.0F;
		float f1 = (float)(packet.pitch * 360) / 256.0F;
		byte t = VersionManager.version().protocol() == 2 && packet.type == 91 ? 93 : packet.type;
		EntityLiving entityliving = (EntityLiving)EntityList.createEntity(t, mc.theWorld);
		entityliving.serverPosX = packet.xPosition;
		entityliving.serverPosY = packet.yPosition;
		entityliving.serverPosZ = packet.zPosition;
		entityliving.entityId = packet.entityId;
		entityliving.setPositionAndRotation(d, d1, d2, f, f1);
		entityliving.isMultiplayerEntity = true;
		handler.worldClient.func_712_a(packet.entityId, entityliving);
	}

	public static void handleComplexEntity(Packet59ComplexEntity packet, NetClientHandler handler, Minecraft mc) {
		if(packet.entityNBT.getInteger("x") == packet.xPosition) {
			if(packet.entityNBT.getInteger("y") == packet.yPosition) {
				if(packet.entityNBT.getInteger("z") == packet.zPosition) {
					TileEntity tileentity = handler.worldClient.getBlockTileEntity(packet.xPosition, packet.yPosition, packet.zPosition);
					if(tileentity != null) {
						try {
							tileentity.readFromNBT(packet.entityNBT);
						} catch (Exception var5) {
						}

						handler.worldClient.markBlocksDirty(packet.xPosition, packet.yPosition, packet.zPosition, packet.xPosition, packet.yPosition, packet.zPosition);
					}

				}
			}
		}
	}
}
