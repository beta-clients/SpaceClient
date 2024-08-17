package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;

public class ItemFishingRod extends Item {
	public ItemFishingRod(int i) {
		super(i);
		this.setMaxDamage(64);
		this.setMaxStackSize(1);
	}

	public boolean isFull3D() {
		return true;
	}

	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(VersionManager.version().protocol() >= 6) {
			if(entityplayer.fishEntity != null) {
				int i = entityplayer.fishEntity.catchFish();
				itemstack.damageItem(i, entityplayer);
				entityplayer.swingItem();
			} else {
				world.playSoundAtEntity(entityplayer, "random.old_bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				if(!world.multiplayerWorld) {
					world.entityJoinedWorld(new EntityFish(world, entityplayer));
				}

				entityplayer.swingItem();
			}
		}

		return itemstack;
	}
}
