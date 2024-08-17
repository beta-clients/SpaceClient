package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;

public class BlockLeaves extends BlockLeavesBase {
	private int baseIndexInPNG;
	int[] adjacentTreeBlocks;

	protected BlockLeaves(int i, int j) {
		super(i, j, Material.leaves, false);
		this.baseIndexInPNG = j;
		this.setTickOnLoad(true);
	}

	public int getRenderColor(int i) {
		return VersionManager.version().protocol() <= 2 && VersionManager.getMinecraft().gameSettings.versionGraphics ? super.getRenderColor(i) : ((i & 1) == 1 && VersionManager.version().protocol() > 7 ? ColorizerFoliage.getFoliageColorPine() : ((i & 2) == 2 && VersionManager.version().protocol() > 7 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.func_31073_c()));
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		if(VersionManager.version().protocol() <= 2 && VersionManager.getMinecraft().gameSettings.versionGraphics) {
			return super.colorMultiplier(iblockaccess, i, j, k);
		} else {
			int l = iblockaccess.getBlockMetadata(i, j, k);
			if((l & 1) == 1 && VersionManager.version().protocol() > 7) {
				return ColorizerFoliage.getFoliageColorPine();
			} else if((l & 2) == 2 && VersionManager.version().protocol() > 7) {
				return ColorizerFoliage.getFoliageColorBirch();
			} else {
				iblockaccess.getWorldChunkManager().func_4069_a(i, k, 1, 1);
				double d = iblockaccess.getWorldChunkManager().temperature[0];
				double d1 = iblockaccess.getWorldChunkManager().humidity[0];
				return ColorizerFoliage.getFoliageColor(d, d1);
			}
		}
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		byte l = 1;
		int i1 = l + 1;
		if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
			for(int j1 = -l; j1 <= l; ++j1) {
				for(int k1 = -l; k1 <= l; ++k1) {
					for(int l1 = -l; l1 <= l; ++l1) {
						int i2 = world.getBlockId(i + j1, j + k1, k + l1);
						if(i2 == Block.leaves.blockID) {
							int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
							world.setBlockMetadata(i + j1, j + k1, k + l1, j2 | 8);
						}
					}
				}
			}
		}

	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if(!world.multiplayerWorld) {
			int l = world.getBlockMetadata(i, j, k);
			if((l & 8) != 0) {
				byte byte0 = 4;
				int i1 = byte0 + 1;
				byte byte1 = 32;
				int j1 = byte1 * byte1;
				int k1 = byte1 / 2;
				if(this.adjacentTreeBlocks == null) {
					this.adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
				}

				int j2;
				if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
					j2 = -byte0;

					label112:
					while(true) {
						int l2;
						int j3;
						int l3;
						if(j2 > byte0) {
							j2 = 1;

							while(true) {
								if(j2 > 4) {
									break label112;
								}

								for(l2 = -byte0; l2 <= byte0; ++l2) {
									for(j3 = -byte0; j3 <= byte0; ++j3) {
										for(l3 = -byte0; l3 <= byte0; ++l3) {
											if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1] == j2 - 1) {
												if(this.adjacentTreeBlocks[(l2 + k1 - 1) * j1 + (j3 + k1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1 - 1) * j1 + (j3 + k1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 - 1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 - 1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 - 1)] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 - 1)] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1 + 1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1 + 1] = j2;
												}
											}
										}
									}
								}

								++j2;
							}
						}

						for(l2 = -byte0; l2 <= byte0; ++l2) {
							for(j3 = -byte0; j3 <= byte0; ++j3) {
								l3 = world.getBlockId(i + j2, j + l2, k + j3);
								if(l3 == Block.wood.blockID) {
									this.adjacentTreeBlocks[(j2 + k1) * j1 + (l2 + k1) * byte1 + j3 + k1] = 0;
								} else if(l3 == Block.leaves.blockID) {
									this.adjacentTreeBlocks[(j2 + k1) * j1 + (l2 + k1) * byte1 + j3 + k1] = -2;
								} else {
									this.adjacentTreeBlocks[(j2 + k1) * j1 + (l2 + k1) * byte1 + j3 + k1] = -1;
								}
							}
						}

						++j2;
					}
				}

				j2 = this.adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
				if(j2 >= 0) {
					world.setBlockMetadata(i, j, k, l & -9);
				} else {
					this.removeLeaves(world, i, j, k);
				}
			}

		}
	}

	private void removeLeaves(World world, int i, int j, int k) {
		this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
		world.setBlockWithNotify(i, j, k, 0);
	}

	public int quantityDropped(Random random) {
		return random.nextInt(20) != 0 ? 0 : 1;
	}

	public int idDropped(int i, Random random) {
		return Block.sapling.blockID;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		if(!world.multiplayerWorld && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.leaves.blockID, 1, l & 3));
		} else {
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}

	}

	protected int damageDropped(int i) {
		return i & 3;
	}

	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return VersionManager.version().protocol() < 8 ? this.blockIndexInTexture : ((j & 3) == 1 ? this.blockIndexInTexture + 80 : this.blockIndexInTexture);
	}

	public void setGraphicsLevel(boolean flag) {
		this.graphicsLevel = flag;
		this.blockIndexInTexture = this.baseIndexInPNG + (flag ? 0 : 1);
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		super.onEntityWalking(world, i, j, k, entity);
	}
}
