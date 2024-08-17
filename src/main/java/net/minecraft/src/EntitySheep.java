package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;
import net.minecraft.protocol.NetHandlerAlpha;

public class EntitySheep extends EntityAnimal {
	public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};

	public EntitySheep(World world) {
		super(world);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(VersionManager.version().protocol() < 5 && entity != null && entity instanceof EntityClientPlayerMP && !this.getSheared()) {
			if(NetHandlerAlpha.netHandler == null) {
				return false;
			} else {
				this.setSheared(true);
				this.performHurtAnimation();
				this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				int i1 = 2 + this.rand.nextInt(3);

				for(int j = 0; j < i1; ++j) {
					EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + 1.0D, this.posZ, new ItemStack(Block.cloth));
					entityitem.motionY += (double)(this.rand.nextFloat() * 0.05F);
					entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					net.minecraft.protocol.alpha.Packet21PickupSpawn packet21pickupspawn = new net.minecraft.protocol.alpha.Packet21PickupSpawn(entityitem);
					entityitem.posX = (double)packet21pickupspawn.xPosition / 32.0D;
					entityitem.posY = (double)packet21pickupspawn.yPosition / 32.0D;
					entityitem.posZ = (double)packet21pickupspawn.zPosition / 32.0D;
					entityitem.motionX = (double)packet21pickupspawn.rotation / 128.0D;
					entityitem.motionY = (double)packet21pickupspawn.pitch / 128.0D;
					entityitem.motionZ = (double)packet21pickupspawn.roll / 128.0D;
					entityitem.delayBeforeCanPickup = 10;
					NetHandlerAlpha.netHandler.addToSendQueue(new net.minecraft.protocol.alpha.Packet21PickupSpawn(entityitem));
				}

				return true;
			}
		} else {
			return super.attackEntityFrom(entity, i);
		}
	}

	protected void dropFewItems() {
		if(!this.getSheared()) {
			this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 0.0F);
		}

	}

	protected int getDropItemId() {
		return Block.cloth.blockID;
	}

	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if(itemstack != null && itemstack.itemID == Item.shears.shiftedIndex && !this.getSheared()) {
			if(!this.worldObj.multiplayerWorld) {
				this.setSheared(true);
				int i = 2 + this.rand.nextInt(3);

				for(int j = 0; j < i; ++j) {
					EntityItem entityitem = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
					entityitem.motionY += (double)(this.rand.nextFloat() * 0.05F);
					entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				}
			}

			itemstack.damageItem(1, entityplayer);
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Sheared", this.getSheared());
		nbttagcompound.setByte("Color", (byte)this.getFleeceColor());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSheared(nbttagcompound.getBoolean("Sheared"));
		this.setFleeceColor(nbttagcompound.getByte("Color"));
	}

	protected String getLivingSound() {
		return "mob.sheep";
	}

	protected String getHurtSound() {
		return "mob.sheep";
	}

	protected String getDeathSound() {
		return "mob.sheep";
	}

	public int getFleeceColor() {
		return this.dataWatcher.getWatchableObjectByte(16) & 15;
	}

	public void setFleeceColor(int i) {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 240 | i & 15)));
	}

	public boolean getSheared() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	public void setSheared(boolean flag) {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
		if(flag) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 16)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -17)));
		}

	}

	public static int getRandomFleeceColor(Random random) {
		int i = random.nextInt(100);
		return i < 5 ? 15 : (i < 10 ? 7 : (i < 15 ? 8 : (i < 18 ? 12 : (random.nextInt(500) != 0 ? 0 : 6))));
	}
}
