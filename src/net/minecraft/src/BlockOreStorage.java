package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class BlockOreStorage extends Block {
	public BlockOreStorage(int i, int j) {
		super(i, Material.iron);
		this.blockIndexInTexture = j;
	}

	public int getBlockTextureFromSide(int i) {
		return VersionManager.version().protocol() == 2 && VersionManager.getMinecraft().gameSettings.versionGraphics ? (i == 0 ? this.blockIndexInTexture + 32 : (i == 1 ? this.blockIndexInTexture : this.blockIndexInTexture + 16)) : this.blockIndexInTexture;
	}
}
