package net.minecraft.src;

public enum EnumOptions {
	MUSIC("MUSIC", 0, "options.music", true, false),
	SOUND("SOUND", 1, "options.sound", true, false),
	INVERT_MOUSE("INVERT_MOUSE", 2, "options.invertMouse", false, true),
	SENSITIVITY("SENSITIVITY", 3, "options.sensitivity", true, false),
	RENDER_DISTANCE("RENDER_DISTANCE", 4, "options.renderDistance", false, false),
	VIEW_BOBBING("VIEW_BOBBING", 5, "options.viewBobbing", false, true),
	ANAGLYPH("ANAGLYPH", 6, "options.anaglyph", false, true),
	ADVANCED_OPENGL("ADVANCED_OPENGL", 7, "options.advancedOpengl", false, true),
	FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", false, false),
	DIFFICULTY("DIFFICULTY", 9, "options.difficulty", false, false),
	GRAPHICS("GRAPHICS", 10, "options.graphics", false, false),
	AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 11, "options.ao", false, true),
	GUI_SCALE("GUI_SCALE", 12, "options.guiScale", false, false),
	SHOW_VERSION("SHOW_VERSION", 13, "Show Version", false, true),
	VERSION_GRAPHICS("VERSION_GRAPHICS", 14, "Version Graphics", false, true);

	private final boolean enumFloat;
	private final boolean enumBoolean;
	private final String enumString;
	private static final EnumOptions[] field_20141_n = new EnumOptions[]{MUSIC, SOUND, INVERT_MOUSE, SENSITIVITY, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, ADVANCED_OPENGL, FRAMERATE_LIMIT, DIFFICULTY, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, SHOW_VERSION, VERSION_GRAPHICS};

	public static EnumOptions getEnumOptions(int i) {
		EnumOptions[] aenumoptions = values();
		int j = aenumoptions.length;

		for(int k = 0; k < j; ++k) {
			EnumOptions enumoptions = aenumoptions[k];
			if(enumoptions.returnEnumOrdinal() == i) {
				return enumoptions;
			}
		}

		return null;
	}

	private EnumOptions(String s, int i, String s1, boolean flag, boolean flag1) {
		this.enumString = s1;
		this.enumFloat = flag;
		this.enumBoolean = flag1;
	}

	public boolean getEnumFloat() {
		return this.enumFloat;
	}

	public boolean getEnumBoolean() {
		return this.enumBoolean;
	}

	public int returnEnumOrdinal() {
		return this.ordinal();
	}

	public String getEnumString() {
		return this.enumString;
	}
}
