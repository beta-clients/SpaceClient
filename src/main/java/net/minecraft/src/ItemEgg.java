package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class ItemEgg extends Item {
	public ItemEgg(int i) {
		super(i);
		this.maxStackSize = 16;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(VersionManager.version().beta()) {
			--itemstack.stackSize;
			world.playSoundAtEntity(entityplayer, "random.old_bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if(!world.multiplayerWorld) {
				world.entityJoinedWorld(new EntityEgg(world, entityplayer));
			}
		}

		return itemstack;
	}
}
