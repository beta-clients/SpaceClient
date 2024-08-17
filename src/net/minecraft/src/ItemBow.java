package net.minecraft.src;

public class ItemBow extends Item {
	public ItemBow(int i) {
		super(i);
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(entityplayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world.playSoundAtEntity(entityplayer, "random.old_bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if(!world.multiplayerWorld) {
				world.entityJoinedWorld(new EntityArrow(world, entityplayer));
			}
		}

		return itemstack;
	}
}
