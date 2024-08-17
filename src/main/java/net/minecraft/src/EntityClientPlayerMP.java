package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import net.minecraft.protocol.alpha.Packet9;

public class EntityClientPlayerMP extends EntityPlayerSP {
	public NetClientHandler sendQueue;
	private int field_9380_bx = 0;
	private boolean field_21093_bH = false;
	private double oldPosX;
	private double field_9378_bz;
	private double oldPosY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private boolean field_9382_bF = false;
	private boolean wasSneaking = false;
	private int field_12242_bI = 0;

	public EntityClientPlayerMP(Minecraft minecraft, World world, Session session, NetClientHandler netclienthandler) {
		super(minecraft, world, session, 0);
		this.sendQueue = netclienthandler;
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		return false;
	}

	public void heal(int i) {
	}

	public void onUpdate() {
		if(this.worldObj.blockExists(MathHelper.floor_double(this.posX), 64, MathHelper.floor_double(this.posZ))) {
			super.onUpdate();
			this.func_4056_N();
		}
	}

	public void func_4056_N() {
		if(this.field_9380_bx++ == 20) {
			this.sendInventoryChanged();
			this.field_9380_bx = 0;
		}

		boolean flag = this.isSneaking();
		if(flag != this.wasSneaking) {
			if(flag) {
				if(VersionManager.version().alpha()) {
					this.sendQueue.addToSendQueue(new Packet18Animation(this, 104));
				} else {
					this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
				}
			} else if(VersionManager.version().alpha()) {
				this.sendQueue.addToSendQueue(new Packet18Animation(this, 105));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
			}

			this.wasSneaking = flag;
		}

		double d = this.posX - this.oldPosX;
		double d1 = this.boundingBox.minY - this.field_9378_bz;
		double d2 = this.posY - this.oldPosY;
		double d3 = this.posZ - this.oldPosZ;
		double d4 = (double)(this.rotationYaw - this.oldRotationYaw);
		double d5 = (double)(this.rotationPitch - this.oldRotationPitch);
		boolean flag1 = d1 != 0.0D || d2 != 0.0D || d != 0.0D || d3 != 0.0D;
		boolean flag2 = d4 != 0.0D || d5 != 0.0D;
		if(this.ridingEntity != null) {
			if(flag2) {
				this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.motionX, -999.0D, -999.0D, this.motionZ, this.onGround));
			} else {
				this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
			}

			flag1 = false;
		} else if(flag1 && flag2) {
			this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
			this.field_12242_bI = 0;
		} else if(flag1) {
			this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
			this.field_12242_bI = 0;
		} else if(flag2) {
			this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
			this.field_12242_bI = 0;
		} else {
			this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
			if(this.field_9382_bF == this.onGround && this.field_12242_bI <= 200) {
				++this.field_12242_bI;
			} else {
				this.field_12242_bI = 0;
			}
		}

		this.field_9382_bF = this.onGround;
		if(flag1) {
			this.oldPosX = this.posX;
			this.field_9378_bz = this.boundingBox.minY;
			this.oldPosY = this.posY;
			this.oldPosZ = this.posZ;
		}

		if(flag2) {
			this.oldRotationYaw = this.rotationYaw;
			this.oldRotationPitch = this.rotationPitch;
		}

	}

	public void dropCurrentItem() {
		if(VersionManager.version().alpha()) {
			super.dropCurrentItem();
		} else {
			this.sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
		}

	}

	private void sendInventoryChanged() {
		if(VersionManager.version().alpha()) {
			InventoryCrafting craftingInventory = null;
			if(this.craftingInventory instanceof ContainerPlayer || this.craftingInventory instanceof ContainerWorkbench) {
				if(this.craftingInventory instanceof ContainerPlayer) {
					craftingInventory = ((ContainerPlayer)this.craftingInventory).craftMatrix;
				} else {
					craftingInventory = ((ContainerWorkbench)this.craftingInventory).craftMatrix;
				}
			}

			this.sendQueue.addToSendQueue(new net.minecraft.protocol.alpha.Packet5PlayerInventory(-1, this.inventory.mainInventory));
			if(craftingInventory != null) {
				this.sendQueue.addToSendQueue(new net.minecraft.protocol.alpha.Packet5PlayerInventory(-2, craftingInventory.stackList));
			}

			this.sendQueue.addToSendQueue(new net.minecraft.protocol.alpha.Packet5PlayerInventory(-3, this.inventory.armorInventory));
		}

	}

	protected void joinEntityItemWithWorld(EntityItem entityitem) {
		if(VersionManager.version().alpha()) {
			net.minecraft.protocol.alpha.Packet21PickupSpawn packet21pickupspawn = new net.minecraft.protocol.alpha.Packet21PickupSpawn(entityitem);
			this.sendQueue.addToSendQueue(packet21pickupspawn);
			entityitem.posX = (double)packet21pickupspawn.xPosition / 32.0D;
			entityitem.posY = (double)packet21pickupspawn.yPosition / 32.0D;
			entityitem.posZ = (double)packet21pickupspawn.zPosition / 32.0D;
			entityitem.motionX = (double)packet21pickupspawn.rotation / 128.0D;
			entityitem.motionY = (double)packet21pickupspawn.pitch / 128.0D;
			entityitem.motionZ = (double)packet21pickupspawn.roll / 128.0D;
		}

	}

	public void sendChatMessage(String s) {
		this.sendQueue.addToSendQueue(new Packet3Chat(s));
	}

	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
	}

	public void respawnPlayer() {
		this.sendInventoryChanged();
		if(VersionManager.version().alpha()) {
			this.sendQueue.addToSendQueue(new Packet9());
		} else {
			this.sendQueue.addToSendQueue(new Packet9Respawn((byte)this.dimension));
		}

	}

	protected void damageEntity(int i) {
		this.health -= i;
	}

	public void closeScreen() {
		if(VersionManager.version().beta()) {
			this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
			this.inventory.setItemStack((ItemStack)null);
		}

		super.closeScreen();
	}

	public void setHealth(int i) {
		if(this.field_21093_bH) {
			super.setHealth(i);
		} else {
			this.health = i;
			this.field_21093_bH = true;
		}

	}

	public void addStat(StatBase statbase, int i) {
		if(statbase != null) {
			if(statbase.field_27088_g) {
				super.addStat(statbase, i);
			}

		}
	}

	public void func_27027_b(StatBase statbase, int i) {
		if(statbase != null) {
			if(!statbase.field_27088_g) {
				super.addStat(statbase, i);
			}

		}
	}
}
