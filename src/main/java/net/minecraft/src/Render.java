package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public abstract class Render {
	protected RenderManager renderManager;
	private ModelBase modelBase = new ModelBiped();
	private RenderBlocks renderBlocks = new RenderBlocks();
	protected float shadowSize = 0.0F;
	protected float field_194_c = 1.0F;

	public abstract void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9);

	protected void loadTexture(String s) {
		RenderEngine renderengine = this.renderManager.renderEngine;
		renderengine.bindTexture(renderengine.getTexture(s));
	}

	protected boolean loadDownloadableImageTexture(String s, String s1) {
		RenderEngine renderengine = this.renderManager.renderEngine;
		int i = renderengine.getTextureForDownloadableImage(s, s1);
		if(i >= 0) {
			renderengine.bindTexture(i);
			return true;
		} else {
			return false;
		}
	}

	private void renderEntityOnFire(Entity entity, double d, double d1, double d2, float f) {
		GL11.glDisable(GL11.GL_LIGHTING);
		int i = Block.fire.blockIndexInTexture;
		int j = (i & 15) << 4;
		int k = i & 240;
		float f1 = (float)j / 256.0F;
		float f3 = ((float)j + 15.99F) / 256.0F;
		float f5 = (float)k / 256.0F;
		float f7 = ((float)k + 15.99F) / 256.0F;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		float f9 = entity.width * 1.4F;
		GL11.glScalef(f9, f9, f9);
		this.loadTexture(VersionManager.version().textures() + "/terrain.png");
		Tessellator tessellator = Tessellator.instance;
		float f10 = 0.5F;
		float f11 = 0.0F;
		float f12 = entity.height / f9;
		float f13 = (float)(entity.posY - entity.boundingBox.minY);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)((int)f12) * 0.02F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f14 = 0.0F;
		int l = 0;
		tessellator.startDrawingQuads();

		while(f12 > 0.0F) {
			float f2;
			float f4;
			float f6;
			float f8;
			if(l % 2 == 0) {
				f2 = (float)j / 256.0F;
				f4 = ((float)j + 15.99F) / 256.0F;
				f6 = (float)k / 256.0F;
				f8 = ((float)k + 15.99F) / 256.0F;
			} else {
				f2 = (float)j / 256.0F;
				f4 = ((float)j + 15.99F) / 256.0F;
				f6 = (float)(k + 16) / 256.0F;
				f8 = ((float)(k + 16) + 15.99F) / 256.0F;
			}

			if(l / 2 % 2 == 0) {
				float f15 = f4;
				f4 = f2;
				f2 = f15;
			}

			tessellator.addVertexWithUV((double)(f10 - f11), (double)(0.0F - f13), (double)f14, (double)f4, (double)f8);
			tessellator.addVertexWithUV((double)(-f10 - f11), (double)(0.0F - f13), (double)f14, (double)f2, (double)f8);
			tessellator.addVertexWithUV((double)(-f10 - f11), (double)(1.4F - f13), (double)f14, (double)f2, (double)f6);
			tessellator.addVertexWithUV((double)(f10 - f11), (double)(1.4F - f13), (double)f14, (double)f4, (double)f6);
			f12 -= 0.45F;
			f13 -= 0.45F;
			f10 *= 0.9F;
			f14 += 0.03F;
			++l;
		}

		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void renderShadow(Entity entity, double d, double d1, double d2, float f, float f1) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderEngine renderengine = this.renderManager.renderEngine;
		renderengine.bindTexture(renderengine.getTexture("%clamp%/misc/shadow.png"));
		World world = this.getWorldFromRenderManager();
		GL11.glDepthMask(false);
		float f2 = this.shadowSize;
		double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f1;
		double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f1 + (double)entity.getShadowSize();
		double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f1;
		int i = MathHelper.floor_double(d3 - (double)f2);
		int j = MathHelper.floor_double(d3 + (double)f2);
		int k = MathHelper.floor_double(d4 - (double)f2);
		int l = MathHelper.floor_double(d4);
		int i1 = MathHelper.floor_double(d5 - (double)f2);
		int j1 = MathHelper.floor_double(d5 + (double)f2);
		double d6 = d - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for(int k1 = i; k1 <= j; ++k1) {
			for(int l1 = k; l1 <= l; ++l1) {
				for(int i2 = i1; i2 <= j1; ++i2) {
					int j2 = world.getBlockId(k1, l1 - 1, i2);
					if(j2 > 0 && world.getBlockLightValue(k1, l1, i2) > 3) {
						this.renderShadowOnBlock(Block.blocksList[j2], d, d1 + (double)entity.getShadowSize(), d2, k1, l1, i2, f, f2, d6, d7 + (double)entity.getShadowSize(), d8);
					}
				}
			}
		}

		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	private World getWorldFromRenderManager() {
		return this.renderManager.worldObj;
	}

	private void renderShadowOnBlock(Block block, double d, double d1, double d2, int i, int j, int k, float f, float f1, double d3, double d4, double d5) {
		Tessellator tessellator = Tessellator.instance;
		if(block.renderAsNormalBlock()) {
			double d6 = ((double)f - (d1 - ((double)j + d4)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getLightBrightness(i, j, k);
			if(d6 >= 0.0D) {
				if(d6 > 1.0D) {
					d6 = 1.0D;
				}

				tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d6);
				double d7 = (double)i + block.minX + d3;
				double d8 = (double)i + block.maxX + d3;
				double d9 = (double)j + block.minY + d4 + 1.0D / 64.0D;
				double d10 = (double)k + block.minZ + d5;
				double d11 = (double)k + block.maxZ + d5;
				float f2 = (float)((d - d7) / 2.0D / (double)f1 + 0.5D);
				float f3 = (float)((d - d8) / 2.0D / (double)f1 + 0.5D);
				float f4 = (float)((d2 - d10) / 2.0D / (double)f1 + 0.5D);
				float f5 = (float)((d2 - d11) / 2.0D / (double)f1 + 0.5D);
				tessellator.addVertexWithUV(d7, d9, d10, (double)f2, (double)f4);
				tessellator.addVertexWithUV(d7, d9, d11, (double)f2, (double)f5);
				tessellator.addVertexWithUV(d8, d9, d11, (double)f3, (double)f5);
				tessellator.addVertexWithUV(d8, d9, d10, (double)f3, (double)f4);
			}
		}
	}

	public static void renderOffsetAABB(AxisAlignedBB axisalignedbb, double d, double d1, double d2) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.startDrawingQuads();
		tessellator.setTranslationD(d, d1, d2);
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderAABB(AxisAlignedBB axisalignedbb) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.draw();
	}

	public void setRenderManager(RenderManager rendermanager) {
		this.renderManager = rendermanager;
	}

	public void doRenderShadowAndFire(Entity entity, double d, double d1, double d2, float f, float f1) {
		if(this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F) {
			double d3 = this.renderManager.func_851_a(entity.posX, entity.posY, entity.posZ);
			float f2 = (float)((1.0D - d3 / 256.0D) * (double)this.field_194_c);
			if(f2 > 0.0F) {
				this.renderShadow(entity, d, d1, d2, f2, f1);
			}
		}

		if(entity.isBurning()) {
			this.renderEntityOnFire(entity, d, d1, d2, f1);
		}

	}

	public FontRenderer getFontRendererFromRenderManager() {
		return this.renderManager.getFontRenderer();
	}
}
