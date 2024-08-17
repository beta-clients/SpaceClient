package net.minecraft.src;

public class InventoryCrafting implements IInventory {
	public ItemStack[] stackList;
	private int field_21104_b;
	private Container eventHandler;

	public InventoryCrafting(Container container, int i, int j) {
		int k = i * j;
		this.stackList = new ItemStack[k];
		this.eventHandler = container;
		this.field_21104_b = i;
	}

	public int getSizeInventory() {
		return this.stackList.length;
	}

	public ItemStack getStackInSlot(int i) {
		return i >= this.getSizeInventory() ? null : this.stackList[i];
	}

	public ItemStack func_21103_b(int i, int j) {
		if(i >= 0 && i < this.field_21104_b) {
			int k = i + j * this.field_21104_b;
			return this.getStackInSlot(k);
		} else {
			return null;
		}
	}

	public String getInvName() {
		return "Crafting";
	}

	public ItemStack decrStackSize(int i, int j) {
		if(this.stackList[i] != null) {
			ItemStack itemstack1;
			if(this.stackList[i].stackSize <= j) {
				itemstack1 = this.stackList[i];
				this.stackList[i] = null;
				this.eventHandler.onCraftMatrixChanged(this);
				return itemstack1;
			} else {
				itemstack1 = this.stackList[i].splitStack(j);
				if(this.stackList[i].stackSize == 0) {
					this.stackList[i] = null;
				}

				this.eventHandler.onCraftMatrixChanged(this);
				return itemstack1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.stackList[i] = itemstack;
		this.eventHandler.onCraftMatrixChanged(this);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
