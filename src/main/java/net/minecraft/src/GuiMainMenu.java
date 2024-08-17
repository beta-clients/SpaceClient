package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import net.minecraft.multiversion.Version;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.Space.Client;

public class GuiMainMenu extends GuiScreen {
	private static final Random rand = new Random();
	private float updateCounter = 0.0F;
	private String splashText = "missingno";
	private GuiButton multiplayerButton;

	public GuiMainMenu() {
		try {
			ArrayList arraylist = new ArrayList();
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
			String s = "";

			while(true) {
				String s1 = bufferedreader.readLine();
				if(s1 == null) {
					this.splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
					break;
				}

				s1 = s1.trim();
				if(s1.length() > 0) {
					arraylist.add(s1);
				}
			}
		} catch (Exception var5) {
		}

	}

	public void updateScreen() {
		++this.updateCounter;
	}

	protected void keyTyped(char c, int i) {
	}

	public void initGui() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if(calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
			this.splashText = "Happy birthday, ez!";
		} else if(calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
			this.splashText = "Happy birthday, Notch!";
		} else if(calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if(calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		}

		StringTranslate stringtranslate = StringTranslate.getInstance();
		int i = this.height / 4 + 48;
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, i, stringtranslate.translateKey("menu.singleplayer")));
		this.controlList.add(this.multiplayerButton = new GuiButton(2, this.width / 2 - 100, i + 24, stringtranslate.translateKey("menu.multiplayer")));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, i + 48, stringtranslate.translateKey("menu.mods")));
		if(this.mc.hideQuitButton) {
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, i + 72, stringtranslate.translateKey("menu.options")));
		} else {
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.options")));
			this.controlList.add(new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.quit")));
		}

		if(this.mc.session == null) {
			this.multiplayerButton.enabled = false;
		}

		RenderBlocks.fancyGrass = VersionManager.version().alpha() && this.mc.gameSettings.versionGraphics ? false : this.mc.gameSettings.fancyGraphics;
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(guibutton.id == 1) {
			VersionManager.instance.changeVersion(Version.BETA_173);
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if(guibutton.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if(guibutton.id == 3) {
			this.mc.displayGuiScreen(new GuiTexturePacks(this));
		}

		if(guibutton.id == 4) {
			this.mc.shutdown();
		}

	}

	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		Tessellator tessellator = Tessellator.instance;
		short c = 274;
		int k = this.width / 2 - c / 2;
		byte byte0 = 30;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(k + 0, byte0 + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(k + 155, byte0 + 0, 0, 45, 155, 44);
		tessellator.setColorOpaque_I(16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f1 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * 3.141593F * 2.0F) * 0.1F);
		f1 = f1 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
		GL11.glScalef(f1, f1, f1);
		this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
		GL11.glPopMatrix();
		this.drawString(this.fontRenderer, "SpaceClient " + Client.currentVersion, 2, 2, 5263440);
		String s = "Copyright Mojang AB. Do not distribute.";
		this.drawString(this.fontRenderer, s, this.width - this.fontRenderer.getStringWidth(s) - 2, this.height - 10, 16777215);
		super.drawScreen(i, j, f);
	}
}
