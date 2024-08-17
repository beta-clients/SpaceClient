package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class RecipesTools {
	private String[][] recipePatterns = new String[][]{{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
	private Object[][] recipeItems = new Object[][]{{Block.planks, Block.cobblestone, Item.ingotIron, Item.diamond, Item.ingotGold}, {Item.pickaxeWood, Item.pickaxeStone, Item.pickaxeSteel, Item.pickaxeDiamond, Item.pickaxeGold}, {Item.shovelWood, Item.shovelStone, Item.shovelSteel, Item.shovelDiamond, Item.shovelGold}, {Item.axeWood, Item.axeStone, Item.axeSteel, Item.axeDiamond, Item.axeGold}, {Item.hoeWood, Item.hoeStone, Item.hoeSteel, Item.hoeDiamond, Item.hoeGold}};

	public void addRecipes(CraftingManager craftingmanager) {
		for(int i = 0; i < this.recipeItems[0].length; ++i) {
			Object obj = this.recipeItems[0][i];

			for(int j = 0; j < this.recipeItems.length - 1; ++j) {
				Item item = (Item)this.recipeItems[j + 1][i];
				craftingmanager.addRecipe(new ItemStack(item), new Object[]{this.recipePatterns[j], Character.valueOf('#'), Item.stick, Character.valueOf('X'), obj});
			}
		}

		if(VersionManager.version().protocol() == 14) {
			craftingmanager.addRecipe(new ItemStack(Item.shears), new Object[]{" #", "# ", Character.valueOf('#'), Item.ingotIron});
		}

	}
}
