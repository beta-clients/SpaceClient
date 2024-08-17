package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class RenderBoat extends Render {
	protected ModelBase modelBoat;

	public RenderBoat() {
		this.shadowSize = 0.5F;
		this.modelBoat = new ModelBoat();
	}

	public void func_157_a(EntityBoat entityboat, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(180.0F - f, 0.0F, 1.0F, 0.0F);
		float f2 = (float)entityboat.boatTimeSinceHit - f1;
		float f3 = (float)entityboat.boatCurrentDamage - f1;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f2 > 0.0F) {
			GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float)entityboat.boatRockDirection, 1.0F, 0.0F, 0.0F);
		}

		this.loadTexture(VersionManager.version().textures() + "/terrain.png");
		float f4 = 12.0F / 16.0F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		this.loadTexture("/item/boat.png");
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelBoat.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F / 16.0F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.func_157_a((EntityBoat)entity, d, d1, d2, f, f1);
	}
}
