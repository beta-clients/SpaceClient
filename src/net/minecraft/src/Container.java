package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.multiversion.VersionManager;

public abstract class Container {
	public List field_20123_d = new ArrayList();
	public List slots = new ArrayList();
	public int windowId = 0;
	private short field_20917_a = 0;
	protected List field_20121_g = new ArrayList();
	private Set field_20918_b = new HashSet();

	protected void addSlot(Slot slot) {
		slot.slotNumber = this.slots.size();
		this.slots.add(slot);
		this.field_20123_d.add((Object)null);
	}

	public void updateCraftingResults() {
		for(int i = 0; i < this.slots.size(); ++i) {
			ItemStack itemstack = ((Slot)this.slots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack)this.field_20123_d.get(i);
			if(!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				itemstack1 = itemstack != null ? itemstack.copy() : null;
				this.field_20123_d.set(i, itemstack1);

				for(int j = 0; j < this.field_20121_g.size(); ++j) {
					((ICrafting)this.field_20121_g.get(j)).func_20159_a(this, i, itemstack1);
				}
			}
		}

	}

	public Slot getSlot(int i) {
		return (Slot)this.slots.get(i);
	}

	public ItemStack getStackInSlot(int i) {
		Slot slot = (Slot)this.slots.get(i);
		return slot != null ? slot.getStack() : null;
	}

	public ItemStack func_27280_a(int i, int j, boolean flag, EntityPlayer entityplayer) {
		if(VersionManager.version().protocol() < 11) {
			flag = false;
		}

		ItemStack itemstack = null;
		if(j == 0 || j == 1) {
			InventoryPlayer inventoryplayer = entityplayer.inventory;
			if(i == -999) {
				if(inventoryplayer.getItemStack() != null && i == -999) {
					if(j == 0) {
						entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
						inventoryplayer.setItemStack((ItemStack)null);
					}

					if(j == 1) {
						entityplayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));
						if(inventoryplayer.getItemStack().stackSize == 0) {
							inventoryplayer.setItemStack((ItemStack)null);
						}
					}
				}
			} else {
				int l1;
				if(flag) {
					ItemStack slot = this.getStackInSlot(i);
					if(slot != null) {
						int itemstack2 = slot.stackSize;
						itemstack = slot.copy();
						Slot itemstack3 = (Slot)this.slots.get(i);
						if(itemstack3 != null && itemstack3.getStack() != null) {
							l1 = itemstack3.getStack().stackSize;
							if(l1 < itemstack2) {
								this.func_27280_a(i, j, flag, entityplayer);
							}
						}
					}
				} else {
					Slot slot1 = (Slot)this.slots.get(i);
					if(slot1 != null) {
						slot1.onSlotChanged();
						ItemStack itemstack21 = slot1.getStack();
						ItemStack itemstack31 = inventoryplayer.getItemStack();
						if(itemstack21 != null) {
							itemstack = itemstack21.copy();
						}

						if(itemstack21 == null) {
							if(itemstack31 != null && slot1.isItemValid(itemstack31)) {
								l1 = j != 0 ? 1 : itemstack31.stackSize;
								if(l1 > slot1.getSlotStackLimit()) {
									l1 = slot1.getSlotStackLimit();
								}

								slot1.putStack(itemstack31.splitStack(l1));
								if(itemstack31.stackSize == 0) {
									inventoryplayer.setItemStack((ItemStack)null);
								}
							}
						} else if(itemstack31 == null) {
							l1 = j != 0 ? (itemstack21.stackSize + 1) / 2 : itemstack21.stackSize;
							ItemStack itemstack5 = slot1.decrStackSize(l1);
							inventoryplayer.setItemStack(itemstack5);
							if(itemstack21.stackSize == 0) {
								slot1.putStack((ItemStack)null);
							}

							slot1.onPickupFromSlot(inventoryplayer.getItemStack());
						} else if(slot1.isItemValid(itemstack31)) {
							if(itemstack21.itemID != itemstack31.itemID || itemstack21.getHasSubtypes() && itemstack21.getItemDamage() != itemstack31.getItemDamage()) {
								if(itemstack31.stackSize <= slot1.getSlotStackLimit()) {
									slot1.putStack(itemstack31);
									inventoryplayer.setItemStack(itemstack21);
								}
							} else {
								l1 = j != 0 ? 1 : itemstack31.stackSize;
								if(l1 > slot1.getSlotStackLimit() - itemstack21.stackSize) {
									l1 = slot1.getSlotStackLimit() - itemstack21.stackSize;
								}

								if(l1 > itemstack31.getMaxStackSize() - itemstack21.stackSize) {
									l1 = itemstack31.getMaxStackSize() - itemstack21.stackSize;
								}

								itemstack31.splitStack(l1);
								if(itemstack31.stackSize == 0) {
									inventoryplayer.setItemStack((ItemStack)null);
								}

								itemstack21.stackSize += l1;
							}
						} else if(itemstack21.itemID == itemstack31.itemID && itemstack31.getMaxStackSize() > 1 && (!itemstack21.getHasSubtypes() || itemstack21.getItemDamage() == itemstack31.getItemDamage())) {
							l1 = itemstack21.stackSize;
							if(l1 > 0 && l1 + itemstack31.stackSize <= itemstack31.getMaxStackSize()) {
								itemstack31.stackSize += l1;
								itemstack21.splitStack(l1);
								if(itemstack21.stackSize == 0) {
									slot1.putStack((ItemStack)null);
								}

								slot1.onPickupFromSlot(inventoryplayer.getItemStack());
							}
						}
					}
				}
			}
		}

		return itemstack;
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		InventoryPlayer inventoryplayer = entityplayer.inventory;
		if(inventoryplayer.getItemStack() != null) {
			entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
			inventoryplayer.setItemStack((ItemStack)null);
		}

	}

	public void onCraftMatrixChanged(IInventory iinventory) {
		this.updateCraftingResults();
	}

	public void putStackInSlot(int i, ItemStack itemstack) {
		this.getSlot(i).putStack(itemstack);
	}

	public void putStacksInSlots(ItemStack[] aitemstack) {
		for(int i = 0; i < aitemstack.length; ++i) {
			this.getSlot(i).putStack(aitemstack[i]);
		}

	}

	public void func_20112_a(int i, int j) {
	}

	public short func_20111_a(InventoryPlayer inventoryplayer) {
		++this.field_20917_a;
		return this.field_20917_a;
	}

	public void func_20113_a(short word0) {
	}

	public void func_20110_b(short word0) {
	}

	public abstract boolean isUsableByPlayer(EntityPlayer var1);

	protected void func_28125_a(ItemStack itemstack, int i, int j, boolean flag) {
		int k = i;
		if(flag) {
			k = j - 1;
		}

		if(itemstack.isStackable()) {
			while(itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i)) {
				Slot l = (Slot)this.slots.get(k);
				ItemStack slot1 = l.getStack();
				if(slot1 != null && slot1.itemID == itemstack.itemID && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == slot1.getItemDamage())) {
					int itemstack2 = slot1.stackSize + itemstack.stackSize;
					if(itemstack2 <= itemstack.getMaxStackSize()) {
						itemstack.stackSize = 0;
						slot1.stackSize = itemstack2;
						l.onSlotChanged();
					} else if(slot1.stackSize < itemstack.getMaxStackSize()) {
						itemstack.stackSize -= itemstack.getMaxStackSize() - slot1.stackSize;
						slot1.stackSize = itemstack.getMaxStackSize();
						l.onSlotChanged();
					}
				}

				if(flag) {
					--k;
				} else {
					++k;
				}
			}
		}

		if(itemstack.stackSize > 0) {
			int var9;
			if(flag) {
				var9 = j - 1;
			} else {
				var9 = i;
			}

			while(!flag && var9 < j || flag && var9 >= i) {
				Slot var10 = (Slot)this.slots.get(var9);
				ItemStack var11 = var10.getStack();
				if(var11 == null) {
					var10.putStack(itemstack.copy());
					var10.onSlotChanged();
					itemstack.stackSize = 0;
					break;
				}

				if(flag) {
					--var9;
				} else {
					++var9;
				}
			}
		}

	}
}
