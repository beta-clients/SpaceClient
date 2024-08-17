package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Explosion {
	public boolean isFlaming = false;
	private Random ExplosionRNG = new Random();
	private World worldObj;
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public Entity exploder;
	public float explosionSize;
	public Set destroyedBlockPositions = new HashSet();

	public Explosion(World world, Entity entity, double d, double d1, double d2, float f) {
		this.worldObj = world;
		this.exploder = entity;
		this.explosionSize = f;
		this.explosionX = d;
		this.explosionY = d1;
		this.explosionZ = d2;
	}

	public void doExplosionA() {
		float f = this.explosionSize;
		byte i = 16;

		int k;
		int i1;
		int k1;
		double j3;
		double l3;
		double d10;
		for(k = 0; k < i; ++k) {
			for(i1 = 0; i1 < i; ++i1) {
				for(k1 = 0; k1 < i; ++k1) {
					if(k == 0 || k == i - 1 || i1 == 0 || i1 == i - 1 || k1 == 0 || k1 == i - 1) {
						double l1 = (double)((float)k / ((float)i - 1.0F) * 2.0F - 1.0F);
						double j2 = (double)((float)i1 / ((float)i - 1.0F) * 2.0F - 1.0F);
						double vec3d = (double)((float)k1 / ((float)i - 1.0F) * 2.0F - 1.0F);
						double l2 = Math.sqrt(l1 * l1 + j2 * j2 + vec3d * vec3d);
						l1 /= l2;
						j2 /= l2;
						vec3d /= l2;
						float i3 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						j3 = this.explosionX;
						l3 = this.explosionY;
						d10 = this.explosionZ;

						for(float d11 = 0.3F; i3 > 0.0F; i3 -= d11 * (12.0F / 16.0F)) {
							int j4 = MathHelper.floor_double(j3);
							int d12 = MathHelper.floor_double(l3);
							int l4 = MathHelper.floor_double(d10);
							int d13 = this.worldObj.getBlockId(j4, d12, l4);
							if(d13 > 0) {
								i3 -= (Block.blocksList[d13].getExplosionResistance(this.exploder) + 0.3F) * d11;
							}

							if(i3 > 0.0F) {
								this.destroyedBlockPositions.add(new ChunkPosition(j4, d12, l4));
							}

							j3 += l1 * (double)d11;
							l3 += j2 * (double)d11;
							d10 += vec3d * (double)d11;
						}
					}
				}
			}
		}

		this.explosionSize *= 2.0F;
		k = MathHelper.floor_double(this.explosionX - (double)this.explosionSize - 1.0D);
		i1 = MathHelper.floor_double(this.explosionX + (double)this.explosionSize + 1.0D);
		k1 = MathHelper.floor_double(this.explosionY - (double)this.explosionSize - 1.0D);
		int var29 = MathHelper.floor_double(this.explosionY + (double)this.explosionSize + 1.0D);
		int i2 = MathHelper.floor_double(this.explosionZ - (double)this.explosionSize - 1.0D);
		int var30 = MathHelper.floor_double(this.explosionZ + (double)this.explosionSize + 1.0D);
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBoxFromPool((double)k, (double)k1, (double)i2, (double)i1, (double)var29, (double)var30));
		Vec3D var31 = Vec3D.createVector(this.explosionX, this.explosionY, this.explosionZ);

		for(int arraylist = 0; arraylist < list.size(); ++arraylist) {
			Entity var33 = (Entity)list.get(arraylist);
			double chunkposition = var33.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.explosionSize;
			if(chunkposition <= 1.0D) {
				j3 = var33.posX - this.explosionX;
				l3 = var33.posY - this.explosionY;
				d10 = var33.posZ - this.explosionZ;
				double var39 = (double)MathHelper.sqrt_double(j3 * j3 + l3 * l3 + d10 * d10);
				j3 /= var39;
				l3 /= var39;
				d10 /= var39;
				double var40 = (double)this.worldObj.func_675_a(var31, var33.boundingBox);
				double var41 = (1.0D - chunkposition) * var40;
				var33.attackEntityFrom(this.exploder, (int)((var41 * var41 + var41) / 2.0D * 8.0D * (double)this.explosionSize + 1.0D));
				var33.motionX += j3 * var41;
				var33.motionY += l3 * var41;
				var33.motionZ += d10 * var41;
			}
		}

		this.explosionSize = f;
		ArrayList var32 = new ArrayList();
		var32.addAll(this.destroyedBlockPositions);
		if(this.isFlaming) {
			for(int var34 = var32.size() - 1; var34 >= 0; --var34) {
				ChunkPosition var35 = (ChunkPosition)var32.get(var34);
				int var36 = var35.x;
				int var37 = var35.y;
				int k3 = var35.z;
				int var38 = this.worldObj.getBlockId(var36, var37, k3);
				int i4 = this.worldObj.getBlockId(var36, var37 - 1, k3);
				if(var38 == 0 && Block.opaqueCubeLookup[i4] && this.ExplosionRNG.nextInt(3) == 0) {
					this.worldObj.setBlockWithNotify(var36, var37, k3, Block.fire.blockID);
				}
			}
		}

	}

	public void doExplosionB(boolean flag) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.old_explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		ArrayList arraylist = new ArrayList();
		arraylist.addAll(this.destroyedBlockPositions);

		for(int i = arraylist.size() - 1; i >= 0; --i) {
			ChunkPosition chunkposition = (ChunkPosition)arraylist.get(i);
			int j = chunkposition.x;
			int k = chunkposition.y;
			int l = chunkposition.z;
			int i1 = this.worldObj.getBlockId(j, k, l);
			if(flag) {
				double d = (double)((float)j + this.worldObj.rand.nextFloat());
				double d1 = (double)((float)k + this.worldObj.rand.nextFloat());
				double d2 = (double)((float)l + this.worldObj.rand.nextFloat());
				double d3 = d - this.explosionX;
				double d4 = d1 - this.explosionY;
				double d5 = d2 - this.explosionZ;
				double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
				d3 /= d6;
				d4 /= d6;
				d5 /= d6;
				double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
				d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
				d3 *= d7;
				d4 *= d7;
				d5 *= d7;
				this.worldObj.spawnParticle("explode", (d + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
				this.worldObj.spawnParticle("smoke", d, d1, d2, d3, d4, d5);
			}

			if(i1 > 0) {
				Block.blocksList[i1].dropBlockAsItemWithChance(this.worldObj, j, k, l, this.worldObj.getBlockMetadata(j, k, l), 0.3F);
				this.worldObj.setBlockWithNotify(j, k, l, 0);
				Block.blocksList[i1].onBlockDestroyedByExplosion(this.worldObj, j, k, l);
			}
		}

	}
}
