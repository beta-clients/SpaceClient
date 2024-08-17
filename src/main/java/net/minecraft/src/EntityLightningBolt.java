package net.minecraft.src;

import java.util.List;

public class EntityLightningBolt extends EntityWeatherEffect {
	private int field_27028_b;
	public long field_27029_a = 0L;
	private int field_27030_c;

	public EntityLightningBolt(World world, double d, double d1, double d2) {
		super(world);
		this.setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
		this.field_27028_b = 2;
		this.field_27029_a = this.rand.nextLong();
		this.field_27030_c = this.rand.nextInt(3) + 1;
		if(world.difficultySetting >= 2 && world.doChunksNearChunkExist(MathHelper.floor_double(d), MathHelper.floor_double(d1), MathHelper.floor_double(d2), 10)) {
			int i = MathHelper.floor_double(d);
			int k = MathHelper.floor_double(d1);
			int i1 = MathHelper.floor_double(d2);
			if(world.getBlockId(i, k, i1) == 0 && Block.fire.canPlaceBlockAt(world, i, k, i1)) {
				world.setBlockWithNotify(i, k, i1, Block.fire.blockID);
			}

			for(int j = 0; j < 4; ++j) {
				int l = MathHelper.floor_double(d) + this.rand.nextInt(3) - 1;
				int j1 = MathHelper.floor_double(d1) + this.rand.nextInt(3) - 1;
				int k1 = MathHelper.floor_double(d2) + this.rand.nextInt(3) - 1;
				if(world.getBlockId(l, j1, k1) == 0 && Block.fire.canPlaceBlockAt(world, l, j1, k1)) {
					world.setBlockWithNotify(l, j1, k1, Block.fire.blockID);
				}
			}
		}

	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_27028_b == 2) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.old_explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
		}

		--this.field_27028_b;
		if(this.field_27028_b < 0) {
			if(this.field_27030_c == 0) {
				this.setEntityDead();
			} else if(this.field_27028_b < -this.rand.nextInt(10)) {
				--this.field_27030_c;
				this.field_27028_b = 1;
				this.field_27029_a = this.rand.nextLong();
				if(this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10)) {
					int d = MathHelper.floor_double(this.posX);
					int j = MathHelper.floor_double(this.posY);
					int list = MathHelper.floor_double(this.posZ);
					if(this.worldObj.getBlockId(d, j, list) == 0 && Block.fire.canPlaceBlockAt(this.worldObj, d, j, list)) {
						this.worldObj.setBlockWithNotify(d, j, list, Block.fire.blockID);
					}
				}
			}
		}

		if(this.field_27028_b >= 0) {
			double var6 = 3.0D;
			List var7 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(this.posX - var6, this.posY - var6, this.posZ - var6, this.posX + var6, this.posY + 6.0D + var6, this.posZ + var6));

			for(int l = 0; l < var7.size(); ++l) {
				Entity entity = (Entity)var7.get(l);
				entity.onStruckByLightning(this);
			}

			this.worldObj.field_27172_i = 2;
		}

	}

	protected void entityInit() {
	}

	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	public boolean isInRangeToRenderVec3D(Vec3D vec3d) {
		return this.field_27028_b >= 0;
	}
}
