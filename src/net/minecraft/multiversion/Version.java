package net.minecraft.multiversion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.protocol.Protocol;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TexturePackDefault;

public enum Version {
	BETA_173(Protocol.BETA_14, "Beta 1.7.3", ""),
	BETA_166(Protocol.BETA_13, "Beta 1.6.6", "/beta"),
	BETA_1502(Protocol.BETA_11, "Beta 1.5_02", "/beta"),
	BETA_1401(Protocol.BETA_10, "Beta 1.4_01", "/beta"),
	BETA_1301(Protocol.BETA_9, "Beta 1.3_01", "/beta"),
	BETA_1202(Protocol.BETA_8, "Beta 1.2_02", "/beta"),
	BETA_1(Protocol.BETA_7, "Beta 1.1_02", "/beta"),
	ALPHA_126(Protocol.ALPHA_6, "Alpha 1.2.6", "/alpha6"),
	ALPHA_123(Protocol.ALPHA_5, "Alpha 1.2.3", "/alpha6"),
	ALPHA_120(Protocol.ALPHA_3, "Alpha 1.2.0", "/alpha6"),
	ALPHA_112(Protocol.ALPHA_2, "Alpha 1.1.2_01", "/alpha2");

	public Protocol protocol;
	public String name;
	private String textures;
	public static Set<Version> alphaList = new HashSet();
	public static Set<Version> betaList = new HashSet();
	public static Set<Version> versionList = new HashSet();
	public static HashMap<Integer, Version> versionMap = new HashMap();

	private Version(Protocol protocol, String s, String s1) {
		this.protocol = protocol;
		this.name = s;
		this.textures = s1;
	}

	public int protocol() {
		return this.protocol.version();
	}

	public String textures() {
		Minecraft mc = VersionManager.getMinecraft();
		return mc.texturePackList.selectedTexturePack instanceof TexturePackDefault && mc.gameSettings.versionGraphics ? this.textures : "";
	}

	public void addRecipes(CraftingManager cm) {
		int p = VersionManager.version().protocol();
		if(p >= 6) {
			cm.addRecipe(new ItemStack(Item.pocketSundial, 1), new Object[]{" # ", "#X#", " # ", Character.valueOf('#'), Item.ingotGold, Character.valueOf('X'), Item.redstone});
		}

		if(p >= 8) {
			cm.addRecipe(new ItemStack(Block.dispenser, 1), new Object[]{"###", "#X#", "#R#", Character.valueOf('#'), Block.cobblestone, Character.valueOf('X'), Item.bow, Character.valueOf('R'), Item.redstone});
			cm.addRecipe(new ItemStack(Block.torchWood, 4), new Object[]{"X", "#", Character.valueOf('X'), new ItemStack(Item.coal, 1, 1), Character.valueOf('#'), Item.stick});
			cm.addRecipe(new ItemStack(Item.cake, 1), new Object[]{"AAA", "BEB", "CCC", Character.valueOf('A'), Item.bucketMilk, Character.valueOf('B'), Item.sugar, Character.valueOf('C'), Item.wheat, Character.valueOf('E'), Item.egg});
			cm.addRecipe(new ItemStack(Item.sugar, 1), new Object[]{"#", Character.valueOf('#'), Item.reed});
			cm.addRecipe(new ItemStack(Block.musicBlock, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Item.redstone});
		}

		if(p >= 9) {
			cm.addRecipe(new ItemStack(Item.bed, 1), new Object[]{"###", "XXX", Character.valueOf('#'), Block.cloth, Character.valueOf('X'), Block.planks});
			cm.addRecipe(new ItemStack(Item.redstoneRepeater, 1), new Object[]{"#X#", "III", Character.valueOf('#'), Block.torchRedstoneActive, Character.valueOf('X'), Item.redstone, Character.valueOf('I'), Block.stone});
			cm.addRecipe(new ItemStack(Block.stairSingle, 3, 3), new Object[]{"###", Character.valueOf('#'), Block.cobblestone});
			cm.addRecipe(new ItemStack(Block.stairSingle, 3, 1), new Object[]{"###", Character.valueOf('#'), Block.sandStone});
			cm.addRecipe(new ItemStack(Block.stairSingle, 3, 2), new Object[]{"###", Character.valueOf('#'), Block.planks});
		}

		if(p >= 11) {
			cm.addRecipe(new ItemStack(Block.railPowered, 6), new Object[]{"X X", "X#X", "XRX", Character.valueOf('X'), Item.ingotGold, Character.valueOf('R'), Item.redstone, Character.valueOf('#'), Item.stick});
			cm.addRecipe(new ItemStack(Block.railDetector, 6), new Object[]{"X X", "X#X", "XRX", Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), Item.redstone, Character.valueOf('#'), Block.pressurePlateStone});
		}

		if(p >= 13) {
			cm.addRecipe(new ItemStack(Block.trapdoor, 2), new Object[]{"###", "###", Character.valueOf('#'), Block.planks});
			cm.addRecipe(new ItemStack(Item.mapItem, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Item.paper, Character.valueOf('X'), Item.compass});
		}

		if(p == 14) {
			cm.addRecipe(new ItemStack(Block.pistonBase, 1), new Object[]{"TTT", "#X#", "#R#", Character.valueOf('#'), Block.cobblestone, Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), Item.redstone, Character.valueOf('T'), Block.planks});
			cm.addRecipe(new ItemStack(Block.pistonStickyBase, 1), new Object[]{"S", "P", Character.valueOf('S'), Item.slimeBall, Character.valueOf('P'), Block.pistonBase});
		}

	}

	public boolean alpha() {
		return this.protocol() < 7;
	}

	public boolean beta() {
		return this.protocol() > 6;
	}

	static {
		betaList.add(BETA_173);
		betaList.add(BETA_166);
		betaList.add(BETA_1502);
		betaList.add(BETA_1401);
		betaList.add(BETA_1301);
		betaList.add(BETA_1202);
		betaList.add(BETA_1);
		alphaList.add(ALPHA_126);
		alphaList.add(ALPHA_123);
		alphaList.add(ALPHA_120);
		alphaList.add(ALPHA_112);
		versionList.addAll(alphaList);
		versionList.addAll(betaList);
		Iterator var0 = versionList.iterator();

		while(var0.hasNext()) {
			Version v = (Version)var0.next();
			versionMap.put(Integer.valueOf(v.protocol()), v);
		}

	}
}
