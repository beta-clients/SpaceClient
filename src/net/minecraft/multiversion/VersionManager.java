package net.minecraft.multiversion;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.protocol.Protocol;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.RenderBlocks;

public class VersionManager {
	public Version version;
	public static final VersionManager instance = new VersionManager();
	public Minecraft mc;

	public VersionManager() {
		this.setVersion(Version.BETA_173);
	}

	public void setVersion(Version v) {
		Protocol.addPackets(v.protocol);
		this.version = v;
	}

	public void changeVersion(Version v) {
		RenderBlocks.fancyGrass = v.alpha() && this.mc.gameSettings.versionGraphics ? false : this.mc.gameSettings.fancyGraphics;
		if(this.version != v) {
			this.setVersion(v);
			CraftingManager.getInstance().loadRecipes();
			FurnaceRecipes.smelting().loadRecipes();
		}
	}

	public static void setMinecraft(Minecraft minecraft) {
		instance.mc = minecraft;
		addSound("newsound/random/old_door_open.ogg", minecraft);
		addSound("newsound/random/old_door_close.ogg", minecraft);
		addSound("newsound/random/old_bow.ogg", minecraft);
	}

	public static void addSound(String s, Minecraft minecraft) {
		File mcDir = Minecraft.getMinecraftDir();
		File file = new File(mcDir, "resources/" + s);
		if(file.exists()) {
			minecraft.installResource(s, file);
			System.out.println("Loaded sound: " + s);
		} else {
			System.out.println("Failed to load sound: " + s);
		}

	}

	public static Minecraft getMinecraft() {
		return instance.mc;
	}

	public static Version version() {
		return instance.version;
	}
}
