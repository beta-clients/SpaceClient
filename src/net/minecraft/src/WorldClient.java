package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.multiversion.VersionManager;
import net.minecraft.protocol.alpha.Packet59ComplexEntity;

public class WorldClient extends World {
	private LinkedList field_1057_z = new LinkedList();
	private NetClientHandler sendQueue;
	private ChunkProviderClient field_20915_C;
	private boolean scheduledTickSet = false;
	private MCHash field_1055_D = new MCHash();
	private Set field_20914_E = new HashSet();
	private Set field_1053_F = new HashSet();

	public WorldClient(NetClientHandler netclienthandler, long l, int i) {
		super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(i), l);
		this.sendQueue = netclienthandler;
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
		this.field_28108_z = netclienthandler.field_28118_b;
	}

	public void tick() {
		this.setWorldTime(this.getWorldTime() + 1L);
		int i = this.calculateSkylightSubtracted(1.0F);
		int l;
		if(i != this.skylightSubtracted) {
			this.skylightSubtracted = i;

			for(l = 0; l < this.worldAccesses.size(); ++l) {
				((IWorldAccess)this.worldAccesses.get(l)).updateAllRenderers();
			}
		}

		for(l = 0; l < 10 && !this.field_1053_F.isEmpty(); ++l) {
			Entity worldblockpositiontype = (Entity)this.field_1053_F.iterator().next();
			if(!this.loadedEntityList.contains(worldblockpositiontype)) {
				this.entityJoinedWorld(worldblockpositiontype);
			}
		}

		this.sendQueue.processReadPackets();

		for(l = 0; l < this.field_1057_z.size(); ++l) {
			WorldBlockPositionType var4 = (WorldBlockPositionType)this.field_1057_z.get(l);
			if(--var4.field_1206_d == 0) {
				super.setBlockAndMetadata(var4.field_1202_a, var4.field_1201_b, var4.field_1207_c, var4.field_1205_e, var4.field_1204_f);
				super.markBlockNeedsUpdate(var4.field_1202_a, var4.field_1201_b, var4.field_1207_c);
				this.field_1057_z.remove(l--);
			}
		}

	}

	public void func_711_c(int i, int j, int k, int l, int i1, int j1) {
		for(int k1 = 0; k1 < this.field_1057_z.size(); ++k1) {
			WorldBlockPositionType worldblockpositiontype = (WorldBlockPositionType)this.field_1057_z.get(k1);
			if(worldblockpositiontype.field_1202_a >= i && worldblockpositiontype.field_1201_b >= j && worldblockpositiontype.field_1207_c >= k && worldblockpositiontype.field_1202_a <= l && worldblockpositiontype.field_1201_b <= i1 && worldblockpositiontype.field_1207_c <= j1) {
				this.field_1057_z.remove(k1--);
			}
		}

	}

	protected IChunkProvider getChunkProvider() {
		this.field_20915_C = new ChunkProviderClient(this);
		return this.field_20915_C;
	}

	public void setSpawnLocation() {
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
	}

	protected void updateBlocksAndPlayCaveSounds() {
	}

	public void scheduleBlockUpdate(int i, int j, int k, int l, int i1) {
	}

	public boolean TickUpdates(boolean flag) {
		return false;
	}

	public void doPreChunk(int i, int j, boolean flag) {
		if(flag) {
			this.field_20915_C.prepareChunk(i, j);
		} else {
			this.field_20915_C.func_539_c(i, j);
		}

		if(!flag) {
			this.markBlocksDirty(i * 16, 0, j * 16, i * 16 + 15, 128, j * 16 + 15);
		}

	}

	public boolean entityJoinedWorld(Entity entity) {
		boolean flag = super.entityJoinedWorld(entity);
		this.field_20914_E.add(entity);
		if(!flag) {
			this.field_1053_F.add(entity);
		}

		return flag;
	}

	public void setEntityDead(Entity entity) {
		super.setEntityDead(entity);
		this.field_20914_E.remove(entity);
	}

	protected void obtainEntitySkin(Entity entity) {
		super.obtainEntitySkin(entity);
		if(this.field_1053_F.contains(entity)) {
			this.field_1053_F.remove(entity);
		}

	}

	protected void releaseEntitySkin(Entity entity) {
		super.releaseEntitySkin(entity);
		if(this.field_20914_E.contains(entity)) {
			this.field_1053_F.add(entity);
		}

	}

	public void func_712_a(int i, Entity entity) {
		Entity entity1 = this.func_709_b(i);
		if(entity1 != null) {
			this.setEntityDead(entity1);
		}

		this.field_20914_E.add(entity);
		entity.entityId = i;
		if(!this.entityJoinedWorld(entity)) {
			this.field_1053_F.add(entity);
		}

		this.field_1055_D.addKey(i, entity);
	}

	public Entity func_709_b(int i) {
		return (Entity)this.field_1055_D.lookup(i);
	}

	public Entity removeEntityFromWorld(int i) {
		Entity entity = (Entity)this.field_1055_D.removeObject(i);
		if(entity != null) {
			this.field_20914_E.remove(entity);
			this.setEntityDead(entity);
		}

		return entity;
	}

	public boolean setBlockMetadata(int i, int j, int k, int l) {
		int i1 = this.getBlockId(i, j, k);
		int j1 = this.getBlockMetadata(i, j, k);
		if(super.setBlockMetadata(i, j, k, l)) {
			this.field_1057_z.add(new WorldBlockPositionType(this, i, j, k, i1, j1));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadata(int i, int j, int k, int l, int i1) {
		int j1 = this.getBlockId(i, j, k);
		int k1 = this.getBlockMetadata(i, j, k);
		if(super.setBlockAndMetadata(i, j, k, l, i1)) {
			this.field_1057_z.add(new WorldBlockPositionType(this, i, j, k, j1, k1));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlock(int i, int j, int k, int l) {
		int i1 = this.getBlockId(i, j, k);
		int j1 = this.getBlockMetadata(i, j, k);
		if(super.setBlock(i, j, k, l)) {
			this.field_1057_z.add(new WorldBlockPositionType(this, i, j, k, i1, j1));
			return true;
		} else {
			return false;
		}
	}

	public boolean func_714_c(int i, int j, int k, int l, int i1) {
		this.func_711_c(i, j, k, i, j, k);
		if(super.setBlockAndMetadata(i, j, k, l, i1)) {
			this.notifyBlockChange(i, j, k, l);
			return true;
		} else {
			return false;
		}
	}

	public void func_698_b(int i, int j, int k, TileEntity tileentity) {
		if(!this.scheduledTickSet) {
			if(VersionManager.version().protocol() < 7) {
				this.sendQueue.addToSendQueue(new Packet59ComplexEntity(i, j, k, tileentity));
			}

		}
	}

	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.func_28117_a(new Packet255KickDisconnect("Quitting"));
	}

	protected void updateWeather() {
		if(!this.worldProvider.hasNoSky) {
			if(this.field_27168_F > 0) {
				--this.field_27168_F;
			}

			this.prevRainingStrength = this.rainingStrength;
			if(this.worldInfo.getRaining()) {
				this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
			} else {
				this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
			}

			if(this.rainingStrength < 0.0F) {
				this.rainingStrength = 0.0F;
			}

			if(this.rainingStrength > 1.0F) {
				this.rainingStrength = 1.0F;
			}

			this.prevThunderingStrength = this.thunderingStrength;
			if(this.worldInfo.getThundering()) {
				this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
			} else {
				this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
			}

			if(this.thunderingStrength < 0.0F) {
				this.thunderingStrength = 0.0F;
			}

			if(this.thunderingStrength > 1.0F) {
				this.thunderingStrength = 1.0F;
			}

		}
	}
}
