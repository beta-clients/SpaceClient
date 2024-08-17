package net.minecraft.src;

public class EntitySkeleton extends EntityMob {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);

	public EntitySkeleton(World world) {
		super(world);
		this.texture = "/mob/skeleton.png";
	}

	protected String getLivingSound() {
		return "mob.skeleton";
	}

	protected String getHurtSound() {
		return "mob.skeletonhurt";
	}

	protected String getDeathSound() {
		return "mob.skeletonhurt";
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime()) {
			float f = this.getEntityBrightness(1.0F);
			if(f > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		super.onLivingUpdate();
	}

	protected void attackEntity(Entity entity, float f) {
		if(f < 10.0F) {
			double d = entity.posX - this.posX;
			double d1 = entity.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow entityarrow = new EntityArrow(this.worldObj, this);
				entityarrow.posY += (double)1.4F;
				double d2 = entity.posY + (double)entity.getEyeHeight() - (double)0.2F - entityarrow.posY;
				float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.old_bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.entityJoinedWorld(entityarrow);
				entityarrow.setArrowHeading(d, d2 + (double)f1, d1, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / (double)((float)Math.PI)) - 90.0F;
			this.hasAttacked = true;
		}

	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}

	protected void dropFewItems() {
		int i = this.rand.nextInt(3);

		int k;
		for(k = 0; k < i; ++k) {
			this.dropItem(Item.arrow.shiftedIndex, 1);
		}

		i = this.rand.nextInt(3);

		for(k = 0; k < i; ++k) {
			this.dropItem(Item.bone.shiftedIndex, 1);
		}

	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}
}
