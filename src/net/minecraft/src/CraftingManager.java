package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.minecraft.multiversion.VersionManager;

public class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	private List recipes;

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		this.loadRecipes();
	}

	public void loadRecipes() {
		this.recipes = new ArrayList();
		(new RecipesTools()).addRecipes(this);
		(new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		(new RecipesFood()).addRecipes(this);
		(new RecipesCrafting()).addRecipes(this);
		(new RecipesArmor()).addRecipes(this);
		(new RecipesDyes()).addRecipes(this);
		this.addRecipe(new ItemStack(Item.paper, 3), new Object[]{"###", Character.valueOf('#'), Item.reed});
		this.addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", Character.valueOf('#'), Item.paper});
		this.addRecipe(new ItemStack(Block.fence, 2), new Object[]{"###", "###", Character.valueOf('#'), Item.stick});
		this.addRecipe(new ItemStack(Block.jukebox, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.diamond});
		this.addRecipe(new ItemStack(Block.bookShelf, 1), new Object[]{"###", "XXX", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.book});
		this.addRecipe(new ItemStack(Block.blockSnow, 1), new Object[]{"##", "##", Character.valueOf('#'), Item.snowball});
		this.addRecipe(new ItemStack(Block.blockClay, 1), new Object[]{"##", "##", Character.valueOf('#'), Item.clay});
		this.addRecipe(new ItemStack(Block.brick, 1), new Object[]{"##", "##", Character.valueOf('#'), Item.brick});
		this.addRecipe(new ItemStack(Block.glowStone, 1), new Object[]{"##", "##", Character.valueOf('#'), Item.lightStoneDust});
		this.addRecipe(new ItemStack(Block.cloth, 1), new Object[]{"##", "##", Character.valueOf('#'), Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", Character.valueOf('X'), Item.gunpowder, Character.valueOf('#'), Block.sand});
		this.addRecipe(new ItemStack(Block.stairSingle, 3, 0), new Object[]{"###", Character.valueOf('#'), Block.stone});
		this.addRecipe(new ItemStack(Block.ladder, 2), new Object[]{"# #", "###", "# #", Character.valueOf('#'), Item.stick});
		this.addRecipe(new ItemStack(Item.doorWood, 1), new Object[]{"##", "##", "##", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Item.doorSteel, 1), new Object[]{"##", "##", "##", Character.valueOf('#'), Item.ingotIron});
		this.addRecipe(new ItemStack(Item.sign, 1), new Object[]{"###", "###", " X ", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.stick});
		this.addRecipe(new ItemStack(Block.planks, 4), new Object[]{"#", Character.valueOf('#'), Block.wood});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.torchWood, 4), new Object[]{"X", "#", Character.valueOf('X'), Item.coal, Character.valueOf('#'), Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.rail, 16), new Object[]{"X X", "X#X", "X X", Character.valueOf('X'), Item.ingotIron, Character.valueOf('#'), Item.stick});
		this.addRecipe(new ItemStack(Item.minecartEmpty, 1), new Object[]{"# #", "###", Character.valueOf('#'), Item.ingotIron});
		this.addRecipe(new ItemStack(Block.pumpkinLantern, 1), new Object[]{"A", "B", Character.valueOf('A'), Block.pumpkin, Character.valueOf('B'), Block.torchWood});
		this.addRecipe(new ItemStack(Item.minecartCrate, 1), new Object[]{"A", "B", Character.valueOf('A'), Block.chest, Character.valueOf('B'), Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.minecartPowered, 1), new Object[]{"A", "B", Character.valueOf('A'), Block.stoneOvenIdle, Character.valueOf('B'), Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.boat, 1), new Object[]{"# #", "###", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Item.bucketEmpty, 1), new Object[]{"# #", " # ", Character.valueOf('#'), Item.ingotIron});
		this.addRecipe(new ItemStack(Item.flintAndSteel, 1), new Object[]{"A ", " B", Character.valueOf('A'), Item.ingotIron, Character.valueOf('B'), Item.flint});
		this.addRecipe(new ItemStack(Item.bread, 1), new Object[]{"###", Character.valueOf('#'), Item.wheat});
		this.addRecipe(new ItemStack(Block.stairCompactPlanks, 4), new Object[]{"#  ", "## ", "###", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Item.fishingRod, 1), new Object[]{"  #", " #X", "# X", Character.valueOf('#'), Item.stick, Character.valueOf('X'), Item.silk});
		this.addRecipe(new ItemStack(Block.stairCompactCobblestone, 4), new Object[]{"#  ", "## ", "###", Character.valueOf('#'), Block.cobblestone});
		this.addRecipe(new ItemStack(Item.painting, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Item.stick, Character.valueOf('X'), Block.cloth});
		this.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Block.blockGold, Character.valueOf('X'), Item.appleRed});
		this.addRecipe(new ItemStack(Block.lever, 1), new Object[]{"X", "#", Character.valueOf('#'), Block.cobblestone, Character.valueOf('X'), Item.stick});
		this.addRecipe(new ItemStack(Block.torchRedstoneActive, 1), new Object[]{"X", "#", Character.valueOf('#'), Item.stick, Character.valueOf('X'), Item.redstone});
		this.addRecipe(new ItemStack(Item.compass, 1), new Object[]{" # ", "#X#", " # ", Character.valueOf('#'), Item.ingotIron, Character.valueOf('X'), Item.redstone});
		this.addRecipe(new ItemStack(Block.button, 1), new Object[]{"#", "#", Character.valueOf('#'), Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlateStone, 1), new Object[]{"##", Character.valueOf('#'), Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlatePlanks, 1), new Object[]{"##", Character.valueOf('#'), Block.planks});
		VersionManager.version().addRecipes(this);
		Collections.sort(this.recipes, new RecipeSorter(this));
		System.out.println(this.recipes.size() + " recipes");
	}

	public void addRecipe(ItemStack itemstack, Object[] aobj) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		if(aobj[i] instanceof String[]) {
			String[] var11 = (String[])((String[])aobj[i++]);

			for(int aitemstack = 0; aitemstack < var11.length; ++aitemstack) {
				String i1 = var11[aitemstack];
				++k;
				j = i1.length();
				s = s + i1;
			}
		} else {
			while(aobj[i] instanceof String) {
				String hashmap = (String)aobj[i++];
				++k;
				j = hashmap.length();
				s = s + hashmap;
			}
		}

		HashMap var12;
		for(var12 = new HashMap(); i < aobj.length; i += 2) {
			Character var13 = (Character)aobj[i];
			ItemStack var15 = null;
			if(aobj[i + 1] instanceof Item) {
				var15 = new ItemStack((Item)aobj[i + 1]);
			} else if(aobj[i + 1] instanceof Block) {
				var15 = new ItemStack((Block)aobj[i + 1], 1, -1);
			} else if(aobj[i + 1] instanceof ItemStack) {
				var15 = (ItemStack)aobj[i + 1];
			}

			var12.put(var13, var15);
		}

		ItemStack[] var14 = new ItemStack[j * k];

		for(int var16 = 0; var16 < j * k; ++var16) {
			char c = s.charAt(var16);
			if(var12.containsKey(Character.valueOf(c))) {
				var14[var16] = ((ItemStack)var12.get(Character.valueOf(c))).copy();
			} else {
				var14[var16] = null;
			}
		}

		this.recipes.add(new ShapedRecipes(j, k, var14, itemstack));
	}

	void addShapelessRecipe(ItemStack itemstack, Object[] aobj) {
		ArrayList arraylist = new ArrayList();
		Object[] aobj1 = aobj;
		int i = aobj.length;

		for(int j = 0; j < i; ++j) {
			Object obj = aobj1[j];
			if(obj instanceof ItemStack) {
				arraylist.add(((ItemStack)obj).copy());
			} else if(obj instanceof Item) {
				arraylist.add(new ItemStack((Item)obj));
			} else {
				if(!(obj instanceof Block)) {
					throw new RuntimeException("Invalid shapeless recipy!");
				}

				arraylist.add(new ItemStack((Block)obj));
			}
		}

		this.recipes.add(new ShapelessRecipes(itemstack, arraylist));
	}

	public ItemStack findMatchingRecipe(InventoryCrafting inventorycrafting) {
		for(int i = 0; i < this.recipes.size(); ++i) {
			IRecipe irecipe = (IRecipe)this.recipes.get(i);
			if(irecipe.matches(inventorycrafting)) {
				return irecipe.getCraftingResult(inventorycrafting);
			}
		}

		return null;
	}

	public List getRecipeList() {
		return this.recipes;
	}
}
