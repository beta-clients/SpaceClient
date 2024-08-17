package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class GuiVideoSettings extends GuiScreen {
	private GuiScreen field_22110_h;
	protected String field_22107_a = "Video Settings";
	private GameSettings guiGameSettings;
	private static EnumOptions[] field_22108_k = new EnumOptions[]{EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.SHOW_VERSION, EnumOptions.VERSION_GRAPHICS};

	public GuiVideoSettings(GuiScreen guiscreen, GameSettings gamesettings) {
		this.field_22110_h = guiscreen;
		this.guiGameSettings = gamesettings;
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.field_22107_a = stringtranslate.translateKey("options.videoTitle");
		int i = 0;
		EnumOptions[] aenumoptions = field_22108_k;
		int j = aenumoptions.length;

		for(int k = 0; k < j; ++k) {
			EnumOptions enumoptions = aenumoptions[k];
			if(!enumoptions.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), enumoptions, this.guiGameSettings.getKeyBinding(enumoptions)));
			} else {
				this.controlList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), enumoptions, this.guiGameSettings.getKeyBinding(enumoptions), this.guiGameSettings.getOptionFloatValue(enumoptions)));
			}

			++i;
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, stringtranslate.translateKey("gui.done")));
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.enabled) {
			if(guibutton.id < 100 && guibutton instanceof GuiSmallButton) {
				this.guiGameSettings.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), 1);
				guibutton.displayString = this.guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
				if(EnumOptions.getEnumOptions(guibutton.id) == EnumOptions.GRAPHICS) {
					RenderBlocks.fancyGrass = VersionManager.version().alpha() && this.mc.gameSettings.versionGraphics ? false : this.mc.gameSettings.fancyGraphics;
				}
			}

			if(guibutton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.field_22110_h);
			}

			ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int i = scaledresolution.getScaledWidth();
			int j = scaledresolution.getScaledHeight();
			this.setWorldAndResolution(this.mc, i, j);
		}
	}

	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.field_22107_a, this.width / 2, 20, 16777215);
		super.drawScreen(i, j, f);
	}
}
