package net.minecraft.src;

import java.util.Random;
import net.minecraft.multiversion.VersionManager;

public class BlockFurnace extends BlockContainer {
	private Random furnaceRand = new Random();
	private final boolean isActive;
	private static boolean keepFurnaceInventory = false;

	protected BlockFurnace(int i, boolean flag) {
		super(i, Material.rock);
		this.isActive = flag;
		this.blockIndexInTexture = 45;
	}

	public int idDropped(int i, Random random) {
		return Block.stoneOvenIdle.blockID;
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		this.setDefaultDirection(world, i, j, k);
	}

	private void setDefaultDirection(World world, int i, int j, int k) {
		if(!world.multiplayerWorld) {
			int l = world.getBlockId(i, j, k - 1);
			int i1 = world.getBlockId(i, j, k + 1);
			int j1 = world.getBlockId(i - 1, j, k);
			int k1 = world.getBlockId(i + 1, j, k);
			byte byte0 = 3;
			if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
				byte0 = 3;
			}

			if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
				byte0 = 2;
			}

			if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
				byte0 = 5;
			}

			if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
				byte0 = 4;
			}

			world.setBlockMetadataWithNotify(i, j, k, byte0);
		}
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l != 1 && l != 0) {
			int i1 = iblockaccess.getBlockMetadata(i, j, k);
			return l != i1 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		} else {
			return VersionManager.version().protocol() < 8 && VersionManager.getMinecraft().gameSettings.versionGraphics ? 1 : this.blockIndexInTexture + 17;
		}
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if(this.isActive) {
			int l = world.getBlockMetadata(i, j, k);
			float f = (float)i + 0.5F;
			float f1 = (float)j + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = (float)k + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;
			if(l == 4) {
				world.spawnParticle("smoke", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
			} else if(l == 5) {
				world.spawnParticle("smoke", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
			} else if(l == 2) {
				world.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
			} else if(l == 3) {
				world.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
			}

		}
	}

	public int getBlockTextureFromSide(int i) {
		return i != 1 && i != 0 ? (i == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture) : (VersionManager.version().protocol() < 8 && VersionManager.getMinecraft().gameSettings.versionGraphics ? 1 : this.blockIndexInTexture + 17);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if(VersionManager.version().alpha() || !world.multiplayerWorld) {
			TileEntityFurnace tileentityfurnace = (TileEntityFurnace)world.getBlockTileEntity(i, j, k);
			entityplayer.displayGUIFurnace(tileentityfurnace);
		}

		return true;
	}

	public static void updateFurnaceBlockState(boolean flag, World world, int i, int j, int k) {
		int l = world.getBlockMetadata(i, j, k);
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		keepFurnaceInventory = true;
		if(flag) {
			world.setBlockWithNotify(i, j, k, Block.stoneOvenActive.blockID);
		} else {
			world.setBlockWithNotify(i, j, k, Block.stoneOvenIdle.blockID);
		}

		keepFurnaceInventory = false;
		world.setBlockMetadataWithNotify(i, j, k, l);
		tileentity.func_31004_j();
		world.setBlockTileEntity(i, j, k, tileentity);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0) {
			world.setBlockMetadataWithNotify(i, j, k, 2);
		}

		if(l == 1) {
			world.setBlockMetadataWithNotify(i, j, k, 5);
		}

		if(l == 2) {
			world.setBlockMetadataWithNotify(i, j, k, 3);
		}

		if(l == 3) {
			world.setBlockMetadataWithNotify(i, j, k, 4);
		}

	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		if(!keepFurnaceInventory) {
			TileEntityFurnace tileentityfurnace = (TileEntityFurnace)world.getBlockTileEntity(i, j, k);

			for(int l = 0; l < tileentityfurnace.getSizeInventory(); ++l) {
				ItemStack itemstack = tileentityfurnace.getStackInSlot(l);
				if(itemstack != null) {
					float f = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
					float f1 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
					float f2 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

					while(itemstack.stackSize > 0) {
						int i1 = this.furnaceRand.nextInt(21) + 10;
						if(i1 > itemstack.stackSize) {
							i1 = itemstack.stackSize;
						}

						itemstack.stackSize -= i1;
						EntityItem entityitem = new EntityItem(world, (double)((float)i + f), (double)((float)j + f1), (double)((float)k + f2), new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (double)((float)this.furnaceRand.nextGaussian() * f3);
						entityitem.motionY = (double)((float)this.furnaceRand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * f3);
						world.entityJoinedWorld(entityitem);
					}
				}
			}
		}

		super.onBlockRemoval(world, i, j, k);
	}
}
