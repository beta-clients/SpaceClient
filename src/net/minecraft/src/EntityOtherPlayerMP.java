package net.minecraft.src;

public class EntityOtherPlayerMP extends EntityPlayer {
	private int field_785_bg;
	private double field_784_bh;
	private double field_783_bi;
	private double field_782_bj;
	private double field_780_bk;
	private double field_786_bl;
	float field_20924_a = 0.0F;

	public EntityOtherPlayerMP(World world, String s) {
		super(world);
		this.username = s;
		this.yOffset = 0.0F;
		this.stepHeight = 0.0F;
		if(s != null && s.length() > 0) {
			this.skinUrl = "http://icebergcraft.com:6543/api/Minecraft/GetSkinByUsername/?username=" + s + ".png";
		}

		this.noClip = true;
		this.field_22062_y = 0.25F;
		this.renderDistanceWeight = 10.0D;
	}

	protected void resetHeight() {
		this.yOffset = 0.0F;
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		return true;
	}

	public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i) {
		this.field_784_bh = d;
		this.field_783_bi = d1;
		this.field_782_bj = d2;
		this.field_780_bk = (double)f;
		this.field_786_bl = (double)f1;
		this.field_785_bg = i;
	}

	public void onUpdate() {
		this.field_22062_y = 0.0F;
		super.onUpdate();
		this.field_705_Q = this.field_704_R;
		double d = this.posX - this.prevPosX;
		double d1 = this.posZ - this.prevPosZ;
		float f = MathHelper.sqrt_double(d * d + d1 * d1) * 4.0F;
		if(f > 1.0F) {
			f = 1.0F;
		}

		this.field_704_R += (f - this.field_704_R) * 0.4F;
		this.field_703_S += this.field_704_R;
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void onLivingUpdate() {
		super.updatePlayerActionState();
		if(this.field_785_bg > 0) {
			double f = this.posX + (this.field_784_bh - this.posX) / (double)this.field_785_bg;
			double d1 = this.posY + (this.field_783_bi - this.posY) / (double)this.field_785_bg;
			double d2 = this.posZ + (this.field_782_bj - this.posZ) / (double)this.field_785_bg;

			double d3;
			for(d3 = this.field_780_bk - (double)this.rotationYaw; d3 < -180.0D; d3 += 360.0D) {
			}

			while(d3 >= 180.0D) {
				d3 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.field_785_bg);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.field_786_bl - (double)this.rotationPitch) / (double)this.field_785_bg);
			--this.field_785_bg;
			this.setPosition(f, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		this.field_775_e = this.field_774_f;
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f2 = (float)Math.atan(-this.motionY * (double)0.2F) * 15.0F;
		if(f1 > 0.1F) {
			f1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		this.field_774_f += (f1 - this.field_774_f) * 0.4F;
		this.field_9328_R += (f1 - this.field_9328_R) * 0.8F;
	}

	public void outfitWithItem(int i, int j, int k) {
		ItemStack itemstack = null;
		if(j >= 0) {
			itemstack = new ItemStack(j, 1, k);
		}

		if(i == 0) {
			this.inventory.mainInventory[this.inventory.currentItem] = itemstack;
		} else {
			this.inventory.armorInventory[i - 1] = itemstack;
		}

	}

	public void func_6420_o() {
	}
}
