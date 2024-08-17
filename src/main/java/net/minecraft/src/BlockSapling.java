package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;

public class BlockSapling extends BlockFlower {
	protected BlockSapling(int i, int j) {
		super(i, j);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if(!world.multiplayerWorld) {
			super.updateTick(world, i, j, k, random);
			if(world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0) {
				int l = world.getBlockMetadata(i, j, k);
				if((l & 8) == 0) {
					world.setBlockMetadataWithNotify(i, j, k, l | 8);
				} else {
					this.growTree(world, i, j, k, random);
				}
			}

		}
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if(VersionManager.version().protocol() < 11) {
			return super.getBlockTextureFromSideAndMetadata(i, j);
		} else {
			j &= 3;
			return j == 1 ? 63 : (j == 2 ? 79 : super.getBlockTextureFromSideAndMetadata(i, j));
		}
	}

	public void growTree(World world, int i, int j, int k, Random random) {
		int l = world.getBlockMetadata(i, j, k) & 3;
		world.setBlock(i, j, k, 0);
		Object obj = null;
		if(l == 1) {
			obj = new WorldGenTaiga2();
		} else if(l == 2) {
			obj = new WorldGenForest();
		} else {
			obj = new WorldGenTrees();
			if(random.nextInt(10) == 0) {
				obj = new WorldGenBigTree();
			}
		}

		if(!((WorldGenerator)((WorldGenerator)obj)).generate(world, random, i, j, k)) {
			world.setBlockAndMetadata(i, j, k, this.blockID, l);
		}

	}

	protected int damageDropped(int i) {
		return i & 3;
	}
}
