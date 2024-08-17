package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;

public class PlayerControllerMP extends PlayerController {
	private int currentBlockX = -1;
	private int currentBlockY = -1;
	private int currentblockZ = -1;
	private float curBlockDamageMP = 0.0F;
	private float prevBlockDamageMP = 0.0F;
	private float field_9441_h = 0.0F;
	private int blockHitDelay = 0;
	private boolean isHittingBlock = false;
	private NetClientHandler netClientHandler;
	private int currentPlayerItem = 0;

	public PlayerControllerMP(Minecraft minecraft, NetClientHandler netclienthandler) {
		super(minecraft);
		this.netClientHandler = netclienthandler;
	}

	public void flipPlayer(EntityPlayer entityplayer) {
		entityplayer.rotationYaw = -180.0F;
	}

	public boolean sendBlockRemoved(int i, int j, int k, int l) {
		if(VersionManager.version().protocol() <= 8) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(3, i, j, k, l));
		}

		int i1 = this.mc.theWorld.getBlockId(i, j, k);
		int j1 = this.mc.theWorld.getBlockMetadata(i, j, k);
		boolean flag = super.sendBlockRemoved(i, j, k, l);
		ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
		if(itemstack != null) {
			itemstack.onDestroyBlock(i1, i, j, k, this.mc.thePlayer);
			if(itemstack.stackSize == 0) {
				itemstack.func_1097_a(this.mc.thePlayer);
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(flag && this.mc.thePlayer.canHarvestBlock(Block.blocksList[i1]) && VersionManager.version().alpha()) {
			Block.blocksList[i1].onBlockDestroyedByPlayer(this.mc.theWorld, i, j, k, j1);
		}

		return flag;
	}

	public void clickBlock(int i, int j, int k, int l) {
		if(!this.isHittingBlock || i != this.currentBlockX || j != this.currentBlockY || k != this.currentblockZ) {
			this.syncCurrentPlayItem();
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, i, j, k, l));
			int i1 = this.mc.theWorld.getBlockId(i, j, k);
			if(i1 > 0 && this.curBlockDamageMP == 0.0F) {
				Block.blocksList[i1].onBlockClicked(this.mc.theWorld, i, j, k, this.mc.thePlayer);
			}

			if(i1 > 0 && Block.blocksList[i1].blockStrength(this.mc.thePlayer) >= 1.0F) {
				this.sendBlockRemoved(i, j, k, l);
			} else {
				this.isHittingBlock = true;
				this.currentBlockX = i;
				this.currentBlockY = j;
				this.currentblockZ = k;
				this.curBlockDamageMP = 0.0F;
				this.prevBlockDamageMP = 0.0F;
				this.field_9441_h = 0.0F;
			}
		}

	}

	public void resetBlockRemoving() {
		if(VersionManager.version().protocol() <= 8) {
			if(this.isHittingBlock) {
				this.isHittingBlock = false;
				this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, 0, 0, 0, 0));
				this.curBlockDamageMP = 0.0F;
				this.blockHitDelay = 0;
			}
		} else {
			this.curBlockDamageMP = 0.0F;
			this.isHittingBlock = false;
		}
	}

	public void sendBlockRemoving(int i, int j, int k, int l) {
		if(VersionManager.version().protocol() <= 8) {
			this.netClientHandler.addToSendQueue(new Packet14BlockDig(1, i, j, k, l));
		}

		if(this.isHittingBlock) {
			this.syncCurrentPlayItem();
			if(this.blockHitDelay > 0) {
				--this.blockHitDelay;
			} else {
				if(i == this.currentBlockX && j == this.currentBlockY && k == this.currentblockZ) {
					int i1 = this.mc.theWorld.getBlockId(i, j, k);
					if(i1 == 0) {
						this.isHittingBlock = false;
						return;
					}

					Block block = Block.blocksList[i1];
					this.curBlockDamageMP += block.blockStrength(this.mc.thePlayer);
					if(this.field_9441_h % 4.0F == 0.0F && block != null) {
						this.mc.sndManager.playSound(block.stepSound.func_1145_d(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8.0F, block.stepSound.getPitch() * 0.5F);
					}

					++this.field_9441_h;
					if(this.curBlockDamageMP >= 1.0F) {
						this.isHittingBlock = false;
						if(VersionManager.version().protocol() > 8) {
							this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, i, j, k, l));
						}

						this.sendBlockRemoved(i, j, k, l);
						this.curBlockDamageMP = 0.0F;
						this.prevBlockDamageMP = 0.0F;
						this.field_9441_h = 0.0F;
						this.blockHitDelay = 5;
					}
				} else {
					this.clickBlock(i, j, k, l);
				}

			}
		}
	}

	public void setPartialTime(float f) {
		if(this.curBlockDamageMP <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float f1 = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * f;
			this.mc.ingameGUI.damageGuiPartialTime = f1;
			this.mc.renderGlobal.damagePartialTime = f1;
		}

	}

	public float getBlockReachDistance() {
		return 4.0F;
	}

	public void func_717_a(World world) {
		super.func_717_a(world);
	}

	public void updateController() {
		this.syncCurrentPlayItem();
		this.prevBlockDamageMP = this.curBlockDamageMP;
		this.mc.sndManager.playRandomMusicIfReady();
	}

	private void syncCurrentPlayItem() {
		ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();
		int i = 0;
		if(VersionManager.version().alpha()) {
			if(itemstack != null) {
				i = itemstack.itemID;
			}

			if(i != this.currentPlayerItem) {
				this.currentPlayerItem = i;
				this.netClientHandler.addToSendQueue(new net.minecraft.protocol.alpha.Packet16BlockItemSwitch(0, this.currentPlayerItem));
			}
		} else {
			i = this.mc.thePlayer.inventory.currentItem;
			if(i != this.currentPlayerItem) {
				this.currentPlayerItem = i;
				this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.currentPlayerItem));
			}
		}

	}

	public boolean sendPlaceBlock(EntityPlayer entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l) {
		this.syncCurrentPlayItem();
		if(VersionManager.version().alpha()) {
			this.netClientHandler.addToSendQueue(new net.minecraft.protocol.alpha.Packet15Place(itemstack == null ? -1 : itemstack.itemID, i, j, k, l));
		} else {
			this.netClientHandler.addToSendQueue(new Packet15Place(i, j, k, l, entityplayer.inventory.getCurrentItem()));
		}

		boolean flag = super.sendPlaceBlock(entityplayer, world, itemstack, i, j, k, l);
		return flag;
	}

	public boolean sendUseItem(EntityPlayer entityplayer, World world, ItemStack itemstack) {
		this.syncCurrentPlayItem();
		if(VersionManager.version().alpha()) {
			this.netClientHandler.addToSendQueue(new net.minecraft.protocol.alpha.Packet15Place(itemstack == null ? -1 : itemstack.itemID, -1, -1, -1, 255));
		} else {
			this.netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, entityplayer.inventory.getCurrentItem()));
		}

		boolean flag = super.sendUseItem(entityplayer, world, itemstack);
		return flag;
	}

	public EntityPlayer createPlayer(World world) {
		return new EntityClientPlayerMP(this.mc, world, this.mc.session, this.netClientHandler);
	}

	public void attackEntity(EntityPlayer entityplayer, Entity entity) {
		this.syncCurrentPlayItem();
		if(VersionManager.version().protocol() > 3) {
			this.netClientHandler.addToSendQueue(new Packet7UseEntity(entityplayer.entityId, entity.entityId, 1));
		}

		entityplayer.attackTargetEntityWithCurrentItem(entity);
	}

	public void interactWithEntity(EntityPlayer entityplayer, Entity entity) {
		this.syncCurrentPlayItem();
		if(VersionManager.version().protocol() > 3) {
			this.netClientHandler.addToSendQueue(new Packet7UseEntity(entityplayer.entityId, entity.entityId, 0));
		}

		entityplayer.useCurrentItemOnEntity(entity);
	}

	public ItemStack func_27174_a(int i, int j, int k, boolean flag, EntityPlayer entityplayer) {
		short word0 = entityplayer.craftingInventory.func_20111_a(entityplayer.inventory);
		ItemStack itemstack = super.func_27174_a(i, j, k, flag, entityplayer);
		if(VersionManager.version().beta()) {
			if(VersionManager.version().protocol() < 11) {
				this.netClientHandler.addToSendQueue(new net.minecraft.protocol.beta.Packet102WindowClick(i, j, k, itemstack, word0));
			} else {
				this.netClientHandler.addToSendQueue(new Packet102WindowClick(i, j, k, flag, itemstack, word0));
			}
		}

		return itemstack;
	}

	public void func_20086_a(int i, EntityPlayer entityplayer) {
		if(i != -9999) {
		}
	}
}
