package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class RecipesFood {
	public void addRecipes(CraftingManager craftingmanager) {
		craftingmanager.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty});
		craftingmanager.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", Character.valueOf('X'), Block.mushroomRed, Character.valueOf('Y'), Block.mushroomBrown, Character.valueOf('#'), Item.bowlEmpty});
		if(VersionManager.version().protocol() >= 10) {
			craftingmanager.addRecipe(new ItemStack(Item.cookie, 8), new Object[]{"#X#", Character.valueOf('X'), new ItemStack(Item.dyePowder, 1, 3), Character.valueOf('#'), Item.wheat});
		}

	}
}
