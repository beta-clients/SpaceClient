package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.src.Space.event.EventRegistry;
import net.minecraft.src.Space.event.events.EventHudUpdate;
import net.minecraft.src.Space.Client;

public class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	public String field_933_a = null;
	private int updateCounter = 0;
	private String recordPlaying = "";
	private int recordPlayingUpFor = 0;
	private boolean field_22065_l = false;
	public float damageGuiPartialTime;
	float prevVignetteBrightness = 1.0F;

	public GuiIngame(Minecraft minecraft) {
		this.mc = minecraft;
	}

	public void renderGameOverlay(float f, boolean flag, int i, int j) {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();
		FontRenderer fontrenderer = this.mc.fontRenderer;
		this.mc.entityRenderer.func_905_b();
		GL11.glEnable(GL11.GL_BLEND);
		if(Minecraft.isFancyGraphicsEnabled()) {
			this.renderVignette(this.mc.thePlayer.getEntityBrightness(f), k, l);
		}

		ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
		if(!this.mc.gameSettings.thirdPersonView && itemstack != null && itemstack.itemID == Block.pumpkin.blockID) {
			this.renderPumpkinBlur(k, l);
		}

		float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * f;
		if(f1 > 0.0F) {
			this.renderPortalOverlay(f1, k, l);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(k / 2 - 91, l - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(k / 2 - 91 - 1 + inventoryplayer.currentItem * 20, l - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		this.drawTexturedModalRect(k / 2 - 7, l / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
		boolean flag1 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			flag1 = false;
		}

		int i1 = this.mc.thePlayer.health;
		int j1 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int byte0;
		int flag2;
		int i5;
		if(this.mc.playerController.shouldDrawHUD()) {
			byte0 = this.mc.thePlayer.getPlayerArmorValue();

			int d;
			for(flag2 = 0; flag2 < 10; ++flag2) {
				i5 = l - 32;
				if(byte0 > 0) {
					d = k / 2 + 91 - flag2 * 8 - 9;
					if(flag2 * 2 + 1 < byte0) {
						this.drawTexturedModalRect(d, i5, 34, 9, 9, 9);
					}

					if(flag2 * 2 + 1 == byte0) {
						this.drawTexturedModalRect(d, i5, 25, 9, 9, 9);
					}

					if(flag2 * 2 + 1 > byte0) {
						this.drawTexturedModalRect(d, i5, 16, 9, 9, 9);
					}
				}

				byte var28 = 0;
				if(flag1) {
					var28 = 1;
				}

				int l6 = k / 2 - 91 + flag2 * 8;
				if(i1 <= 4) {
					i5 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(l6, i5, 16 + var28 * 9, 0, 9, 9);
				if(flag1) {
					if(flag2 * 2 + 1 < j1) {
						this.drawTexturedModalRect(l6, i5, 70, 0, 9, 9);
					}

					if(flag2 * 2 + 1 == j1) {
						this.drawTexturedModalRect(l6, i5, 79, 0, 9, 9);
					}
				}

				if(flag2 * 2 + 1 < i1) {
					this.drawTexturedModalRect(l6, i5, 52, 0, 9, 9);
				}

				if(flag2 * 2 + 1 == i1) {
					this.drawTexturedModalRect(l6, i5, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
				flag2 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				i5 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - flag2;

				for(d = 0; d < flag2 + i5; ++d) {
					if(d < flag2) {
						this.drawTexturedModalRect(k / 2 - 91 + d * 8, l - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(k / 2 - 91 + d * 8, l - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPushMatrix();
		GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		for(byte0 = 0; byte0 < 9; ++byte0) {
			flag2 = k / 2 - 90 + byte0 * 20 + 2;
			i5 = l - 16 - 3;
			this.renderInventorySlot(byte0, flag2, i5, f);
		}

		RenderHelper.disableStandardItemLighting();
		if(this.mc.gameSettings.showVersion && !this.mc.gameSettings.showDebugInfo) {
			fontrenderer.drawStringWithShadow("Minecraft " + VersionManager.version().name, 2, 2, 16777215);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		if(this.mc.thePlayer.func_22060_M() > 0) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			byte0 = this.mc.thePlayer.func_22060_M();
			float var27 = (float)byte0 / 100.0F;
			if(var27 > 1.0F) {
				var27 = 1.0F - (float)(byte0 - 100) / 10.0F;
			}

			i5 = (int)(220.0F * var27) << 24 | 1052704;
			this.drawRect(0, 0, k, l, i5);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		String s1;
		EventHudUpdate e = new EventHudUpdate();
		EventRegistry.handleEvent(e);
		if(this.mc.gameSettings.showDebugInfo) {
			GL11.glPushMatrix();
			if(Minecraft.hasPaidCheckTime > 0L) {
				GL11.glTranslatef(0.0F, 32.0F, 0.0F);
			}

			fontrenderer.drawStringWithShadow("Minecraft Beta 1.7.3 (" + this.mc.debug + ")", 2, 2, 16777215);
			fontrenderer.drawStringWithShadow(this.mc.func_6241_m(), 2, 12, 16777215);
			fontrenderer.drawStringWithShadow(this.mc.func_6262_n(), 2, 22, 16777215);
			fontrenderer.drawStringWithShadow(this.mc.func_6245_o(), 2, 32, 16777215);
			fontrenderer.drawStringWithShadow(this.mc.func_21002_o(), 2, 42, 16777215);
			long var24 = Runtime.getRuntime().maxMemory();
			long var29 = Runtime.getRuntime().totalMemory();
			long var30 = Runtime.getRuntime().freeMemory();
			long byte1 = var29 - var30;
			s1 = "Used memory: " + byte1 * 100L / var24 + "% (" + byte1 / 1024L / 1024L + "MB) of " + var24 / 1024L / 1024L + "MB";
			this.drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 2, 14737632);
			s1 = "Allocated memory: " + var29 * 100L / var24 + "% (" + var29 / 1024L / 1024L + "MB)";
			this.drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 12, 14737632);
			this.drawString(fontrenderer, "x: " + this.mc.thePlayer.posX, 2, 64, 14737632);
			this.drawString(fontrenderer, "y: " + this.mc.thePlayer.posY, 2, 72, 14737632);
			this.drawString(fontrenderer, "z: " + this.mc.thePlayer.posZ, 2, 80, 14737632);
			this.drawString(fontrenderer, "f: " + (MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3), 2, 88, 14737632);
			this.drawString(fontrenderer, "Seed: " + (VersionManager.version().protocol() > 2 ? Long.valueOf(this.mc.theWorld.getRandomSeed()) : "n/a"), 2, 100, 14737632);
			if(this.mc.theWorld.multiplayerWorld) {
				this.drawString(fontrenderer, "Protocol version: " + VersionManager.version().protocol() + " (" + VersionManager.version().name + ")", 2, 112, 14737632);
			}

			GL11.glPopMatrix();
		}
		else{
			fontrenderer.drawStringWithShadow("SpaceClient " + Client.currentVersion, 2, (mc.gameSettings.showVersion ? 10 : 0) + 2, 0xADD8E6);
		}
		if(this.recordPlayingUpFor > 0) {
			float var25 = (float)this.recordPlayingUpFor - f;
			flag2 = (int)(var25 * 256.0F / 20.0F);
			if(flag2 > 255) {
				flag2 = 255;
			}

			if(flag2 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(k / 2), (float)(l - 48), 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				i5 = 16777215;
				if(this.field_22065_l) {
					i5 = Color.HSBtoRGB(var25 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				fontrenderer.drawString(this.recordPlaying, -fontrenderer.getStringWidth(this.recordPlaying) / 2, -4, i5 + (flag2 << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}

		byte var26 = 10;
		boolean var31 = false;
		if(this.mc.currentScreen instanceof GuiChat) {
			var26 = 20;
			var31 = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(l - 48), 0.0F);

		for(i5 = 0; i5 < this.chatMessageList.size() && i5 < var26; ++i5) {
			if(((ChatLine)this.chatMessageList.get(i5)).updateCounter < 200 || var31) {
				double var32 = (double)((ChatLine)this.chatMessageList.get(i5)).updateCounter / 200.0D;
				var32 = 1.0D - var32;
				var32 *= 10.0D;
				if(var32 < 0.0D) {
					var32 = 0.0D;
				}

				if(var32 > 1.0D) {
					var32 = 1.0D;
				}

				var32 *= var32;
				int j6 = (int)(255.0D * var32);
				if(var31) {
					j6 = 255;
				}

				if(j6 > 0) {
					byte var33 = 2;
					int k6 = -i5 * 9;
					s1 = ((ChatLine)this.chatMessageList.get(i5)).message;
					this.drawRect(var33, k6 - 1, var33 + 320, k6 + 8, j6 / 2 << 24);
					GL11.glEnable(GL11.GL_BLEND);
					if(s1.startsWith("[Space]")){
						fontrenderer.drawStringWithShadow(s1, var33, k6, 0xADD8E6);
					}
					else{
						fontrenderer.drawStringWithShadow(s1, var33, k6, 16777215 + (j6 << 24));
					}
				}
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderPumpkinBlur(int i, int j) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, (double)j, -90.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV((double)i, (double)j, -90.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV((double)i, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderVignette(float f, int i, int j) {
		f = 1.0F - f;
		if(f < 0.0F) {
			f = 0.0F;
		}

		if(f > 1.0F) {
			f = 1.0F;
		}

		this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(f - this.prevVignetteBrightness) * 0.01D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, (double)j, -90.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV((double)i, (double)j, -90.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV((double)i, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void renderPortalOverlay(float f, int i, int j) {
		if(f < 1.0F) {
			f *= f;
			f *= f;
			f = f * 0.8F + 0.2F;
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png"));
		float f1 = (float)(Block.portal.blockIndexInTexture % 16) / 16.0F;
		float f2 = (float)(Block.portal.blockIndexInTexture / 16) / 16.0F;
		float f3 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16.0F;
		float f4 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16.0F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, (double)j, -90.0D, (double)f1, (double)f4);
		tessellator.addVertexWithUV((double)i, (double)j, -90.0D, (double)f3, (double)f4);
		tessellator.addVertexWithUV((double)i, 0.0D, -90.0D, (double)f3, (double)f2);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)f1, (double)f2);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderInventorySlot(int i, int j, int k, float f) {
		ItemStack itemstack = this.mc.thePlayer.inventory.mainInventory[i];
		if(itemstack != null) {
			float f1 = (float)itemstack.animationsToGo - f;
			if(f1 > 0.0F) {
				GL11.glPushMatrix();
				float f2 = 1.0F + f1 / 5.0F;
				GL11.glTranslatef((float)(j + 8), (float)(k + 12), 0.0F);
				GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float)(-(j + 8)), (float)(-(k + 12)), 0.0F);
			}

			itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, j, k);
			if(f1 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, j, k);
		}
	}

	public void updateTick() {
		if(this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		for(int i = 0; i < this.chatMessageList.size(); ++i) {
			++((ChatLine)this.chatMessageList.get(i)).updateCounter;
		}

	}

	public void clearChatMessages() {
		this.chatMessageList.clear();
	}

	public void addChatMessage(String s) {
		while(this.mc.fontRenderer.getStringWidth(s) > 320) {
			int i;
			for(i = 1; i < s.length() && this.mc.fontRenderer.getStringWidth(s.substring(0, i + 1)) <= 320; ++i) {
			}

			this.addChatMessage(s.substring(0, i));
			s = s.substring(i);
		}

		this.chatMessageList.add(0, new ChatLine(s));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}

	public void setRecordPlayingMessage(String s) {
		this.recordPlaying = "Now playing: " + s;
		this.recordPlayingUpFor = 60;
		this.field_22065_l = true;
	}

	public void addChatMessageTranslate(String s) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		String s1 = stringtranslate.translateKey(s);
		this.addChatMessage(s1);
	}
}
