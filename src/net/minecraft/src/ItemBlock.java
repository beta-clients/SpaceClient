package net.minecraft.src;

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i) {
		super(i);
		this.blockID = i + 256;
		this.setIconIndex(Block.blocksList[i + 256].getBlockTextureFromSide(2));
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		if(world.getBlockId(i, j, k) == Block.snow.blockID) {
			l = 0;
		} else {
			if(l == 0) {
				--j;
			}

			if(l == 1) {
				++j;
			}

			if(l == 2) {
				--k;
			}

			if(l == 3) {
				++k;
			}

			if(l == 4) {
				--i;
			}

			if(l == 5) {
				++i;
			}
		}

		if(itemstack.stackSize == 0) {
			return false;
		} else if(j == 127 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if(world.canBlockBePlacedAt(this.blockID, i, j, k, false, l)) {
			Block block = Block.blocksList[this.blockID];
			if(world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, this.getPlacedBlockMetadata(itemstack.getItemDamage()))) {
				Block.blocksList[this.blockID].onBlockPlaced(world, i, j, k, l);
				Block.blocksList[this.blockID].onBlockPlacedBy(world, i, j, k, entityplayer);
				world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), block.stepSound.func_1145_d(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				--itemstack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public String getItemNameIS(ItemStack itemstack) {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public String getItemName() {
		return Block.blocksList[this.blockID].getBlockName();
	}
}
