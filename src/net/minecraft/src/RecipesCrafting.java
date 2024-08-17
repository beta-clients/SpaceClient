package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class RecipesCrafting {
	public void addRecipes(CraftingManager craftingmanager) {
		craftingmanager.addRecipe(new ItemStack(Block.chest), new Object[]{"###", "# #", "###", Character.valueOf('#'), Block.planks});
		craftingmanager.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[]{"###", "# #", "###", Character.valueOf('#'), Block.cobblestone});
		craftingmanager.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", Character.valueOf('#'), Block.planks});
		if(VersionManager.version().protocol() >= 8) {
			craftingmanager.addRecipe(new ItemStack(Block.sandStone), new Object[]{"##", "##", Character.valueOf('#'), Block.sand});
		}

	}
}
