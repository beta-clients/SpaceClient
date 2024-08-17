package net.minecraft.src;

public class ItemSnowball extends Item {
	public ItemSnowball(int i) {
		super(i);
		this.maxStackSize = 16;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		--itemstack.stackSize;
		world.playSoundAtEntity(entityplayer, "random.old_bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if(!world.multiplayerWorld) {
			world.entityJoinedWorld(new EntitySnowball(world, entityplayer));
		}

		return itemstack;
	}
}
