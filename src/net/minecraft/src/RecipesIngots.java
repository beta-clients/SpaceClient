package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class RecipesIngots {
	private Object[][] recipeItems = new Object[][]{{Block.blockGold, new ItemStack(Item.ingotGold, 9)}, {Block.blockSteel, new ItemStack(Item.ingotIron, 9)}, {Block.blockDiamond, new ItemStack(Item.diamond, 9)}, {Block.blockLapis, new ItemStack(Item.dyePowder, 9, 4)}};

	public void addRecipes(CraftingManager craftingmanager) {
		for(int i = 0; i < this.recipeItems.length && (i != 3 || VersionManager.version().protocol() >= 8); ++i) {
			Block block = (Block)this.recipeItems[i][0];
			ItemStack itemstack = (ItemStack)this.recipeItems[i][1];
			craftingmanager.addRecipe(new ItemStack(block), new Object[]{"###", "###", "###", Character.valueOf('#'), itemstack});
			craftingmanager.addRecipe(itemstack, new Object[]{"#", Character.valueOf('#'), block});
		}

	}
}
