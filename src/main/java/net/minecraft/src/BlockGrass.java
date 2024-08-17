package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;

public class BlockGrass extends Block {
	protected BlockGrass(int i) {
		super(i, Material.grassMaterial);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l == 1) {
			return 0;
		} else if(l == 0) {
			return 2;
		} else {
			Material material = iblockaccess.getBlockMaterial(i, j + 1, k);
			return material != Material.snow && material != Material.builtSnow ? 3 : 68;
		}
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		if(VersionManager.version().protocol() == 2 && VersionManager.getMinecraft().gameSettings.versionGraphics) {
			return super.colorMultiplier(iblockaccess, i, j, k);
		} else {
			iblockaccess.getWorldChunkManager().func_4069_a(i, k, 1, 1);
			double d = iblockaccess.getWorldChunkManager().temperature[0];
			double d1 = iblockaccess.getWorldChunkManager().humidity[0];
			return ColorizerGrass.getGrassColor(d, d1);
		}
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if(!world.multiplayerWorld) {
			if(world.getBlockLightValue(i, j + 1, k) < 4 && Block.lightOpacity[world.getBlockId(i, j + 1, k)] > 2) {
				if(random.nextInt(4) != 0) {
					return;
				}

				world.setBlockWithNotify(i, j, k, Block.dirt.blockID);
			} else if(world.getBlockLightValue(i, j + 1, k) >= 9) {
				int l = i + random.nextInt(3) - 1;
				int i1 = j + random.nextInt(5) - 3;
				int j1 = k + random.nextInt(3) - 1;
				int k1 = world.getBlockId(l, i1 + 1, j1);
				if(world.getBlockId(l, i1, j1) == Block.dirt.blockID && world.getBlockLightValue(l, i1 + 1, j1) >= 4 && Block.lightOpacity[k1] <= 2) {
					world.setBlockWithNotify(l, i1, j1, Block.grass.blockID);
				}
			}

		}
	}

	public int idDropped(int i, Random random) {
		return Block.dirt.idDropped(0, random);
	}
}
