package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievements extends GuiScreen {
	private static final int field_27126_s = AchievementList.minDisplayColumn * 24 - 112;
	private static final int field_27125_t = AchievementList.minDisplayRow * 24 - 112;
	private static final int field_27124_u = AchievementList.maxDisplayColumn * 24 - 77;
	private static final int field_27123_v = AchievementList.maxDisplayRow * 24 - 77;
	protected int field_27121_a = 256;
	protected int field_27119_i = 202;
	protected int field_27118_j = 0;
	protected int field_27117_l = 0;
	protected double field_27116_m;
	protected double field_27115_n;
	protected double field_27114_o;
	protected double field_27113_p;
	protected double field_27112_q;
	protected double field_27111_r;
	private int field_27122_w = 0;
	private StatFileWriter field_27120_x;

	public GuiAchievements(StatFileWriter statfilewriter) {
		this.field_27120_x = statfilewriter;
		short c = 141;
		short c1 = 141;
		this.field_27116_m = this.field_27114_o = this.field_27112_q = (double)(AchievementList.openInventory.displayColumn * 24 - c / 2 - 12);
		this.field_27115_n = this.field_27113_p = this.field_27111_r = (double)(AchievementList.openInventory.displayRow * 24 - c1 / 2);
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiSmallButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, StatCollector.translateToLocal("gui.done")));
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

		super.actionPerformed(guibutton);
	}

	protected void keyTyped(char c, int i) {
		if(i == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		} else {
			super.keyTyped(c, i);
		}

	}

	public void drawScreen(int i, int j, float f) {
		if(Mouse.isButtonDown(0)) {
			int k = (this.width - this.field_27121_a) / 2;
			int l = (this.height - this.field_27119_i) / 2;
			int i1 = k + 8;
			int j1 = l + 17;
			if((this.field_27122_w == 0 || this.field_27122_w == 1) && i >= i1 && i < i1 + 224 && j >= j1 && j < j1 + 155) {
				if(this.field_27122_w == 0) {
					this.field_27122_w = 1;
				} else {
					this.field_27114_o -= (double)(i - this.field_27118_j);
					this.field_27113_p -= (double)(j - this.field_27117_l);
					this.field_27112_q = this.field_27116_m = this.field_27114_o;
					this.field_27111_r = this.field_27115_n = this.field_27113_p;
				}

				this.field_27118_j = i;
				this.field_27117_l = j;
			}

			if(this.field_27112_q < (double)field_27126_s) {
				this.field_27112_q = (double)field_27126_s;
			}

			if(this.field_27111_r < (double)field_27125_t) {
				this.field_27111_r = (double)field_27125_t;
			}

			if(this.field_27112_q >= (double)field_27124_u) {
				this.field_27112_q = (double)(field_27124_u - 1);
			}

			if(this.field_27111_r >= (double)field_27123_v) {
				this.field_27111_r = (double)(field_27123_v - 1);
			}
		} else {
			this.field_27122_w = 0;
		}

		this.drawDefaultBackground();
		this.func_27109_b(i, j, f);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.func_27110_k();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void updateScreen() {
		this.field_27116_m = this.field_27114_o;
		this.field_27115_n = this.field_27113_p;
		double d = this.field_27112_q - this.field_27114_o;
		double d1 = this.field_27111_r - this.field_27113_p;
		if(d * d + d1 * d1 < 4.0D) {
			this.field_27114_o += d;
			this.field_27113_p += d1;
		} else {
			this.field_27114_o += d * 0.85D;
			this.field_27113_p += d1 * 0.85D;
		}

	}

	protected void func_27110_k() {
		int i = (this.width - this.field_27121_a) / 2;
		int j = (this.height - this.field_27119_i) / 2;
		this.fontRenderer.drawString("Achievements", i + 15, j + 5, 4210752);
	}

	protected void func_27109_b(int i, int j, float f) {
		int k = MathHelper.floor_double(this.field_27116_m + (this.field_27114_o - this.field_27116_m) * (double)f);
		int l = MathHelper.floor_double(this.field_27115_n + (this.field_27113_p - this.field_27115_n) * (double)f);
		if(k < field_27126_s) {
			k = field_27126_s;
		}

		if(l < field_27125_t) {
			l = field_27125_t;
		}

		if(k >= field_27124_u) {
			k = field_27124_u - 1;
		}

		if(l >= field_27123_v) {
			l = field_27123_v - 1;
		}

		int i1 = this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png");
		int j1 = this.mc.renderEngine.getTexture("/achievement/bg.png");
		int k1 = (this.width - this.field_27121_a) / 2;
		int l1 = (this.height - this.field_27119_i) / 2;
		int i2 = k1 + 16;
		int j2 = l1 + 17;
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		this.mc.renderEngine.bindTexture(i1);
		int k2 = k + 288 >> 4;
		int i3 = l + 288 >> 4;
		int j3 = (k + 288) % 16;
		int i4 = (l + 288) % 16;
		Random random = new Random();

		int achievement;
		int achievement2;
		int s;
		int s1;
		for(achievement = 0; achievement * 16 - i4 < 155; ++achievement) {
			float renderitem = 0.6F - (float)(i3 + achievement) / 25.0F * 0.3F;
			GL11.glColor4f(renderitem, renderitem, renderitem, 1.0F);

			for(achievement2 = 0; achievement2 * 16 - j3 < 224; ++achievement2) {
				random.setSeed((long)(1234 + k2 + achievement2));
				random.nextInt();
				s = random.nextInt(1 + i3 + achievement) + (i3 + achievement) / 2;
				s1 = Block.sand.blockIndexInTexture;
				if(s <= 37 && i3 + achievement != 35) {
					if(s == 22) {
						if(random.nextInt(2) == 0) {
							s1 = Block.oreDiamond.blockIndexInTexture;
						} else {
							s1 = Block.oreRedstone.blockIndexInTexture;
						}
					} else if(s == 10) {
						s1 = Block.oreIron.blockIndexInTexture;
					} else if(s == 8) {
						s1 = Block.oreCoal.blockIndexInTexture;
					} else if(s > 4) {
						s1 = Block.stone.blockIndexInTexture;
					} else if(s > 0) {
						s1 = Block.dirt.blockIndexInTexture;
					}
				} else {
					s1 = Block.bedrock.blockIndexInTexture;
				}

				this.drawTexturedModalRect(i2 + achievement2 * 16 - j3, j2 + achievement * 16 - i4, s1 % 16 << 4, s1 >> 4 << 4, 16, 16);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		int k5;
		int k7;
		int var32;
		for(achievement = 0; achievement < AchievementList.achievementList.size(); ++achievement) {
			Achievement var28 = (Achievement)AchievementList.achievementList.get(achievement);
			if(var28.parentAchievement != null) {
				achievement2 = var28.displayColumn * 24 - k + 11 + i2;
				s = var28.displayRow * 24 - l + 11 + j2;
				s1 = var28.parentAchievement.displayColumn * 24 - k + 11 + i2;
				k5 = var28.parentAchievement.displayRow * 24 - l + 11 + j2;
				boolean j6 = false;
				boolean i7 = this.field_27120_x.hasAchievementUnlocked(var28);
				boolean s2 = this.field_27120_x.func_27181_b(var28);
				k7 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0D * Math.PI * 2.0D) <= 0.6D ? 130 : 255;
				if(i7) {
					var32 = -9408400;
				} else if(s2) {
					var32 = '\uff00' + (k7 << 24);
				} else {
					var32 = -16777216;
				}

				this.func_27100_a(achievement2, s1, s, var32);
				this.func_27099_b(s1, s, k5, var32);
			}
		}

		Achievement var27 = null;
		RenderItem var29 = new RenderItem();
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		int var33;
		for(achievement2 = 0; achievement2 < AchievementList.achievementList.size(); ++achievement2) {
			Achievement var30 = (Achievement)AchievementList.achievementList.get(achievement2);
			s1 = var30.displayColumn * 24 - k;
			k5 = var30.displayRow * 24 - l;
			if(s1 >= -24 && k5 >= -24 && s1 <= 224 && k5 <= 155) {
				float var35;
				if(this.field_27120_x.hasAchievementUnlocked(var30)) {
					var35 = 1.0F;
					GL11.glColor4f(var35, var35, var35, 1.0F);
				} else if(this.field_27120_x.func_27181_b(var30)) {
					var35 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0D * Math.PI * 2.0D) >= 0.6D ? 0.8F : 0.6F;
					GL11.glColor4f(var35, var35, var35, 1.0F);
				} else {
					var35 = 0.3F;
					GL11.glColor4f(var35, var35, var35, 1.0F);
				}

				this.mc.renderEngine.bindTexture(j1);
				var32 = i2 + s1;
				var33 = j2 + k5;
				if(var30.getSpecial()) {
					this.drawTexturedModalRect(var32 - 2, var33 - 2, 26, 202, 26, 26);
				} else {
					this.drawTexturedModalRect(var32 - 2, var33 - 2, 0, 202, 26, 26);
				}

				if(!this.field_27120_x.func_27181_b(var30)) {
					float var36 = 0.1F;
					GL11.glColor4f(var36, var36, var36, 1.0F);
					var29.field_27004_a = false;
				}

				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_CULL_FACE);
				var29.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var30.theItemStack, var32 + 3, var33 + 3);
				GL11.glDisable(GL11.GL_LIGHTING);
				if(!this.field_27120_x.func_27181_b(var30)) {
					var29.field_27004_a = true;
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(i >= i2 && j >= j2 && i < i2 + 224 && j < j2 + 155 && i >= var32 && i <= var32 + 22 && j >= var33 && j <= var33 + 22) {
					var27 = var30;
				}
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(j1);
		this.drawTexturedModalRect(k1, l1, 0, 0, this.field_27121_a, this.field_27119_i);
		GL11.glPopMatrix();
		this.zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(i, j, f);
		if(var27 != null) {
			String var31 = var27.statName;
			String var34 = var27.getDescription();
			k5 = i + 12;
			var32 = j - 4;
			if(this.field_27120_x.func_27181_b(var27)) {
				var33 = Math.max(this.fontRenderer.getStringWidth(var31), 120);
				int var37 = this.fontRenderer.func_27277_a(var34, var33);
				if(this.field_27120_x.hasAchievementUnlocked(var27)) {
					var37 += 12;
				}

				this.drawGradientRect(k5 - 3, var32 - 3, k5 + var33 + 3, var32 + var37 + 3 + 12, -1073741824, -1073741824);
				this.fontRenderer.func_27278_a(var34, k5, var32 + 12, var33, -6250336);
				if(this.field_27120_x.hasAchievementUnlocked(var27)) {
					this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("achievement.taken"), k5, var32 + var37 + 4, -7302913);
				}
			} else {
				var33 = Math.max(this.fontRenderer.getStringWidth(var31), 120);
				String var38 = StatCollector.translateToLocalFormatted("achievement.requires", new Object[]{var27.parentAchievement.statName});
				k7 = this.fontRenderer.func_27277_a(var38, var33);
				this.drawGradientRect(k5 - 3, var32 - 3, k5 + var33 + 3, var32 + k7 + 12 + 3, -1073741824, -1073741824);
				this.fontRenderer.func_27278_a(var38, k5, var32 + 12, var33, -9416624);
			}

			this.fontRenderer.drawStringWithShadow(var31, k5, var32, this.field_27120_x.func_27181_b(var27) ? (var27.getSpecial() ? -128 : -1) : (var27.getSpecial() ? -8355776 : -8355712));
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
}
