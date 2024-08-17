package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class RenderFallingSand extends Render {
	private RenderBlocks field_197_d = new RenderBlocks();

	public RenderFallingSand() {
		this.shadowSize = 0.5F;
	}

	public void doRenderFallingSand(EntityFallingSand entityfallingsand, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		this.loadTexture(VersionManager.version().textures() + "/terrain.png");
		Block block = Block.blocksList[entityfallingsand.blockID];
		World world = entityfallingsand.getWorld();
		GL11.glDisable(GL11.GL_LIGHTING);
		this.field_197_d.renderBlockFallingSand(block, world, MathHelper.floor_double(entityfallingsand.posX), MathHelper.floor_double(entityfallingsand.posY), MathHelper.floor_double(entityfallingsand.posZ));
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.doRenderFallingSand((EntityFallingSand)entity, d, d1, d2, f, f1);
	}
}
