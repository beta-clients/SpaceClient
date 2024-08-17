package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class RenderMinecart extends Render {
	protected ModelBase modelMinecart;

	public RenderMinecart() {
		this.shadowSize = 0.5F;
		this.modelMinecart = new ModelMinecart();
	}

	public void func_152_a(EntityMinecart entityminecart, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		double d3 = entityminecart.lastTickPosX + (entityminecart.posX - entityminecart.lastTickPosX) * (double)f1;
		double d4 = entityminecart.lastTickPosY + (entityminecart.posY - entityminecart.lastTickPosY) * (double)f1;
		double d5 = entityminecart.lastTickPosZ + (entityminecart.posZ - entityminecart.lastTickPosZ) * (double)f1;
		double d6 = (double)0.3F;
		Vec3D vec3d = entityminecart.func_514_g(d3, d4, d5);
		float f2 = entityminecart.prevRotationPitch + (entityminecart.rotationPitch - entityminecart.prevRotationPitch) * f1;
		if(vec3d != null) {
			Vec3D f3 = entityminecart.func_515_a(d3, d4, d5, d6);
			Vec3D f4 = entityminecart.func_515_a(d3, d4, d5, -d6);
			if(f3 == null) {
				f3 = vec3d;
			}

			if(f4 == null) {
				f4 = vec3d;
			}

			d += vec3d.xCoord - d3;
			d1 += (f3.yCoord + f4.yCoord) / 2.0D - d4;
			d2 += vec3d.zCoord - d5;
			Vec3D f5 = f4.addVector(-f3.xCoord, -f3.yCoord, -f3.zCoord);
			if(f5.lengthVector() != 0.0D) {
				f5 = f5.normalize();
				f = (float)(Math.atan2(f5.zCoord, f5.xCoord) * 180.0D / Math.PI);
				f2 = (float)(Math.atan(f5.yCoord) * 73.0D);
			}
		}

		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(180.0F - f, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f2, 0.0F, 0.0F, 1.0F);
		float f31 = (float)entityminecart.minecartTimeSinceHit - f1;
		float f41 = (float)entityminecart.minecartCurrentDamage - f1;
		if(f41 < 0.0F) {
			f41 = 0.0F;
		}

		if(f31 > 0.0F) {
			GL11.glRotatef(MathHelper.sin(f31) * f31 * f41 / 10.0F * (float)entityminecart.minecartRockDirection, 1.0F, 0.0F, 0.0F);
		}

		if(entityminecart.minecartType != 0) {
			this.loadTexture(VersionManager.version().textures() + "/terrain.png");
			float f51 = 12.0F / 16.0F;
			GL11.glScalef(f51, f51, f51);
			GL11.glTranslatef(0.0F, 5.0F / 16.0F, 0.0F);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			if(entityminecart.minecartType == 1) {
				(new RenderBlocks()).renderBlockOnInventory(Block.chest, 0, entityminecart.getEntityBrightness(f1));
			} else if(entityminecart.minecartType == 2) {
				(new RenderBlocks()).renderBlockOnInventory(Block.stoneOvenIdle, 0, entityminecart.getEntityBrightness(f1));
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -(5.0F / 16.0F), 0.0F);
			GL11.glScalef(1.0F / f51, 1.0F / f51, 1.0F / f51);
		}

		this.loadTexture("/item/cart.png");
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelMinecart.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F / 16.0F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.func_152_a((EntityMinecart)entity, d, d1, d2, f, f1);
	}
}
