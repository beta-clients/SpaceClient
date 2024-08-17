package net.minecraft.src;

public class ServerNBTStorage {
	public String serverName;
	public String serverAddress;
	public int protocolVersion;

	public ServerNBTStorage(String s, String s1, int i) {
		this.serverName = s;
		this.serverAddress = s1;
		this.protocolVersion = i;
	}

	public NBTTagCompound func_35789_a() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("name", this.serverName);
		nbttagcompound.setString("ip", this.serverAddress);
		nbttagcompound.setInteger("version", this.protocolVersion);
		return nbttagcompound;
	}

	public static ServerNBTStorage func_35788_a(NBTTagCompound nbttagcompound) {
		return new ServerNBTStorage(nbttagcompound.getString("name"), nbttagcompound.getString("ip"), nbttagcompound.getInteger("version"));
	}
}
