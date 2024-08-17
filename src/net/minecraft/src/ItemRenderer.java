package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();
	private MapItemRenderer field_28131_f;
	private int field_20099_f = -1;

	public ItemRenderer(Minecraft minecraft) {
		this.mc = minecraft;
		this.field_28131_f = new MapItemRenderer(minecraft.fontRenderer, minecraft.gameSettings, minecraft.renderEngine);
	}

	public void renderItem(EntityLiving entityliving, ItemStack itemstack) {
		GL11.glPushMatrix();
		if(itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png"));
			this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getItemDamage(), entityliving.getEntityBrightness(1.0F));
		} else {
			if(itemstack.itemID < 256) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png"));
			} else {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
			}

			Tessellator tessellator = Tessellator.instance;
			int i = entityliving.getItemIcon(itemstack);
			float f = ((float)(i % 16 * 16) + 0.0F) / 256.0F;
			float f1 = ((float)(i % 16 * 16) + 15.99F) / 256.0F;
			float f2 = ((float)(i / 16 * 16) + 0.0F) / 256.0F;
			float f3 = ((float)(i / 16 * 16) + 15.99F) / 256.0F;
			float f4 = 1.0F;
			float f5 = 0.0F;
			float f6 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-f5, -f6, 0.0F);
			float f7 = 1.5F;
			GL11.glScalef(f7, f7, f7);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-(15.0F / 16.0F), -(1.0F / 16.0F), 0.0F);
			float f8 = 1.0F / 16.0F;
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)f1, (double)f3);
			tessellator.addVertexWithUV((double)f4, 0.0D, 0.0D, (double)f, (double)f3);
			tessellator.addVertexWithUV((double)f4, 1.0D, 0.0D, (double)f, (double)f2);
			tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)f1, (double)f2);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			tessellator.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - f8), (double)f1, (double)f2);
			tessellator.addVertexWithUV((double)f4, 1.0D, (double)(0.0F - f8), (double)f, (double)f2);
			tessellator.addVertexWithUV((double)f4, 0.0D, (double)(0.0F - f8), (double)f, (double)f3);
			tessellator.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - f8), (double)f1, (double)f3);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);

			int i1;
			float f12;
			float f16;
			float f20;
			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f1 + (f - f1) * f12 - 0.001953125F;
				f20 = f4 * f12;
				tessellator.addVertexWithUV((double)f20, 0.0D, (double)(0.0F - f8), (double)f16, (double)f3);
				tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)f3);
				tessellator.addVertexWithUV((double)f20, 1.0D, 0.0D, (double)f16, (double)f2);
				tessellator.addVertexWithUV((double)f20, 1.0D, (double)(0.0F - f8), (double)f16, (double)f2);
			}

			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);

			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f1 + (f - f1) * f12 - 0.001953125F;
				f20 = f4 * f12 + 1.0F / 16.0F;
				tessellator.addVertexWithUV((double)f20, 1.0D, (double)(0.0F - f8), (double)f16, (double)f2);
				tessellator.addVertexWithUV((double)f20, 1.0D, 0.0D, (double)f16, (double)f2);
				tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)f3);
				tessellator.addVertexWithUV((double)f20, 0.0D, (double)(0.0F - f8), (double)f16, (double)f3);
			}

			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);

			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f3 + (f2 - f3) * f12 - 0.001953125F;
				f20 = f4 * f12 + 1.0F / 16.0F;
				tessellator.addVertexWithUV(0.0D, (double)f20, 0.0D, (double)f1, (double)f16);
				tessellator.addVertexWithUV((double)f4, (double)f20, 0.0D, (double)f, (double)f16);
				tessellator.addVertexWithUV((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
				tessellator.addVertexWithUV(0.0D, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
			}

			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);

			for(i1 = 0; i1 < 16; ++i1) {
				f12 = (float)i1 / 16.0F;
				f16 = f3 + (f2 - f3) * f12 - 0.001953125F;
				f20 = f4 * f12;
				tessellator.addVertexWithUV((double)f4, (double)f20, 0.0D, (double)f, (double)f16);
				tessellator.addVertexWithUV(0.0D, (double)f20, 0.0D, (double)f1, (double)f16);
				tessellator.addVertexWithUV(0.0D, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
				tessellator.addVertexWithUV((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
			}

			tessellator.draw();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		GL11.glPopMatrix();
	}

	public void renderItemInFirstPerson(float f) {
		float f1 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * f;
		EntityPlayerSP entityplayersp = this.mc.thePlayer;
		float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * f;
		GL11.glPushMatrix();
		GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * f, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		ItemStack itemstack = this.itemToRender;
		float f3 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(entityplayersp.posX), MathHelper.floor_double(entityplayersp.posY), MathHelper.floor_double(entityplayersp.posZ));
		float f10;
		float f14;
		float f18;
		if(itemstack != null) {
			int f6 = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage());
			f10 = (float)(f6 >> 16 & 255) / 255.0F;
			f14 = (float)(f6 >> 8 & 255) / 255.0F;
			f18 = (float)(f6 & 255) / 255.0F;
			GL11.glColor4f(f3 * f10, f3 * f14, f3 * f18, 1.0F);
		} else {
			GL11.glColor4f(f3, f3, f3, 1.0F);
		}

		Render render;
		RenderPlayer renderplayer;
		float var15;
		if(itemstack != null && itemstack.itemID == Item.mapItem.shiftedIndex) {
			GL11.glPushMatrix();
			var15 = 0.8F;
			f10 = entityplayersp.getSwingProgress(f);
			f14 = MathHelper.sin(f10 * 3.141593F);
			f18 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F);
			GL11.glTranslatef(-f18 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F * 2.0F) * 0.2F, -f14 * 0.2F);
			f10 = 1.0F - f2 / 45.0F + 0.1F;
			if(f10 < 0.0F) {
				f10 = 0.0F;
			}

			if(f10 > 1.0F) {
				f10 = 1.0F;
			}

			f10 = -MathHelper.cos(f10 * 3.141593F) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * var15 - (1.0F - f1) * 1.2F - f10 * 0.5F + 0.04F, -0.9F * var15);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(f10 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getEntityTexture()));

			for(f14 = 0.0F; f14 < 2.0F; ++f14) {
				f18 = f14 * 2.0F - 1.0F;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * f18);
				GL11.glRotatef(-45.0F * f18, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-65.0F * f18, 0.0F, 1.0F, 0.0F);
				render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
				renderplayer = (RenderPlayer)render;
				float byte0 = 1.0F;
				GL11.glScalef(byte0, byte0, byte0);
				renderplayer.drawFirstPersonHand();
				GL11.glPopMatrix();
			}

			f14 = entityplayersp.getSwingProgress(f);
			f18 = MathHelper.sin(f14 * f14 * 3.141593F);
			float var16 = MathHelper.sin(MathHelper.sqrt_float(f14) * 3.141593F);
			GL11.glRotatef(-f18 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var16 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var16 * 80.0F, 1.0F, 0.0F, 0.0F);
			f14 = 0.38F;
			GL11.glScalef(f14, f14, f14);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			f18 = 0.015625F;
			GL11.glScalef(f18, f18, f18);
			this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/misc/mapbg.png"));
			Tessellator var17 = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			var17.startDrawingQuads();
			byte var18 = 7;
			var17.addVertexWithUV((double)(0 - var18), (double)(128 + var18), 0.0D, 0.0D, 1.0D);
			var17.addVertexWithUV((double)(128 + var18), (double)(128 + var18), 0.0D, 1.0D, 1.0D);
			var17.addVertexWithUV((double)(128 + var18), (double)(0 - var18), 0.0D, 1.0D, 0.0D);
			var17.addVertexWithUV((double)(0 - var18), (double)(0 - var18), 0.0D, 0.0D, 0.0D);
			var17.draw();
			MapData mapdata = Item.mapItem.func_28012_a(itemstack, this.mc.theWorld);
			this.field_28131_f.func_28157_a(this.mc.thePlayer, this.mc.renderEngine, mapdata);
			GL11.glPopMatrix();
		} else if(itemstack != null) {
			GL11.glPushMatrix();
			var15 = 0.8F;
			f10 = entityplayersp.getSwingProgress(f);
			f14 = MathHelper.sin(f10 * 3.141593F);
			f18 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F);
			GL11.glTranslatef(-f18 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F * 2.0F) * 0.2F, -f14 * 0.2F);
			GL11.glTranslatef(0.7F * var15, -0.65F * var15 - (1.0F - f1) * 0.6F, -0.9F * var15);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f10 = entityplayersp.getSwingProgress(f);
			f14 = MathHelper.sin(f10 * f10 * 3.141593F);
			f18 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F);
			GL11.glRotatef(-f14 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f18 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f18 * 80.0F, 1.0F, 0.0F, 0.0F);
			f10 = 0.4F;
			GL11.glScalef(f10, f10, f10);
			if(itemstack.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			this.renderItem(entityplayersp, itemstack);
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			var15 = 0.8F;
			f10 = entityplayersp.getSwingProgress(f);
			f14 = MathHelper.sin(f10 * 3.141593F);
			f18 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F);
			GL11.glTranslatef(-f18 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F * 2.0F) * 0.4F, -f14 * 0.4F);
			GL11.glTranslatef(0.8F * var15, -(12.0F / 16.0F) * var15 - (1.0F - f1) * 0.6F, -0.9F * var15);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f10 = entityplayersp.getSwingProgress(f);
			f14 = MathHelper.sin(f10 * f10 * 3.141593F);
			f18 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F);
			GL11.glRotatef(f18 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f14 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getEntityTexture()));
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			renderplayer = (RenderPlayer)render;
			f18 = 1.0F;
			GL11.glScalef(f18, f18, f18);
			renderplayer.drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderOverlays(float f) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		int k;
		if(this.mc.thePlayer.isBurning()) {
			k = this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, k);
			this.renderFireInFirstPerson(f);
		}

		if(this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
			k = MathHelper.floor_double(this.mc.thePlayer.posX);
			int l = MathHelper.floor_double(this.mc.thePlayer.posY);
			int i1 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			int j1 = this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, j1);
			int k1 = this.mc.theWorld.getBlockId(k, l, i1);
			if(this.mc.theWorld.isBlockNormalCube(k, l, i1)) {
				this.renderInsideOfBlock(f, Block.blocksList[k1].getBlockTextureFromSide(2));
			} else {
				for(int l1 = 0; l1 < 8; ++l1) {
					float f1 = ((float)((l1 >> 0) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					float f2 = ((float)((l1 >> 1) % 2) - 0.5F) * this.mc.thePlayer.height * 0.2F;
					float f3 = ((float)((l1 >> 2) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					int i2 = MathHelper.floor_float((float)k + f1);
					int j2 = MathHelper.floor_float((float)l + f2);
					int k2 = MathHelper.floor_float((float)i1 + f3);
					if(this.mc.theWorld.isBlockNormalCube(i2, j2, k2)) {
						k1 = this.mc.theWorld.getBlockId(i2, j2, k2);
					}
				}
			}

			if(Block.blocksList[k1] != null) {
				this.renderInsideOfBlock(f, Block.blocksList[k1].getBlockTextureFromSide(2));
			}
		}

		if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			k = this.mc.renderEngine.getTexture("/misc/water.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, k);
			this.renderWarpedTextureOverlay(f);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void renderInsideOfBlock(float f, int i) {
		Tessellator tessellator = Tessellator.instance;
		this.mc.thePlayer.getEntityBrightness(f);
		float f1 = 0.1F;
		GL11.glColor4f(f1, f1, f1, 0.5F);
		GL11.glPushMatrix();
		float f2 = -1.0F;
		float f3 = 1.0F;
		float f4 = -1.0F;
		float f5 = 1.0F;
		float f6 = -0.5F;
		float f7 = 0.0078125F;
		float f8 = (float)(i % 16) / 256.0F - f7;
		float f9 = ((float)(i % 16) + 15.99F) / 256.0F + f7;
		float f10 = (float)(i / 16) / 256.0F - f7;
		float f11 = ((float)(i / 16) + 15.99F) / 256.0F + f7;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)f2, (double)f4, (double)f6, (double)f9, (double)f11);
		tessellator.addVertexWithUV((double)f3, (double)f4, (double)f6, (double)f8, (double)f11);
		tessellator.addVertexWithUV((double)f3, (double)f5, (double)f6, (double)f8, (double)f10);
		tessellator.addVertexWithUV((double)f2, (double)f5, (double)f6, (double)f9, (double)f10);
		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderWarpedTextureOverlay(float f) {
		Tessellator tessellator = Tessellator.instance;
		float f1 = this.mc.thePlayer.getEntityBrightness(f);
		GL11.glColor4f(f1, f1, f1, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		float f2 = 4.0F;
		float f3 = -1.0F;
		float f4 = 1.0F;
		float f5 = -1.0F;
		float f6 = 1.0F;
		float f7 = -0.5F;
		float f8 = -this.mc.thePlayer.rotationYaw / 64.0F;
		float f9 = this.mc.thePlayer.rotationPitch / 64.0F;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)f3, (double)f5, (double)f7, (double)(f2 + f8), (double)(f2 + f9));
		tessellator.addVertexWithUV((double)f4, (double)f5, (double)f7, (double)(0.0F + f8), (double)(f2 + f9));
		tessellator.addVertexWithUV((double)f4, (double)f6, (double)f7, (double)(0.0F + f8), (double)(0.0F + f9));
		tessellator.addVertexWithUV((double)f3, (double)f6, (double)f7, (double)(f2 + f8), (double)(0.0F + f9));
		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderFireInFirstPerson(float f) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float f1 = 1.0F;

		for(int i = 0; i < 2; ++i) {
			GL11.glPushMatrix();
			int j = Block.fire.blockIndexInTexture + i * 16;
			int k = (j & 15) << 4;
			int l = j & 240;
			float f2 = (float)k / 256.0F;
			float f3 = ((float)k + 15.99F) / 256.0F;
			float f4 = (float)l / 256.0F;
			float f5 = ((float)l + 15.99F) / 256.0F;
			float f6 = (0.0F - f1) / 2.0F;
			float f7 = f6 + f1;
			float f8 = 0.0F - f1 / 2.0F;
			float f9 = f8 + f1;
			float f10 = -0.5F;
			GL11.glTranslatef((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((float)(i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV((double)f6, (double)f8, (double)f10, (double)f3, (double)f5);
			tessellator.addVertexWithUV((double)f7, (double)f8, (double)f10, (double)f2, (double)f5);
			tessellator.addVertexWithUV((double)f7, (double)f9, (double)f10, (double)f2, (double)f4);
			tessellator.addVertexWithUV((double)f6, (double)f9, (double)f10, (double)f3, (double)f4);
			tessellator.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		EntityPlayerSP entityplayersp = this.mc.thePlayer;
		ItemStack itemstack = entityplayersp.inventory.getCurrentItem();
		boolean flag = this.field_20099_f == entityplayersp.inventory.currentItem && itemstack == this.itemToRender;
		if(this.itemToRender == null && itemstack == null) {
			flag = true;
		}

		if(itemstack != null && this.itemToRender != null && itemstack != this.itemToRender && itemstack.itemID == this.itemToRender.itemID && itemstack.getItemDamage() == this.itemToRender.getItemDamage()) {
			this.itemToRender = itemstack;
			flag = true;
		}

		float f = 0.4F;
		float f1 = flag ? 1.0F : 0.0F;
		float f2 = f1 - this.equippedProgress;
		if(f2 < -f) {
			f2 = -f;
		}

		if(f2 > f) {
			f2 = f;
		}

		this.equippedProgress += f2;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = itemstack;
			this.field_20099_f = entityplayersp.inventory.currentItem;
		}

	}

	public void func_9449_b() {
		this.equippedProgress = 0.0F;
	}

	public void func_9450_c() {
		this.equippedProgress = 0.0F;
	}
}
