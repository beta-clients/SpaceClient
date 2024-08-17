package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

class SlotArmor extends Slot {
	final int armorType;
	final ContainerPlayer inventory;

	SlotArmor(ContainerPlayer containerplayer, IInventory iinventory, int i, int j, int k, int l) {
		super(iinventory, i, j, k);
		this.inventory = containerplayer;
		this.armorType = l;
	}

	public int getSlotStackLimit() {
		return 1;
	}

	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemArmor ? ((ItemArmor)itemstack.getItem()).armorType == this.armorType : (itemstack.getItem().shiftedIndex == Block.pumpkin.blockID ? this.armorType == 0 : false);
	}

	public int getBackgroundIconIndex() {
		return VersionManager.version().alpha() && VersionManager.getMinecraft().gameSettings.versionGraphics ? 15 + this.armorType * 16 : -1;
	}
}
