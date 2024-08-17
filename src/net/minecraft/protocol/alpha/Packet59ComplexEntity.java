package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;

public class Packet59ComplexEntity extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte[] entityData;
	public NBTTagCompound entityNBT;

	public Packet59ComplexEntity() {
		this.isChunkDataPacket = true;
	}

	public Packet59ComplexEntity(int i, int j, int k, TileEntity tileentity) {
		this.isChunkDataPacket = true;
		this.xPosition = i;
		this.yPosition = j;
		this.zPosition = k;
		this.entityNBT = new NBTTagCompound();
		tileentity.writeToNBT(this.entityNBT);

		try {
			this.entityData = CompressedStreamTools.func_1142_a(this.entityNBT);
		} catch (IOException var6) {
			var6.printStackTrace();
		}

	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.xPosition = datainputstream.readInt();
		this.yPosition = datainputstream.readShort();
		this.zPosition = datainputstream.readInt();
		int i = datainputstream.readShort() & '\uffff';
		this.entityData = new byte[i];
		datainputstream.readFully(this.entityData);
		this.entityNBT = CompressedStreamTools.func_1140_a(this.entityData);
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(this.xPosition);
		dataoutputstream.writeShort(this.yPosition);
		dataoutputstream.writeInt(this.zPosition);
		dataoutputstream.writeShort((short)this.entityData.length);
		dataoutputstream.write(this.entityData);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return this.entityData.length + 2 + 10;
	}
}
