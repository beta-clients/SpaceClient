package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

	protected boolean setArmorModel(EntityPlayer entityplayer, int i, float f) {
		ItemStack itemstack = entityplayer.inventory.armorItemInSlot(3 - i);
		if(itemstack != null) {
			Item item = itemstack.getItem();
			if(item instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor)item;
				this.loadTexture("/armor/" + armorFilenamePrefix[itemarmor.renderIndex] + "_" + (i != 2 ? 1 : 2) + ".png");
				ModelBiped modelbiped = i != 2 ? this.modelArmorChestplate : this.modelArmor;
				modelbiped.bipedHead.showModel = i == 0;
				modelbiped.bipedHeadwear.showModel = i == 0;
				modelbiped.bipedBody.showModel = i == 1 || i == 2;
				modelbiped.bipedRightArm.showModel = i == 1;
				modelbiped.bipedLeftArm.showModel = i == 1;
				modelbiped.bipedRightLeg.showModel = i == 2 || i == 3;
				modelbiped.bipedLeftLeg.showModel = i == 2 || i == 3;
				this.setRenderPassModel(modelbiped);
				return true;
			}
		}

		return false;
	}

	public void renderPlayer(EntityPlayer entityplayer, double d, double d1, double d2, float f, float f1) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		this.modelArmorChestplate.field_1278_i = this.modelArmor.field_1278_i = this.modelBipedMain.field_1278_i = itemstack != null;
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = entityplayer.isSneaking();
		double d3 = d1 - (double)entityplayer.yOffset;
		if(entityplayer.isSneaking() && !(entityplayer instanceof EntityPlayerSP)) {
			d3 -= 0.125D;
		}

		super.doRenderLiving(entityplayer, d, d3, d2, f, f1);
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
		this.modelArmorChestplate.field_1278_i = this.modelArmor.field_1278_i = this.modelBipedMain.field_1278_i = false;
	}

	protected void renderName(EntityPlayer entityplayer, double d, double d1, double d2) {
		if(Minecraft.isGuiEnabled() && entityplayer != this.renderManager.livingPlayer) {
			float f = 1.6F;
			float f1 = 0.01666667F * f;
			float f2 = entityplayer.getDistanceToEntity(this.renderManager.livingPlayer);
			float f3 = entityplayer.isSneaking() ? 32.0F : 64.0F;
			if(f2 < f3) {
				boolean vg = VersionManager.getMinecraft().gameSettings.versionGraphics;
				String s = entityplayer.username;
				FontRenderer fontrenderer;
				Tessellator tessellator;
				int i;
				if(!entityplayer.isSneaking()) {
					if(entityplayer.isPlayerSleeping()) {
						this.renderLivingLabel(entityplayer, s, d, d1 - 1.5D, d2, 64);
					} else if(VersionManager.version().alpha() && vg) {
						f1 = (float)((double)f1 * (Math.sqrt((double)f2) / 2.0D));
						fontrenderer = this.getFontRendererFromRenderManager();
						GL11.glPushMatrix();
						GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
						GL11.glNormal3f(0.0F, 1.0F, 0.0F);
						GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
						GL11.glScalef(-f1, -f1, f1);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glDepthMask(false);
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						tessellator = Tessellator.instance;
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						tessellator.startDrawingQuads();
						i = fontrenderer.getStringWidth(s) / 2;
						tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
						tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
						tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
						tessellator.draw();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDepthMask(true);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, -1);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glPopMatrix();
					} else {
						this.renderLivingLabel(entityplayer, s, d, d1, d2, 64);
					}
				} else {
					fontrenderer = this.getFontRendererFromRenderManager();
					if(VersionManager.version().alpha() && vg) {
						GL11.glPushMatrix();
						GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
						GL11.glDepthMask(false);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						tessellator = Tessellator.instance;
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						tessellator.startDrawingQuads();
						i = fontrenderer.getStringWidth(s) / 2;
						tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
						tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
						tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
						tessellator.draw();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDepthMask(true);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glPopMatrix();
					} else {
						GL11.glPushMatrix();
						GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
						GL11.glNormal3f(0.0F, 1.0F, 0.0F);
						GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
						GL11.glScalef(-f1, -f1, f1);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
						GL11.glDepthMask(false);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						tessellator = Tessellator.instance;
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						tessellator.startDrawingQuads();
						i = fontrenderer.getStringWidth(s) / 2;
						tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
						tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
						tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
						tessellator.draw();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDepthMask(true);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glPopMatrix();
					}
				}
			}
		}

	}

	protected void renderSpecials(EntityPlayer entityplayer, float f) {
		ItemStack itemstack = entityplayer.inventory.armorItemInSlot(3);
		if(itemstack != null && itemstack.getItem().shiftedIndex < 256) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedHead.postRender(1.0F / 16.0F);
			if(RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
				float itemstack1 = 10.0F / 16.0F;
				GL11.glTranslatef(0.0F, -0.25F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(itemstack1, -itemstack1, itemstack1);
			}

			this.renderManager.itemRenderer.renderItem(entityplayer, itemstack);
			GL11.glPopMatrix();
		}

		float f5;
		if(entityplayer.username.equals("deadmau5") && this.loadDownloadableImageTexture(entityplayer.skinUrl, (String)null)) {
			for(int var19 = 0; var19 < 2; ++var19) {
				f5 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f - (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * f);
				float d1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
				GL11.glPushMatrix();
				GL11.glRotatef(f5, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(d1, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(6.0F / 16.0F * (float)(var19 * 2 - 1), 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -(6.0F / 16.0F), 0.0F);
				GL11.glRotatef(-d1, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-f5, 0.0F, 1.0F, 0.0F);
				float f7 = 1.333333F;
				GL11.glScalef(f7, f7, f7);
				this.modelBipedMain.renderEars(1.0F / 16.0F);
				GL11.glPopMatrix();
			}
		}

		if(this.loadDownloadableImageTexture(entityplayer.playerCloakUrl, (String)null)) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 2.0F / 16.0F);
			double var20 = entityplayer.field_20066_r + (entityplayer.field_20063_u - entityplayer.field_20066_r) * (double)f - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)f);
			double var22 = entityplayer.field_20065_s + (entityplayer.field_20062_v - entityplayer.field_20065_s) * (double)f - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)f);
			double d2 = entityplayer.field_20064_t + (entityplayer.field_20061_w - entityplayer.field_20064_t) * (double)f - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)f);
			float f8 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * f;
			double d3 = (double)MathHelper.sin(f8 * 3.141593F / 180.0F);
			double d4 = (double)(-MathHelper.cos(f8 * 3.141593F / 180.0F));
			float f9 = (float)var22 * 10.0F;
			if(f9 < -6.0F) {
				f9 = -6.0F;
			}

			if(f9 > 32.0F) {
				f9 = 32.0F;
			}

			float f10 = (float)(var20 * d3 + d2 * d4) * 100.0F;
			float f11 = (float)(var20 * d4 - d2 * d3) * 100.0F;
			if(f10 < 0.0F) {
				f10 = 0.0F;
			}

			float f12 = entityplayer.field_775_e + (entityplayer.field_774_f - entityplayer.field_775_e) * f;
			f9 += MathHelper.sin((entityplayer.prevDistanceWalkedModified + (entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified) * f) * 6.0F) * 32.0F * f12;
			if(entityplayer.isSneaking()) {
				f9 += 25.0F;
			}

			GL11.glRotatef(6.0F + f10 / 2.0F + f9, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f11 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f11 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			this.modelBipedMain.renderCloak(1.0F / 16.0F);
			GL11.glPopMatrix();
		}

		ItemStack var21 = entityplayer.inventory.getCurrentItem();
		if(var21 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(1.0F / 16.0F);
			GL11.glTranslatef(-(1.0F / 16.0F), 7.0F / 16.0F, 1.0F / 16.0F);
			if(entityplayer.fishEntity != null) {
				var21 = new ItemStack(Item.stick);
			}

			if(var21.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var21.itemID].getRenderType())) {
				f5 = 0.5F;
				GL11.glTranslatef(0.0F, 3.0F / 16.0F, -(5.0F / 16.0F));
				f5 *= 12.0F / 16.0F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f5, -f5, f5);
			} else if(Item.itemsList[var21.itemID].isFull3D()) {
				f5 = 10.0F / 16.0F;
				if(Item.itemsList[var21.itemID].shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -(2.0F / 16.0F), 0.0F);
				}

				GL11.glTranslatef(0.0F, 3.0F / 16.0F, 0.0F);
				GL11.glScalef(f5, -f5, f5);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f5 = 6.0F / 16.0F;
				GL11.glTranslatef(0.25F, 3.0F / 16.0F, -(3.0F / 16.0F));
				GL11.glScalef(f5, f5, f5);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(entityplayer, var21);
			GL11.glPopMatrix();
		}

	}

	protected void func_186_b(EntityPlayer entityplayer, float f) {
		float f1 = 15.0F / 16.0F;
		GL11.glScalef(f1, f1, f1);
	}

	public void drawFirstPersonHand() {
		this.modelBipedMain.onGround = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F / 16.0F);
		this.modelBipedMain.bipedRightArm.render(1.0F / 16.0F);
	}

	protected void func_22016_b(EntityPlayer entityplayer, double d, double d1, double d2) {
		if(entityplayer.isEntityAlive() && entityplayer.isPlayerSleeping()) {
			super.func_22012_b(entityplayer, d + (double)entityplayer.field_22063_x, d1 + (double)entityplayer.field_22062_y, d2 + (double)entityplayer.field_22061_z);
		} else {
			super.func_22012_b(entityplayer, d, d1, d2);
		}

	}

	protected void func_22017_a(EntityPlayer entityplayer, float f, float f1, float f2) {
		if(entityplayer.isEntityAlive() && entityplayer.isPlayerSleeping()) {
			GL11.glRotatef(entityplayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.getDeathMaxRotation(entityplayer), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(entityplayer, f, f1, f2);
		}

	}

	protected void passSpecialRender(EntityLiving entityliving, double d, double d1, double d2) {
		this.renderName((EntityPlayer)entityliving, d, d1, d2);
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		this.func_186_b((EntityPlayer)entityliving, f);
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.setArmorModel((EntityPlayer)entityliving, i, f);
	}

	protected void renderEquippedItems(EntityLiving entityliving, float f) {
		this.renderSpecials((EntityPlayer)entityliving, f);
	}

	protected void rotateCorpse(EntityLiving entityliving, float f, float f1, float f2) {
		this.func_22017_a((EntityPlayer)entityliving, f, f1, f2);
	}

	protected void func_22012_b(EntityLiving entityliving, double d, double d1, double d2) {
		this.func_22016_b((EntityPlayer)entityliving, d, d1, d2);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		this.renderPlayer((EntityPlayer)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.renderPlayer((EntityPlayer)entity, d, d1, d2, f, f1);
	}
}
