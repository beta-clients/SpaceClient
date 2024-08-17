package net.minecraft.protocol.beta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet102WindowClick extends Packet {
	public int window_Id;
	public int inventorySlot;
	public int mouseClick;
	public short action;
	public ItemStack itemStack;

	public Packet102WindowClick() {
	}

	public Packet102WindowClick(int i, int j, int k, ItemStack itemstack, short word0) {
		this.window_Id = i;
		this.inventorySlot = j;
		this.mouseClick = k;
		this.itemStack = itemstack;
		this.action = word0;
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleBetaOverride(this);
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.window_Id = datainputstream.readByte();
		this.inventorySlot = datainputstream.readShort();
		this.mouseClick = datainputstream.readByte();
		this.action = datainputstream.readShort();
		short word0 = datainputstream.readShort();
		if(word0 >= 0) {
			byte byte0 = datainputstream.readByte();
			short word1 = datainputstream.readShort();
			this.itemStack = new ItemStack(word0, byte0, word1);
		} else {
			this.itemStack = null;
		}

	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeByte(this.window_Id);
		dataoutputstream.writeShort(this.inventorySlot);
		dataoutputstream.writeByte(this.mouseClick);
		dataoutputstream.writeShort(this.action);
		if(this.itemStack == null) {
			dataoutputstream.writeShort(-1);
		} else {
			dataoutputstream.writeShort(this.itemStack.itemID);
			dataoutputstream.writeByte(this.itemStack.stackSize);
			dataoutputstream.writeShort(this.itemStack.getItemDamage());
		}

	}

	public int getPacketSize() {
		return 11;
	}
}
