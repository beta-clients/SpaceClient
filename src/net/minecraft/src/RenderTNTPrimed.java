package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class RenderTNTPrimed extends Render {
	private RenderBlocks blockRenderer = new RenderBlocks();

	public RenderTNTPrimed() {
		this.shadowSize = 0.5F;
	}

	public void func_153_a(EntityTNTPrimed entitytntprimed, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		float f3;
		if((float)entitytntprimed.fuse - f1 + 1.0F < 10.0F) {
			f3 = 1.0F - ((float)entitytntprimed.fuse - f1 + 1.0F) / 10.0F;
			if(f3 < 0.0F) {
				f3 = 0.0F;
			}

			if(f3 > 1.0F) {
				f3 = 1.0F;
			}

			f3 *= f3;
			f3 *= f3;
			float f4 = 1.0F + f3 * 0.3F;
			GL11.glScalef(f4, f4, f4);
		}

		f3 = (1.0F - ((float)entitytntprimed.fuse - f1 + 1.0F) / 100.0F) * 0.8F;
		this.loadTexture(VersionManager.version().textures() + "/terrain.png");
		this.blockRenderer.renderBlockOnInventory(Block.tnt, 0, entitytntprimed.getEntityBrightness(f1));
		if(entitytntprimed.fuse / 5 % 2 == 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
			this.blockRenderer.renderBlockOnInventory(Block.tnt, 0, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.func_153_a((EntityTNTPrimed)entity, d, d1, d2, f, f1);
	}
}
