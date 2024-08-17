package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet5PlayerInventory extends Packet {
	public int type;
	public ItemStack[] stacks;

	public Packet5PlayerInventory() {
	}

	public Packet5PlayerInventory(int i, ItemStack[] aitemstack) {
		this.type = i;
		this.stacks = new ItemStack[aitemstack.length];

		for(int j = 0; j < this.stacks.length; ++j) {
			this.stacks[j] = aitemstack[j] != null ? aitemstack[j].copy() : null;
		}

	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.type = datainputstream.readInt();
		short word0 = datainputstream.readShort();
		this.stacks = new ItemStack[word0];

		for(int i = 0; i < word0; ++i) {
			short word1 = datainputstream.readShort();
			if(word1 >= 0) {
				byte byte0 = datainputstream.readByte();
				short word2 = datainputstream.readShort();
				this.stacks[i] = new ItemStack(word1, byte0, word2);
			}
		}

	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(this.type);
		dataoutputstream.writeShort(this.stacks.length);

		for(int i = 0; i < this.stacks.length; ++i) {
			if(this.stacks[i] == null) {
				dataoutputstream.writeShort(-1);
			} else {
				dataoutputstream.writeShort((short)this.stacks[i].itemID);
				dataoutputstream.writeByte((byte)this.stacks[i].stackSize);
				dataoutputstream.writeShort((short)this.stacks[i].getItemDamage());
			}
		}

	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 6 + this.stacks.length * 5;
	}
}
