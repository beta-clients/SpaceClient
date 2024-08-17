package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItem extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();
	private Random random = new Random();
	public boolean field_27004_a = true;

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.field_194_c = 12.0F / 16.0F;
	}

	public void doRenderItem(EntityItem entityitem, double d, double d1, double d2, float f, float f1) {
		this.random.setSeed(187L);
		ItemStack itemstack = entityitem.item;
		GL11.glPushMatrix();
		float f2 = MathHelper.sin(((float)entityitem.age + f1) / 10.0F + entityitem.field_804_d) * 0.1F + 0.1F;
		float f3 = (((float)entityitem.age + f1) / 20.0F + entityitem.field_804_d) * 57.29578F;
		byte byte0 = 1;
		if(entityitem.item.stackSize > 1) {
			byte0 = 2;
		}

		if(entityitem.item.stackSize > 5) {
			byte0 = 3;
		}

		if(entityitem.item.stackSize > 20) {
			byte0 = 4;
		}

		GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float f6;
		float f8;
		float f10;
		if(itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			this.loadTexture(VersionManager.version().textures() + "/terrain.png");
			float var28 = 0.25F;
			if(!Block.blocksList[itemstack.itemID].renderAsNormalBlock() && itemstack.itemID != Block.stairSingle.blockID && Block.blocksList[itemstack.itemID].getRenderType() != 16) {
				var28 = 0.5F;
			}

			GL11.glScalef(var28, var28, var28);

			for(int var29 = 0; var29 < byte0; ++var29) {
				GL11.glPushMatrix();
				if(var29 > 0) {
					f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var28;
					f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var28;
					f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var28;
					GL11.glTranslatef(f6, f8, f10);
				}

				this.renderBlocks.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getItemDamage(), entityitem.getEntityBrightness(f1));
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int i = itemstack.getIconIndex();
			if(itemstack.itemID < 256) {
				this.loadTexture(VersionManager.version().textures() + "/terrain.png");
			} else {
				this.loadTexture("/gui/items.png");
			}

			Tessellator tessellator = Tessellator.instance;
			f6 = (float)(i % 16 * 16 + 0) / 256.0F;
			f8 = (float)(i % 16 * 16 + 16) / 256.0F;
			f10 = (float)(i / 16 * 16 + 0) / 256.0F;
			float f11 = (float)(i / 16 * 16 + 16) / 256.0F;
			float f12 = 1.0F;
			float f13 = 0.5F;
			float f14 = 0.25F;
			int l;
			float f16;
			float f18;
			float f20;
			if(this.field_27004_a) {
				l = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage());
				f16 = (float)(l >> 16 & 255) / 255.0F;
				f18 = (float)(l >> 8 & 255) / 255.0F;
				f20 = (float)(l & 255) / 255.0F;
				float f21 = entityitem.getEntityBrightness(f1);
				GL11.glColor4f(f16 * f21, f18 * f21, f20 * f21, 1.0F);
			}

			for(l = 0; l < byte0; ++l) {
				GL11.glPushMatrix();
				if(l > 0) {
					f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					f18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					f20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(f16, f18, f20);
				}

				GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.addVertexWithUV((double)(0.0F - f13), (double)(0.0F - f14), 0.0D, (double)f6, (double)f11);
				tessellator.addVertexWithUV((double)(f12 - f13), (double)(0.0F - f14), 0.0D, (double)f8, (double)f11);
				tessellator.addVertexWithUV((double)(f12 - f13), (double)(1.0F - f14), 0.0D, (double)f8, (double)f10);
				tessellator.addVertexWithUV((double)(0.0F - f13), (double)(1.0F - f14), 0.0D, (double)f6, (double)f10);
				tessellator.draw();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1) {
		float f3;
		if(i < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[i].getRenderType())) {
			renderengine.bindTexture(renderengine.getTexture(VersionManager.version().textures() + "/terrain.png"));
			Block f1 = Block.blocksList[i];
			GL11.glPushMatrix();
			GL11.glTranslatef((float)(l - 2), (float)(i1 + 3), -3.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int f11 = Item.itemsList[i].getColorFromDamage(j);
			f3 = (float)(f11 >> 16 & 255) / 255.0F;
			float f4 = (float)(f11 >> 8 & 255) / 255.0F;
			float f5 = (float)(f11 & 255) / 255.0F;
			if(this.field_27004_a) {
				GL11.glColor4f(f3, f4, f5, 1.0F);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.renderBlocks.field_31088_b = this.field_27004_a;
			this.renderBlocks.renderBlockOnInventory(f1, j, 1.0F);
			this.renderBlocks.field_31088_b = true;
			GL11.glPopMatrix();
		} else if(k >= 0) {
			GL11.glDisable(GL11.GL_LIGHTING);
			if(i < 256) {
				renderengine.bindTexture(renderengine.getTexture(VersionManager.version().textures() + "/terrain.png"));
			} else {
				renderengine.bindTexture(renderengine.getTexture("/gui/items.png"));
			}

			int k1 = Item.itemsList[i].getColorFromDamage(j);
			float f = (float)(k1 >> 16 & 255) / 255.0F;
			float f1 = (float)(k1 >> 8 & 255) / 255.0F;
			f3 = (float)(k1 & 255) / 255.0F;
			if(this.field_27004_a) {
				GL11.glColor4f(f, f1, f3, 1.0F);
			}

			this.renderTexturedQuad(l, i1, k % 16 * 16, k / 16 * 16, 16, 16);
			GL11.glEnable(GL11.GL_LIGHTING);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j) {
		if(itemstack != null) {
			this.drawItemIntoGui(fontrenderer, renderengine, itemstack.itemID, itemstack.getItemDamage(), itemstack.getIconIndex(), i, j);
		}
	}

	public void renderItemOverlayIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j) {
		if(itemstack != null) {
			String k = "" + itemstack.stackSize;
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			fontrenderer.drawStringWithShadow(k, i + 19 - 2 - fontrenderer.getStringWidth(k), j + 6 + 3, 16777215);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			if(itemstack.isItemDamaged()) {
				int k1 = (int)Math.round(13.0D - (double)itemstack.getItemDamageForDisplay() * 13.0D / (double)itemstack.getMaxDamage());
				int l = (int)Math.round(255.0D - (double)itemstack.getItemDamageForDisplay() * 255.0D / (double)itemstack.getMaxDamage());
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator tessellator = Tessellator.instance;
				int i1 = 255 - l << 16 | l << 8;
				int j1 = (255 - l) / 4 << 16 | 16128;
				this.renderQuad(tessellator, i + 2, j + 13, 13, 2, 0);
				this.renderQuad(tessellator, i + 2, j + 13, 12, 1, j1);
				this.renderQuad(tessellator, i + 2, j + 13, k1, 1, i1);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

		}
	}

	private void renderQuad(Tessellator tessellator, int i, int j, int k, int l, int i1) {
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(i1);
		tessellator.addVertex((double)(i + 0), (double)(j + 0), 0.0D);
		tessellator.addVertex((double)(i + 0), (double)(j + l), 0.0D);
		tessellator.addVertex((double)(i + k), (double)(j + l), 0.0D);
		tessellator.addVertex((double)(i + k), (double)(j + 0), 0.0D);
		tessellator.draw();
	}

	public void renderTexturedQuad(int i, int j, int k, int l, int i1, int j1) {
		float f = 0.0F;
		float f1 = 0.00390625F;
		float f2 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(i + 0), (double)(j + j1), (double)f, (double)((float)(k + 0) * f1), (double)((float)(l + j1) * f2));
		tessellator.addVertexWithUV((double)(i + i1), (double)(j + j1), (double)f, (double)((float)(k + i1) * f1), (double)((float)(l + j1) * f2));
		tessellator.addVertexWithUV((double)(i + i1), (double)(j + 0), (double)f, (double)((float)(k + i1) * f1), (double)((float)(l + 0) * f2));
		tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)f, (double)((float)(k + 0) * f1), (double)((float)(l + 0) * f2));
		tessellator.draw();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.doRenderItem((EntityItem)entity, d, d1, d2, f, f1);
	}
}
