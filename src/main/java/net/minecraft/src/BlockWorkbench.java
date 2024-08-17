package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class BlockWorkbench extends Block {
	protected BlockWorkbench(int i) {
		super(i, Material.wood);
		this.blockIndexInTexture = 59;
	}

	public int getBlockTextureFromSide(int i) {
		return i == 1 ? this.blockIndexInTexture - 16 : (i == 0 ? Block.planks.getBlockTextureFromSide(0) : (i != 2 && i != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 1));
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if(VersionManager.version().alpha() || !world.multiplayerWorld) {
			entityplayer.displayWorkbenchGUI(i, j, k);
		}

		return true;
	}
}
