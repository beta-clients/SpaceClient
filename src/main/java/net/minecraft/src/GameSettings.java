package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.input.Keyboard;

public class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
	private static final String[] DIFFICULTIES = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
	private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
	private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean advancedOpengl = false;
	public int limitFramerate = 1;
	public boolean fancyGraphics = true;
	public boolean ambientOcclusion = true;
	public String skin = "Default";
	public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
	public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
	public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
	public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
	public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
	public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
	public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
	public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
	public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", 33);
	public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog};
	protected Minecraft mc;
	private File optionsFile;
	public int difficulty = 2;
	public boolean hideGUI = false;
	public boolean thirdPersonView = false;
	public boolean showDebugInfo = false;
	public String lastServer = "";
	public boolean field_22275_C = false;
	public boolean smoothCamera = false;
	public boolean field_22273_E = false;
	public float field_22272_F = 1.0F;
	public float field_22271_G = 1.0F;
	public int guiScale = 0;
	public boolean showVersion = false;
	public boolean versionGraphics = true;

	public GameSettings(Minecraft minecraft, File file) {
		this.mc = minecraft;
		this.optionsFile = new File(file, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
	}

	public String getKeyBindingDescription(int i) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		return stringtranslate.translateKey(this.keyBindings[i].keyDescription);
	}

	public String getOptionDisplayString(int i) {
		return Keyboard.getKeyName(this.keyBindings[i].keyCode);
	}

	public void setKeyBinding(int i, int j) {
		this.keyBindings[i].keyCode = j;
		this.saveOptions();
	}

	public void setOptionFloatValue(EnumOptions enumoptions, float f) {
		if(enumoptions == EnumOptions.MUSIC) {
			this.musicVolume = f;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(enumoptions == EnumOptions.SOUND) {
			this.soundVolume = f;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(enumoptions == EnumOptions.SENSITIVITY) {
			this.mouseSensitivity = f;
		}

	}

	public void setOptionValue(EnumOptions enumoptions, int i) {
		if(enumoptions == EnumOptions.INVERT_MOUSE) {
			this.invertMouse = !this.invertMouse;
		}

		if(enumoptions == EnumOptions.RENDER_DISTANCE) {
			this.renderDistance = this.renderDistance + i & 3;
		}

		if(enumoptions == EnumOptions.GUI_SCALE) {
			this.guiScale = this.guiScale + i & 3;
		}

		if(enumoptions == EnumOptions.VIEW_BOBBING) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(enumoptions == EnumOptions.ADVANCED_OPENGL) {
			this.advancedOpengl = !this.advancedOpengl;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumoptions == EnumOptions.ANAGLYPH) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(enumoptions == EnumOptions.FRAMERATE_LIMIT) {
			this.limitFramerate = (this.limitFramerate + i + 3) % 3;
		}

		if(enumoptions == EnumOptions.DIFFICULTY) {
			this.difficulty = this.difficulty + i & 3;
		}

		if(enumoptions == EnumOptions.GRAPHICS) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumoptions == EnumOptions.AMBIENT_OCCLUSION) {
			this.ambientOcclusion = !this.ambientOcclusion;
			this.mc.renderGlobal.loadRenderers();
		}

		if(enumoptions == EnumOptions.SHOW_VERSION) {
			this.showVersion = !this.showVersion;
		}

		if(enumoptions == EnumOptions.VERSION_GRAPHICS) {
			this.versionGraphics = !this.versionGraphics;
			RenderBlocks.fancyGrass = VersionManager.version().alpha() && this.mc.gameSettings.versionGraphics ? false : this.mc.gameSettings.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		this.saveOptions();
	}

	public float getOptionFloatValue(EnumOptions enumoptions) {
		return enumoptions == EnumOptions.MUSIC ? this.musicVolume : (enumoptions == EnumOptions.SOUND ? this.soundVolume : (enumoptions == EnumOptions.SENSITIVITY ? this.mouseSensitivity : 0.0F));
	}

	public boolean getOptionOrdinalValue(EnumOptions enumoptions) {
		switch(EnumOptionsMappingHelper.enumOptionsMappingHelperArray[enumoptions.ordinal()]) {
		case 1:
			return this.invertMouse;
		case 2:
			return this.viewBobbing;
		case 3:
			return this.anaglyph;
		case 4:
			return this.advancedOpengl;
		case 5:
			return this.ambientOcclusion;
		case 6:
			return this.showVersion;
		case 7:
			return this.versionGraphics;
		default:
			return false;
		}
	}

	public String getKeyBinding(EnumOptions enumoptions) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		String s = stringtranslate.translateKey(enumoptions.getEnumString()) + ": ";
		if(enumoptions.getEnumFloat()) {
			float flag1 = this.getOptionFloatValue(enumoptions);
			return enumoptions == EnumOptions.SENSITIVITY ? (flag1 == 0.0F ? s + stringtranslate.translateKey("options.sensitivity.min") : (flag1 == 1.0F ? s + stringtranslate.translateKey("options.sensitivity.max") : s + (int)(flag1 * 200.0F) + "%")) : (flag1 == 0.0F ? s + stringtranslate.translateKey("options.off") : s + (int)(flag1 * 100.0F) + "%");
		} else if(enumoptions.getEnumBoolean()) {
			boolean flag = this.getOptionOrdinalValue(enumoptions);
			return flag ? s + stringtranslate.translateKey("options.on") : s + stringtranslate.translateKey("options.off");
		} else {
			return enumoptions == EnumOptions.RENDER_DISTANCE ? s + stringtranslate.translateKey(RENDER_DISTANCES[this.renderDistance]) : (enumoptions == EnumOptions.DIFFICULTY ? s + stringtranslate.translateKey(DIFFICULTIES[this.difficulty]) : (enumoptions == EnumOptions.GUI_SCALE ? s + stringtranslate.translateKey(GUISCALES[this.guiScale]) : (enumoptions == EnumOptions.FRAMERATE_LIMIT ? s + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]) : (enumoptions == EnumOptions.GRAPHICS ? (this.fancyGraphics ? s + stringtranslate.translateKey("options.graphics.fancy") : s + stringtranslate.translateKey("options.graphics.fast")) : s))));
		}
	}

	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				return;
			}

			BufferedReader exception = new BufferedReader(new FileReader(this.optionsFile));
			String s = "";

			while(true) {
				s = exception.readLine();
				if(s == null) {
					exception.close();
					break;
				}

				try {
					String[] exception1 = s.split(":");
					if(exception1[0].equals("music")) {
						this.musicVolume = this.parseFloat(exception1[1]);
					}

					if(exception1[0].equals("sound")) {
						this.soundVolume = this.parseFloat(exception1[1]);
					}

					if(exception1[0].equals("mouseSensitivity")) {
						this.mouseSensitivity = this.parseFloat(exception1[1]);
					}

					if(exception1[0].equals("invertYMouse")) {
						this.invertMouse = exception1[1].equals("true");
					}

					if(exception1[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(exception1[1]);
					}

					if(exception1[0].equals("guiScale")) {
						this.guiScale = Integer.parseInt(exception1[1]);
					}

					if(exception1[0].equals("bobView")) {
						this.viewBobbing = exception1[1].equals("true");
					}

					if(exception1[0].equals("anaglyph3d")) {
						this.anaglyph = exception1[1].equals("true");
					}

					if(exception1[0].equals("advancedOpengl")) {
						this.advancedOpengl = exception1[1].equals("true");
					}

					if(exception1[0].equals("fpsLimit")) {
						this.limitFramerate = Integer.parseInt(exception1[1]);
					}

					if(exception1[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(exception1[1]);
					}

					if(exception1[0].equals("fancyGraphics")) {
						this.fancyGraphics = exception1[1].equals("true");
					}

					if(exception1[0].equals("ao")) {
						this.ambientOcclusion = exception1[1].equals("true");
					}

					if(exception1[0].equals("skin")) {
						this.skin = exception1[1];
					}

					if(exception1[0].equals("lastServer") && exception1.length >= 2) {
						this.lastServer = exception1[1];
					}

					if(exception1[0].equals("showVersion")) {
						this.showVersion = exception1[1].equals("true");
					}

					if(exception1[0].equals("versionGraphics")) {
						this.versionGraphics = exception1[1].equals("true");
					}

					for(int i = 0; i < this.keyBindings.length; ++i) {
						if(exception1[0].equals("key_" + this.keyBindings[i].keyDescription)) {
							this.keyBindings[i].keyCode = Integer.parseInt(exception1[1]);
						}
					}
				} catch (Exception var5) {
					System.out.println("Skipping bad option: " + s);
				}
			}
		} catch (Exception var6) {
			System.out.println("Failed to load options");
			var6.printStackTrace();
		}

	}

	private float parseFloat(String s) {
		return s.equals("true") ? 1.0F : (s.equals("false") ? 0.0F : Float.parseFloat(s));
	}

	public void saveOptions() {
		try {
			PrintWriter exception = new PrintWriter(new FileWriter(this.optionsFile));
			exception.println("music:" + this.musicVolume);
			exception.println("sound:" + this.soundVolume);
			exception.println("invertYMouse:" + this.invertMouse);
			exception.println("mouseSensitivity:" + this.mouseSensitivity);
			exception.println("viewDistance:" + this.renderDistance);
			exception.println("guiScale:" + this.guiScale);
			exception.println("bobView:" + this.viewBobbing);
			exception.println("anaglyph3d:" + this.anaglyph);
			exception.println("advancedOpengl:" + this.advancedOpengl);
			exception.println("fpsLimit:" + this.limitFramerate);
			exception.println("difficulty:" + this.difficulty);
			exception.println("fancyGraphics:" + this.fancyGraphics);
			exception.println("ao:" + this.ambientOcclusion);
			exception.println("skin:" + this.skin);
			exception.println("lastServer:" + this.lastServer);
			exception.println("showVersion:" + this.showVersion);
			exception.println("versionGraphics:" + this.versionGraphics);

			for(int i = 0; i < this.keyBindings.length; ++i) {
				exception.println("key_" + this.keyBindings[i].keyDescription + ":" + this.keyBindings[i].keyCode);
			}

			exception.close();
		} catch (Exception var3) {
			System.out.println("Failed to save options");
			var3.printStackTrace();
		}

	}
}
