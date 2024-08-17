package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.multiversion.VersionManager;

public class FurnaceRecipes {
	private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();
	private Map smeltingList;

	public static final FurnaceRecipes smelting() {
		return smeltingBase;
	}

	private FurnaceRecipes() {
		this.loadRecipes();
	}

	public void loadRecipes() {
		this.smeltingList = new HashMap();
		this.addSmelting(Block.oreIron.blockID, new ItemStack(Item.ingotIron));
		this.addSmelting(Block.oreGold.blockID, new ItemStack(Item.ingotGold));
		this.addSmelting(Block.oreDiamond.blockID, new ItemStack(Item.diamond));
		this.addSmelting(Block.sand.blockID, new ItemStack(Block.glass));
		this.addSmelting(Item.porkRaw.shiftedIndex, new ItemStack(Item.porkCooked));
		this.addSmelting(Item.fishRaw.shiftedIndex, new ItemStack(Item.fishCooked));
		this.addSmelting(Block.cobblestone.blockID, new ItemStack(Block.stone));
		this.addSmelting(Item.clay.shiftedIndex, new ItemStack(Item.brick));
		this.addSmelting(Block.cactus.blockID, new ItemStack(Item.dyePowder, 1, 2));
		if(VersionManager.version().protocol() >= 8) {
			this.addSmelting(Block.wood.blockID, new ItemStack(Item.coal, 1, 1));
		}

	}

	public void addSmelting(int i, ItemStack itemstack) {
		this.smeltingList.put(Integer.valueOf(i), itemstack);
	}

	public ItemStack getSmeltingResult(int i) {
		return (ItemStack)this.smeltingList.get(Integer.valueOf(i));
	}

	public Map getSmeltingList() {
		return this.smeltingList;
	}
}
