package net.minecraft.protocol.alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet24MobSpawn extends Packet {
	public int entityId;
	public byte type;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;

	public Packet24MobSpawn() {
	}

	public Packet24MobSpawn(EntityLiving entityliving) {
		this.entityId = entityliving.entityId;
		this.type = (byte)EntityList.getEntityID(entityliving);
		this.xPosition = MathHelper.floor_double(entityliving.posX * 32.0D);
		this.yPosition = MathHelper.floor_double(entityliving.posY * 32.0D);
		this.zPosition = MathHelper.floor_double(entityliving.posZ * 32.0D);
		this.yaw = (byte)((int)(entityliving.rotationYaw * 256.0F / 360.0F));
		this.pitch = (byte)((int)(entityliving.rotationPitch * 256.0F / 360.0F));
	}

	public void readPacketData(DataInputStream datainputstream) throws IOException {
		this.entityId = datainputstream.readInt();
		this.type = datainputstream.readByte();
		this.xPosition = datainputstream.readInt();
		this.yPosition = datainputstream.readInt();
		this.zPosition = datainputstream.readInt();
		this.yaw = datainputstream.readByte();
		this.pitch = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(this.entityId);
		dataoutputstream.writeByte(this.type);
		dataoutputstream.writeInt(this.xPosition);
		dataoutputstream.writeInt(this.yPosition);
		dataoutputstream.writeInt(this.zPosition);
		dataoutputstream.writeByte(this.yaw);
		dataoutputstream.writeByte(this.pitch);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAlphaOverride(this);
	}

	public int getPacketSize() {
		return 19;
	}
}
