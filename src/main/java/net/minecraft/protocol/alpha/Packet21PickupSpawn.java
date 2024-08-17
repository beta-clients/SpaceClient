package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.EntityItem;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet21PickupSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public byte roll;
	public int itemId;
	public int count;

	public Packet21PickupSpawn() {
	}

	public Packet21PickupSpawn(EntityItem entityitem) {
		this.entityId = entityitem.entityId;
		this.itemId = entityitem.item.itemID;
		this.count = entityitem.item.stackSize;
		this.xPosition = MathHelper.floor_double(entityitem.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityitem.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityitem.posZ * 32.0D);
		this.rotation = (byte)((int)(entityitem.motionX * 128.0D));
		this.pitch = (byte)((int)(entityitem.motionY * 128.0D));
		this.roll = (byte)((int)(entityitem.motionZ * 128.0D));
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.entityId = datainputstream.readInt();
		this.itemId = datainputstream.readShort();
		this.count = datainputstream.readByte();
		this.xPosition = datainputstream.readInt();
		this.yPosition = datainputstream.readInt();
		this.zPosition = datainputstream.readInt();
		this.rotation = datainputstream.readByte();
		this.pitch = datainputstream.readByte();
		this.roll = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(this.entityId);
		dataoutputstream.writeShort(this.itemId);
		dataoutputstream.writeByte(this.count);
		dataoutputstream.writeInt(this.xPosition);
		dataoutputstream.writeInt(this.yPosition);
		dataoutputstream.writeInt(this.zPosition);
		dataoutputstream.writeByte(this.rotation);
		dataoutputstream.writeByte(this.pitch);
		dataoutputstream.writeByte(this.roll);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 22;
	}
}
