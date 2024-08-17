package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import net.minecraft.protocol.NetHandlerAlpha;
import net.minecraft.protocol.NetHandlerBeta;
import net.minecraft.protocol.alpha.Packet1Login2;

public class NetClientHandler extends NetHandler {
	private boolean disconnected = false;
	public NetworkManager netManager;
	public String field_1209_a;
	private Minecraft mc;
	public WorldClient worldClient;
	private boolean field_1210_g = false;
	public MapStorage field_28118_b = new MapStorage((ISaveHandler)null);
	Random rand = new Random();

	public NetClientHandler(Minecraft minecraft, String s, int i) throws UnknownHostException, IOException {
		this.mc = minecraft;
		Socket socket = new Socket(InetAddress.getByName(s), i);
		this.netManager = new NetworkManager(socket, "Client", this);
	}

	public void processReadPackets() {
		if(!this.disconnected) {
			this.netManager.processReadPackets();
		}

		this.netManager.wakeThreads();
	}

	public void handleLogin(Packet1Login packet1login) {
		this.mc.playerController = new PlayerControllerMP(this.mc, this);
		this.mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
		this.worldClient = new WorldClient(this, packet1login.mapSeed, packet1login.dimension);
		this.worldClient.multiplayerWorld = true;
		this.mc.changeWorld1(this.worldClient);
		this.mc.thePlayer.dimension = packet1login.dimension;
		this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
		this.mc.thePlayer.entityId = packet1login.protocolVersion;
	}

	public void handlePickupSpawn(Packet21PickupSpawn packet21pickupspawn) {
		double d = (double)packet21pickupspawn.xPosition / 32.0D;
		double d1 = (double)packet21pickupspawn.yPosition / 32.0D;
		double d2 = (double)packet21pickupspawn.zPosition / 32.0D;
		EntityItem entityitem = new EntityItem(this.worldClient, d, d1, d2, new ItemStack(packet21pickupspawn.itemID, packet21pickupspawn.count, packet21pickupspawn.itemDamage));
		entityitem.motionX = (double)packet21pickupspawn.rotation / 128.0D;
		entityitem.motionY = (double)packet21pickupspawn.pitch / 128.0D;
		entityitem.motionZ = (double)packet21pickupspawn.roll / 128.0D;
		entityitem.serverPosX = packet21pickupspawn.xPosition;
		entityitem.serverPosY = packet21pickupspawn.yPosition;
		entityitem.serverPosZ = packet21pickupspawn.zPosition;
		this.worldClient.func_712_a(packet21pickupspawn.entityId, entityitem);
	}

	public void handleVehicleSpawn(Packet23VehicleSpawn packet23vehiclespawn) {
		double d = (double)packet23vehiclespawn.xPosition / 32.0D;
		double d1 = (double)packet23vehiclespawn.yPosition / 32.0D;
		double d2 = (double)packet23vehiclespawn.zPosition / 32.0D;
		Object obj = null;
		if(packet23vehiclespawn.type == 10) {
			obj = new EntityMinecart(this.worldClient, d, d1, d2, 0);
		}

		if(packet23vehiclespawn.type == 11) {
			obj = new EntityMinecart(this.worldClient, d, d1, d2, 1);
		}

		if(packet23vehiclespawn.type == 12) {
			obj = new EntityMinecart(this.worldClient, d, d1, d2, 2);
		}

		if(packet23vehiclespawn.type == 90) {
			obj = new EntityFish(this.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 60) {
			obj = new EntityArrow(this.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 61) {
			obj = new EntitySnowball(this.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 63) {
			obj = new EntityFireball(this.worldClient, d, d1, d2, (double)packet23vehiclespawn.field_28047_e / 8000.0D, (double)packet23vehiclespawn.field_28046_f / 8000.0D, (double)packet23vehiclespawn.field_28045_g / 8000.0D);
			packet23vehiclespawn.field_28044_i = 0;
		}

		if(packet23vehiclespawn.type == 62) {
			obj = new EntityEgg(this.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 1) {
			obj = new EntityBoat(this.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 50) {
			obj = new EntityTNTPrimed(this.worldClient, d, d1, d2);
		}

		if(packet23vehiclespawn.type == 70) {
			obj = new EntityFallingSand(this.worldClient, d, d1, d2, Block.sand.blockID);
		}

		if(packet23vehiclespawn.type == 71) {
			obj = new EntityFallingSand(this.worldClient, d, d1, d2, Block.gravel.blockID);
		}

		if(obj != null) {
			((Entity)obj).serverPosX = packet23vehiclespawn.xPosition;
			((Entity)obj).serverPosY = packet23vehiclespawn.yPosition;
			((Entity)obj).serverPosZ = packet23vehiclespawn.zPosition;
			((Entity)obj).rotationYaw = 0.0F;
			((Entity)obj).rotationPitch = 0.0F;
			((Entity)obj).entityId = packet23vehiclespawn.entityId;
			this.worldClient.func_712_a(packet23vehiclespawn.entityId, (Entity)obj);
			if(packet23vehiclespawn.field_28044_i > 0) {
				if(packet23vehiclespawn.type == 60) {
					Entity entity = this.getEntityByID(packet23vehiclespawn.field_28044_i);
					if(entity instanceof EntityLiving) {
						((EntityArrow)obj).owner = (EntityLiving)entity;
					}
				}

				((Entity)obj).setVelocity((double)packet23vehiclespawn.field_28047_e / 8000.0D, (double)packet23vehiclespawn.field_28046_f / 8000.0D, (double)packet23vehiclespawn.field_28045_g / 8000.0D);
			}
		}

	}

	public void handleWeather(Packet71Weather packet71weather) {
		double d = (double)packet71weather.field_27053_b / 32.0D;
		double d1 = (double)packet71weather.field_27057_c / 32.0D;
		double d2 = (double)packet71weather.field_27056_d / 32.0D;
		EntityLightningBolt entitylightningbolt = null;
		if(packet71weather.field_27055_e == 1) {
			entitylightningbolt = new EntityLightningBolt(this.worldClient, d, d1, d2);
		}

		if(entitylightningbolt != null) {
			entitylightningbolt.serverPosX = packet71weather.field_27053_b;
			entitylightningbolt.serverPosY = packet71weather.field_27057_c;
			entitylightningbolt.serverPosZ = packet71weather.field_27056_d;
			entitylightningbolt.rotationYaw = 0.0F;
			entitylightningbolt.rotationPitch = 0.0F;
			entitylightningbolt.entityId = packet71weather.field_27054_a;
			this.worldClient.addWeatherEffect(entitylightningbolt);
		}

	}

	public void func_21146_a(Packet25EntityPainting packet25entitypainting) {
		EntityPainting entitypainting = new EntityPainting(this.worldClient, packet25entitypainting.xPosition, packet25entitypainting.yPosition, packet25entitypainting.zPosition, packet25entitypainting.direction, packet25entitypainting.title);
		this.worldClient.func_712_a(packet25entitypainting.entityId, entitypainting);
	}

	public void func_6498_a(Packet28EntityVelocity packet28entityvelocity) {
		Entity entity = this.getEntityByID(packet28entityvelocity.entityId);
		if(entity != null) {
			entity.setVelocity((double)packet28entityvelocity.motionX / 8000.0D, (double)packet28entityvelocity.motionY / 8000.0D, (double)packet28entityvelocity.motionZ / 8000.0D);
		}
	}

	public void func_21148_a(Packet40EntityMetadata packet40entitymetadata) {
		Entity entity = this.getEntityByID(packet40entitymetadata.entityId);
		if(entity != null && packet40entitymetadata.func_21047_b() != null) {
			entity.getDataWatcher().updateWatchedObjectsFromList(packet40entitymetadata.func_21047_b());
		}

	}

	public void handleNamedEntitySpawn(Packet20NamedEntitySpawn packet20namedentityspawn) {
		double d = (double)packet20namedentityspawn.xPosition / 32.0D;
		double d1 = (double)packet20namedentityspawn.yPosition / 32.0D;
		double d2 = (double)packet20namedentityspawn.zPosition / 32.0D;
		float f = (float)(packet20namedentityspawn.rotation * 360) / 256.0F;
		float f1 = (float)(packet20namedentityspawn.pitch * 360) / 256.0F;
		EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.mc.theWorld, packet20namedentityspawn.name);
		entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (double)(entityotherplayermp.serverPosX = packet20namedentityspawn.xPosition);
		entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (double)(entityotherplayermp.serverPosY = packet20namedentityspawn.yPosition);
		entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (double)(entityotherplayermp.serverPosZ = packet20namedentityspawn.zPosition);
		int i = packet20namedentityspawn.currentItem;
		if(i == 0) {
			entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
		} else {
			entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(i, 1, 0);
		}

		entityotherplayermp.setPositionAndRotation(d, d1, d2, f, f1);
		this.worldClient.func_712_a(packet20namedentityspawn.entityId, entityotherplayermp);
	}

	public void handleEntityTeleport(Packet34EntityTeleport packet34entityteleport) {
		Entity entity = this.getEntityByID(packet34entityteleport.entityId);
		if(entity != null) {
			entity.serverPosX = packet34entityteleport.xPosition;
			entity.serverPosY = packet34entityteleport.yPosition;
			entity.serverPosZ = packet34entityteleport.zPosition;
			double d = (double)entity.serverPosX / 32.0D;
			double d1 = (double)entity.serverPosY / 32.0D + 1.0D / 64.0D;
			double d2 = (double)entity.serverPosZ / 32.0D;
			float f = (float)(packet34entityteleport.yaw * 360) / 256.0F;
			float f1 = (float)(packet34entityteleport.pitch * 360) / 256.0F;
			entity.setPositionAndRotation2(d, d1, d2, f, f1, 3);
		}
	}

	public void handleEntity(Packet30Entity packet30entity) {
		Entity entity = this.getEntityByID(packet30entity.entityId);
		if(entity != null) {
			entity.serverPosX += packet30entity.xPosition;
			entity.serverPosY += packet30entity.yPosition;
			entity.serverPosZ += packet30entity.zPosition;
			double d = (double)entity.serverPosX / 32.0D;
			double d1 = (double)entity.serverPosY / 32.0D;
			double d2 = (double)entity.serverPosZ / 32.0D;
			float f = packet30entity.rotating ? (float)(packet30entity.yaw * 360) / 256.0F : entity.rotationYaw;
			float f1 = packet30entity.rotating ? (float)(packet30entity.pitch * 360) / 256.0F : entity.rotationPitch;
			entity.setPositionAndRotation2(d, d1, d2, f, f1, 3);
		}
	}

	public void handleDestroyEntity(Packet29DestroyEntity packet29destroyentity) {
		this.worldClient.removeEntityFromWorld(packet29destroyentity.entityId);
	}

	public void handleFlying(Packet10Flying packet10flying) {
		EntityPlayerSP entityplayersp = this.mc.thePlayer;
		double d = entityplayersp.posX;
		double d1 = entityplayersp.posY;
		double d2 = entityplayersp.posZ;
		float f = entityplayersp.rotationYaw;
		float f1 = entityplayersp.rotationPitch;
		if(packet10flying.moving) {
			d = packet10flying.xPosition;
			d1 = packet10flying.yPosition;
			d2 = packet10flying.zPosition;
		}

		if(packet10flying.rotating) {
			f = packet10flying.yaw;
			f1 = packet10flying.pitch;
		}

		entityplayersp.ySize = 0.0F;
		entityplayersp.motionX = entityplayersp.motionY = entityplayersp.motionZ = 0.0D;
		entityplayersp.setPositionAndRotation(d, d1, d2, f, f1);
		packet10flying.xPosition = entityplayersp.posX;
		packet10flying.yPosition = entityplayersp.boundingBox.minY;
		packet10flying.zPosition = entityplayersp.posZ;
		packet10flying.stance = entityplayersp.posY;
		this.netManager.addToSendQueue(packet10flying);
		if(!this.field_1210_g) {
			this.mc.thePlayer.prevPosX = this.mc.thePlayer.posX;
			this.mc.thePlayer.prevPosY = this.mc.thePlayer.posY;
			this.mc.thePlayer.prevPosZ = this.mc.thePlayer.posZ;
			this.field_1210_g = true;
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void handlePreChunk(Packet50PreChunk packet50prechunk) {
		this.worldClient.doPreChunk(packet50prechunk.xPosition, packet50prechunk.yPosition, packet50prechunk.mode);
	}

	public void handleMultiBlockChange(Packet52MultiBlockChange packet52multiblockchange) {
		Chunk chunk = this.worldClient.getChunkFromChunkCoords(packet52multiblockchange.xPosition, packet52multiblockchange.zPosition);
		int i = packet52multiblockchange.xPosition * 16;
		int j = packet52multiblockchange.zPosition * 16;

		for(int k = 0; k < packet52multiblockchange.size; ++k) {
			short word0 = packet52multiblockchange.coordinateArray[k];
			int l = packet52multiblockchange.typeArray[k] & 255;
			byte byte0 = packet52multiblockchange.metadataArray[k];
			int i1 = word0 >> 12 & 15;
			int j1 = word0 >> 8 & 15;
			int k1 = word0 & 255;
			chunk.setBlockIDWithMetadata(i1, k1, j1, l, byte0);
			this.worldClient.func_711_c(i1 + i, k1, j1 + j, i1 + i, k1, j1 + j);
			this.worldClient.markBlocksDirty(i1 + i, k1, j1 + j, i1 + i, k1, j1 + j);
		}

	}

	public void handleMapChunk(Packet51MapChunk packet51mapchunk) {
		this.worldClient.func_711_c(packet51mapchunk.xPosition, packet51mapchunk.yPosition, packet51mapchunk.zPosition, packet51mapchunk.xPosition + packet51mapchunk.xSize - 1, packet51mapchunk.yPosition + packet51mapchunk.ySize - 1, packet51mapchunk.zPosition + packet51mapchunk.zSize - 1);
		this.worldClient.setChunkData(packet51mapchunk.xPosition, packet51mapchunk.yPosition, packet51mapchunk.zPosition, packet51mapchunk.xSize, packet51mapchunk.ySize, packet51mapchunk.zSize, packet51mapchunk.chunk);
	}

	public void handleBlockChange(Packet53BlockChange packet53blockchange) {
		this.worldClient.func_714_c(packet53blockchange.xPosition, packet53blockchange.yPosition, packet53blockchange.zPosition, packet53blockchange.type, packet53blockchange.metadata);
	}

	public void handleKickDisconnect(Packet255KickDisconnect packet255kickdisconnect) {
		this.netManager.networkShutdown("disconnect.kicked", new Object[0]);
		this.disconnected = true;
		this.mc.changeWorld1((World)null);
		this.mc.displayGuiScreen(new GuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", new Object[]{packet255kickdisconnect.reason}));
	}

	public void handleErrorMessage(String s, Object[] aobj) {
		if(!this.disconnected) {
			this.disconnected = true;
			this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiConnectFailed("disconnect.lost", s, aobj));
		}
	}

	public void func_28117_a(Packet packet) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(packet);
			this.netManager.func_28142_c();
		}
	}

	public void addToSendQueue(Packet packet) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(packet);
		}
	}

	public void handleCollect(Packet22Collect packet22collect) {
		Entity entity = this.getEntityByID(packet22collect.collectedEntityId);
		Object obj = (EntityLiving)this.getEntityByID(packet22collect.collectorEntityId);
		if(obj == null) {
			obj = this.mc.thePlayer;
		}

		if(entity != null) {
			this.worldClient.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, (Entity)((Entity)obj), -0.5F));
			this.worldClient.removeEntityFromWorld(packet22collect.collectedEntityId);
		}

	}

	public void handleChat(Packet3Chat packet3chat) {
		this.mc.ingameGUI.addChatMessage(packet3chat.message);
	}

	public void handleArmAnimation(Packet18Animation packet18animation) {
		Entity entity = this.getEntityByID(packet18animation.entityId);
		if(entity != null) {
			EntityPlayer entityplayer4;
			if(packet18animation.animate == 1) {
				entityplayer4 = (EntityPlayer)entity;
				entityplayer4.swingItem();
			} else if(packet18animation.animate == 2) {
				entity.performHurtAnimation();
			} else if(packet18animation.animate == 3) {
				entityplayer4 = (EntityPlayer)entity;
				entityplayer4.wakeUpPlayer(false, false, false);
			} else if(packet18animation.animate == 4) {
				entityplayer4 = (EntityPlayer)entity;
				entityplayer4.func_6420_o();
			}

			if(packet18animation.animate == 104) {
				entityplayer4 = (EntityPlayer)entity;
				entityplayer4.setEntityFlag(1, true);
			}

			if(packet18animation.animate == 105) {
				entityplayer4 = (EntityPlayer)entity;
				entityplayer4.setEntityFlag(1, false);
			}

		}
	}

	public void func_22186_a(Packet17Sleep packet17sleep) {
		Entity entity = this.getEntityByID(packet17sleep.field_22045_a);
		if(entity != null) {
			if(packet17sleep.field_22046_e == 0) {
				EntityPlayer entityplayer = (EntityPlayer)entity;
				entityplayer.sleepInBedAt(packet17sleep.field_22044_b, packet17sleep.field_22048_c, packet17sleep.field_22047_d);
			}

		}
	}

	public void handleHandshake(Packet2Handshake packet2handshake) {
		if(packet2handshake.username.equals("-")) {
			if(VersionManager.version().alpha()) {
				if(VersionManager.version().protocol() == 2) {
					this.addToSendQueue(new Packet1Login2(this.mc.session.username, "Password", VersionManager.version().protocol()));
				} else {
					this.addToSendQueue(new net.minecraft.protocol.alpha.Packet1Login(this.mc.session.username, "Password", VersionManager.version().protocol()));
				}
			} else if(VersionManager.version().protocol() < 11) {
				this.addToSendQueue(new net.minecraft.protocol.beta.Packet1Login(this.mc.session.username, "Password", VersionManager.version().protocol()));
			} else {
				this.addToSendQueue(new Packet1Login(this.mc.session.username, VersionManager.version().protocol()));
			}
		} else {
			try {
				URL exception = new URL("http://www.minecraft.net/game/joinserver.jsp?user=" + this.mc.session.username + "&sessionId=" + this.mc.session.sessionId + "&serverId=" + packet2handshake.username);
				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(exception.openStream()));
				String s = bufferedreader.readLine();
				bufferedreader.close();
				if(s.equalsIgnoreCase("ok")) {
					if(VersionManager.version().alpha()) {
						if(VersionManager.version().protocol() == 2) {
							this.addToSendQueue(new Packet1Login2(this.mc.session.username, "Password", VersionManager.version().protocol()));
						} else {
							this.addToSendQueue(new net.minecraft.protocol.alpha.Packet1Login(this.mc.session.username, "Password", VersionManager.version().protocol()));
						}
					} else if(VersionManager.version().protocol() < 11) {
						this.addToSendQueue(new net.minecraft.protocol.beta.Packet1Login(this.mc.session.username, "Password", VersionManager.version().protocol()));
					} else {
						this.addToSendQueue(new Packet1Login(this.mc.session.username, VersionManager.version().protocol()));
					}
				} else {
					this.netManager.networkShutdown("disconnect.loginFailedInfo", new Object[]{s});
				}
			} catch (Exception var5) {
				var5.printStackTrace();
				this.netManager.networkShutdown("disconnect.genericReason", new Object[]{"Internal client error: " + var5.toString()});
			}
		}

	}

	public void disconnect() {
		this.disconnected = true;
		this.netManager.wakeThreads();
		this.netManager.networkShutdown("disconnect.closed", new Object[0]);
	}

	public void handleMobSpawn(Packet24MobSpawn packet24mobspawn) {
		double d = (double)packet24mobspawn.xPosition / 32.0D;
		double d1 = (double)packet24mobspawn.yPosition / 32.0D;
		double d2 = (double)packet24mobspawn.zPosition / 32.0D;
		float f = (float)(packet24mobspawn.yaw * 360) / 256.0F;
		float f1 = (float)(packet24mobspawn.pitch * 360) / 256.0F;
		EntityLiving entityliving = (EntityLiving)EntityList.createEntity(packet24mobspawn.type, this.mc.theWorld);
		entityliving.serverPosX = packet24mobspawn.xPosition;
		entityliving.serverPosY = packet24mobspawn.yPosition;
		entityliving.serverPosZ = packet24mobspawn.zPosition;
		entityliving.entityId = packet24mobspawn.entityId;
		entityliving.setPositionAndRotation(d, d1, d2, f, f1);
		entityliving.isMultiplayerEntity = true;
		this.worldClient.func_712_a(packet24mobspawn.entityId, entityliving);
		List list = packet24mobspawn.getMetadata();
		if(list != null) {
			entityliving.getDataWatcher().updateWatchedObjectsFromList(list);
		}

	}

	public void handleUpdateTime(Packet4UpdateTime packet4updatetime) {
		this.mc.theWorld.setWorldTime(packet4updatetime.time);
	}

	public void handleSpawnPosition(Packet6SpawnPosition packet6spawnposition) {
		this.mc.thePlayer.setPlayerSpawnCoordinate(new ChunkCoordinates(packet6spawnposition.xPosition, packet6spawnposition.yPosition, packet6spawnposition.zPosition));
		this.mc.theWorld.getWorldInfo().setSpawn(packet6spawnposition.xPosition, packet6spawnposition.yPosition, packet6spawnposition.zPosition);
	}

	public void func_6497_a(Packet39AttachEntity packet39attachentity) {
		Object obj = this.getEntityByID(packet39attachentity.entityId);
		Entity entity = this.getEntityByID(packet39attachentity.vehicleEntityId);
		if(packet39attachentity.entityId == this.mc.thePlayer.entityId) {
			obj = this.mc.thePlayer;
		}

		if(obj != null) {
			((Entity)((Entity)obj)).mountEntity(entity);
		}
	}

	public void func_9447_a(Packet38EntityStatus packet38entitystatus) {
		Entity entity = this.getEntityByID(packet38entitystatus.entityId);
		if(entity != null) {
			entity.handleHealthUpdate(packet38entitystatus.entityStatus);
		}

	}

	public Entity getEntityByID(int i) {
		return (Entity)(i == this.mc.thePlayer.entityId ? this.mc.thePlayer : this.worldClient.func_709_b(i));
	}

	public void handleHealth(Packet8UpdateHealth packet8updatehealth) {
		this.mc.thePlayer.setHealth(packet8updatehealth.healthMP);
	}

	public void func_9448_a(Packet9Respawn packet9respawn) {
		if(packet9respawn.field_28048_a != this.mc.thePlayer.dimension) {
			this.field_1210_g = false;
			this.worldClient = new WorldClient(this, this.worldClient.getWorldInfo().getRandomSeed(), packet9respawn.field_28048_a);
			this.worldClient.multiplayerWorld = true;
			this.mc.changeWorld1(this.worldClient);
			this.mc.thePlayer.dimension = packet9respawn.field_28048_a;
			this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
		}

		this.mc.respawn(true, packet9respawn.field_28048_a);
	}

	public void func_12245_a(Packet60Explosion packet60explosion) {
		Explosion explosion = new Explosion(this.mc.theWorld, (Entity)null, packet60explosion.explosionX, packet60explosion.explosionY, packet60explosion.explosionZ, packet60explosion.explosionSize);
		explosion.destroyedBlockPositions = packet60explosion.destroyedBlockPositions;
		explosion.doExplosionB(true);
	}

	public void func_20087_a(Packet100OpenWindow packet100openwindow) {
		if(packet100openwindow.inventoryType == 0) {
			InventoryBasic entityplayersp = new InventoryBasic(packet100openwindow.windowTitle, packet100openwindow.slotsCount);
			this.mc.thePlayer.displayGUIChest(entityplayersp);
			this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
		} else if(packet100openwindow.inventoryType == 2) {
			TileEntityFurnace entityplayersp1 = new TileEntityFurnace();
			this.mc.thePlayer.displayGUIFurnace(entityplayersp1);
			this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
		} else if(packet100openwindow.inventoryType == 3) {
			TileEntityDispenser entityplayersp2 = new TileEntityDispenser();
			this.mc.thePlayer.displayGUIDispenser(entityplayersp2);
			this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
		} else if(packet100openwindow.inventoryType == 1) {
			EntityPlayerSP entityplayersp3 = this.mc.thePlayer;
			this.mc.thePlayer.displayWorkbenchGUI(MathHelper.floor_double(entityplayersp3.posX), MathHelper.floor_double(entityplayersp3.posY), MathHelper.floor_double(entityplayersp3.posZ));
			this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
		}

	}

	public void func_20088_a(Packet103SetSlot packet103setslot) {
		if(packet103setslot.windowId == -1) {
			this.mc.thePlayer.inventory.setItemStack(packet103setslot.myItemStack);
		} else if(packet103setslot.windowId == 0 && packet103setslot.itemSlot >= 36 && packet103setslot.itemSlot < 45) {
			ItemStack itemstack = this.mc.thePlayer.inventorySlots.getSlot(packet103setslot.itemSlot).getStack();
			if(packet103setslot.myItemStack != null && (itemstack == null || itemstack.stackSize < packet103setslot.myItemStack.stackSize)) {
				packet103setslot.myItemStack.animationsToGo = 5;
			}

			this.mc.thePlayer.inventorySlots.putStackInSlot(packet103setslot.itemSlot, packet103setslot.myItemStack);
		} else if(packet103setslot.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			this.mc.thePlayer.craftingInventory.putStackInSlot(packet103setslot.itemSlot, packet103setslot.myItemStack);
		}

	}

	public void func_20089_a(Packet106Transaction packet106transaction) {
		Container container = null;
		if(packet106transaction.windowId == 0) {
			container = this.mc.thePlayer.inventorySlots;
		} else if(packet106transaction.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			container = this.mc.thePlayer.craftingInventory;
		}

		if(container != null) {
			if(packet106transaction.field_20030_c) {
				container.func_20113_a(packet106transaction.field_20028_b);
			} else {
				container.func_20110_b(packet106transaction.field_20028_b);
				if(VersionManager.version().beta()) {
					this.addToSendQueue(new Packet106Transaction(packet106transaction.windowId, packet106transaction.field_20028_b, true));
				}
			}
		}

	}

	public void func_20094_a(Packet104WindowItems packet104windowitems) {
		if(packet104windowitems.windowId == 0) {
			this.mc.thePlayer.inventorySlots.putStacksInSlots(packet104windowitems.itemStack);
		} else if(packet104windowitems.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			this.mc.thePlayer.craftingInventory.putStacksInSlots(packet104windowitems.itemStack);
		}

	}

	public void handleSignUpdate(Packet130UpdateSign packet130updatesign) {
		if(this.mc.theWorld.blockExists(packet130updatesign.xPosition, packet130updatesign.yPosition, packet130updatesign.zPosition)) {
			TileEntity tileentity = this.mc.theWorld.getBlockTileEntity(packet130updatesign.xPosition, packet130updatesign.yPosition, packet130updatesign.zPosition);
			if(tileentity instanceof TileEntitySign) {
				TileEntitySign tileentitysign = (TileEntitySign)tileentity;

				for(int i = 0; i < 4; ++i) {
					tileentitysign.signText[i] = packet130updatesign.signLines[i];
				}

				tileentitysign.onInventoryChanged();
			}
		}

	}

	public void func_20090_a(Packet105UpdateProgressbar packet105updateprogressbar) {
		this.registerPacket(packet105updateprogressbar);
		if(this.mc.thePlayer.craftingInventory != null && this.mc.thePlayer.craftingInventory.windowId == packet105updateprogressbar.windowId) {
			this.mc.thePlayer.craftingInventory.func_20112_a(packet105updateprogressbar.progressBar, packet105updateprogressbar.progressBarValue);
		}

	}

	public void handlePlayerInventory(Packet5PlayerInventory packet5playerinventory) {
		Entity entity = this.getEntityByID(packet5playerinventory.entityID);
		if(entity != null) {
			entity.outfitWithItem(packet5playerinventory.slot, packet5playerinventory.itemID, packet5playerinventory.itemDamage);
		}

	}

	public void func_20092_a(Packet101CloseWindow packet101closewindow) {
		this.mc.thePlayer.closeScreen();
	}

	public void handleNotePlay(Packet54PlayNoteBlock packet54playnoteblock) {
		this.mc.theWorld.playNoteAt(packet54playnoteblock.xLocation, packet54playnoteblock.yLocation, packet54playnoteblock.zLocation, packet54playnoteblock.instrumentType, packet54playnoteblock.pitch);
	}

	public void func_25118_a(Packet70Bed packet70bed) {
		int i = packet70bed.field_25019_b;
		if(i >= 0 && i < Packet70Bed.field_25020_a.length && Packet70Bed.field_25020_a[i] != null) {
			this.mc.thePlayer.addChatMessage(Packet70Bed.field_25020_a[i]);
		}

		if(i == 1) {
			this.worldClient.getWorldInfo().setRaining(true);
			this.worldClient.func_27158_h(1.0F);
		} else if(i == 2) {
			this.worldClient.getWorldInfo().setRaining(false);
			this.worldClient.func_27158_h(0.0F);
		}

	}

	public void func_28116_a(Packet131MapData packet131mapdata) {
		if(packet131mapdata.field_28055_a == Item.mapItem.shiftedIndex) {
			ItemMap.func_28013_a(packet131mapdata.field_28054_b, this.mc.theWorld).func_28171_a(packet131mapdata.field_28056_c);
		} else {
			System.out.println("Unknown itemid: " + packet131mapdata.field_28054_b);
		}

	}

	public void func_28115_a(Packet61DoorChange packet61doorchange) {
		this.mc.theWorld.func_28106_e(packet61doorchange.field_28050_a, packet61doorchange.field_28053_c, packet61doorchange.field_28052_d, packet61doorchange.field_28051_e, packet61doorchange.field_28049_b);
	}

	public void func_27245_a(Packet200Statistic packet200statistic) {
		((EntityClientPlayerMP)this.mc.thePlayer).func_27027_b(StatList.func_27361_a(packet200statistic.field_27052_a), packet200statistic.field_27051_b);
	}

	public void handleAlphaOverride(Packet packet) {
		NetHandlerAlpha.handle(packet, this, this.mc);
	}

	public void handleBetaOverride(Packet packet) {
		NetHandlerBeta.handle(packet, this, this.mc);
	}

	public boolean isServerHandler() {
		return false;
	}
}
