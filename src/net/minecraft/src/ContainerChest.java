package net.minecraft.src;

public class ContainerChest extends Container {
	public IInventory field_20125_a;
	private int field_27282_b;

	public ContainerChest(IInventory iinventory, IInventory iinventory1) {
		this.field_20125_a = iinventory1;
		this.field_27282_b = iinventory1.getSizeInventory() / 9;
		int i = (this.field_27282_b - 4) * 18;

		int l;
		int j1;
		for(l = 0; l < this.field_27282_b; ++l) {
			for(j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(iinventory1, j1 + l * 9, 8 + j1 * 18, 18 + l * 18));
			}
		}

		for(l = 0; l < 3; ++l) {
			for(j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(iinventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
			}
		}

		for(l = 0; l < 9; ++l) {
			this.addSlot(new Slot(iinventory, l, 8 + l * 18, 161 + i));
		}

	}

	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return this.field_20125_a.canInteractWith(entityplayer);
	}

	public ItemStack getStackInSlot(int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.slots.get(i);
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(i < this.field_27282_b * 9) {
				this.func_28125_a(itemstack1, this.field_27282_b * 9, this.slots.size(), true);
			} else {
				this.func_28125_a(itemstack1, 0, this.field_27282_b * 9, false);
			}

			if(itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}
}
