package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.Version;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen {
	private GuiScreen parentScreen;
	private GuiSlotServer field_35342_d;
	private List field_35340_f = new ArrayList();
	private int field_35341_g = -1;
	private GuiButton field_35347_h;
	private GuiButton field_35348_i;
	private GuiButton field_35345_j;
	private boolean field_35346_k = false;
	private boolean field_35353_s = false;
	private boolean field_35352_t = false;
	private boolean field_35351_u = false;
	private String field_35350_v = null;
	private ServerNBTStorage field_35349_w = null;

	public GuiMultiplayer(GuiScreen guiscreen) {
		this.parentScreen = guiscreen;
	}

	public void updateScreen() {
	}

	public void initGui() {
		this.loadServerData();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.field_35342_d = new GuiSlotServer(this);
		this.func_35337_c();
	}

	public static void func_35620_b(NBTTagCompound nbttagcompound, File file) throws IOException {
		DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

		try {
			CompressedStreamTools.func_1139_a(nbttagcompound, dataoutputstream);
		} finally {
			dataoutputstream.close();
		}

	}

	public static NBTTagCompound func_35622_a(File file) throws IOException {
		if(!file.exists()) {
			return null;
		} else {
			DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));

			NBTTagCompound var3;
			try {
				NBTTagCompound nbttagcompound = CompressedStreamTools.func_1141_a(datainputstream);
				var3 = nbttagcompound;
			} finally {
				datainputstream.close();
			}

			return var3;
		}
	}

	public static void func_35621_a(NBTTagCompound nbttagcompound, File file) throws IOException {
		File file1 = new File(file.getAbsolutePath() + "_tmp");
		if(file1.exists()) {
			file1.delete();
		}

		func_35620_b(nbttagcompound, file1);
		if(file.exists()) {
			file.delete();
		}

		if(file.exists()) {
			throw new IOException("Failed to delete " + file);
		} else {
			file1.renameTo(file);
		}
	}

	private void loadServerData() {
		try {
			Minecraft var10002 = this.mc;
			NBTTagCompound exception = func_35622_a(new File(Minecraft.getMinecraftDir(), "multiversion-servers.dat"));
			NBTTagList nbttaglist = exception.getTagList("servers");
			this.field_35340_f.clear();

			for(int i = 0; i < nbttaglist.tagCount(); ++i) {
				this.field_35340_f.add(ServerNBTStorage.func_35788_a((NBTTagCompound)nbttaglist.tagAt(i)));
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

	}

	private void func_35323_q() {
		try {
			NBTTagList exception = new NBTTagList();

			for(int nbttagcompound = 0; nbttagcompound < this.field_35340_f.size(); ++nbttagcompound) {
				exception.setTag(((ServerNBTStorage)this.field_35340_f.get(nbttagcompound)).func_35789_a());
			}

			NBTTagCompound var4 = new NBTTagCompound();
			var4.setTag("servers", exception);
			Minecraft var10003 = this.mc;
			func_35621_a(var4, new File(Minecraft.getMinecraftDir(), "multiversion-servers.dat"));
		} catch (Exception var3) {
			var3.printStackTrace();
		}

	}

	public void func_35337_c() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.controlList.add(this.field_35347_h = new GuiButton(7, this.width / 2 - 154, this.height - 28, 100, 20, "Edit"));
		this.controlList.add(this.field_35345_j = new GuiButton(2, this.width / 2 - 50, this.height - 28, 100, 20, "Delete"));
		this.controlList.add(this.field_35348_i = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, "Join Server"));
		this.controlList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, "Direct Connect"));
		this.controlList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, "Add server"));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4 + 50, this.height - 28, 100, 20, stringtranslate.translateKey("gui.cancel")));
		boolean flag = this.field_35341_g >= 0 && this.field_35341_g < this.field_35342_d.getSize();
		this.field_35348_i.enabled = flag;
		this.field_35347_h.enabled = flag;
		this.field_35345_j.enabled = flag;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.enabled) {
			if(guibutton.id == 2) {
				String servernbtstorage = ((ServerNBTStorage)this.field_35340_f.get(this.field_35341_g)).serverName;
				if(servernbtstorage != null) {
					this.field_35346_k = true;
					StringTranslate stringtranslate = StringTranslate.getInstance();
					String s1 = "Are you sure you want to remove this server?";
					String s2 = "\'" + servernbtstorage + "\' " + "will be lost forever! (A long time!)";
					String s3 = "Delete";
					String s4 = stringtranslate.translateKey("gui.cancel");
					GuiYesNo guiyesno = new GuiYesNo(this, s1, s2, s3, s4, this.field_35341_g);
					this.mc.displayGuiScreen(guiyesno);
				}
			} else if(guibutton.id == 1) {
				this.func_35322_a(this.field_35341_g);
			} else if(guibutton.id == 4) {
				this.field_35351_u = true;
				this.mc.displayGuiScreen(new GuiServerList(this, this.field_35349_w = new ServerNBTStorage("Minecraft Server", "", VersionManager.version().protocol())));
			} else if(guibutton.id == 3) {
				this.field_35353_s = true;
				this.mc.displayGuiScreen(new GuiAddServer(this, this.field_35349_w = new ServerNBTStorage("Minecraft Server", "", VersionManager.version().protocol())));
			} else if(guibutton.id == 7) {
				this.field_35352_t = true;
				ServerNBTStorage servernbtstorage1 = (ServerNBTStorage)this.field_35340_f.get(this.field_35341_g);
				this.mc.displayGuiScreen(new GuiAddServer(this, this.field_35349_w = new ServerNBTStorage(servernbtstorage1.serverName, servernbtstorage1.serverAddress, servernbtstorage1.protocolVersion)));
			} else if(guibutton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if(guibutton.id == 8) {
				this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
			} else {
				this.field_35342_d.actionPerformed(guibutton);
			}

		}
	}

	public void deleteWorld(boolean flag, int i) {
		if(this.field_35346_k) {
			this.field_35346_k = false;
			if(flag) {
				this.field_35340_f.remove(i);
				this.func_35323_q();
			}

			this.mc.displayGuiScreen(this);
		} else if(this.field_35351_u) {
			this.field_35351_u = false;
			if(flag) {
				this.func_35330_a(this.field_35349_w);
			} else {
				this.mc.displayGuiScreen(this);
			}
		} else if(this.field_35353_s) {
			this.field_35353_s = false;
			if(flag) {
				this.field_35340_f.add(this.field_35349_w);
				this.func_35323_q();
			}

			this.mc.displayGuiScreen(this);
		} else if(this.field_35352_t) {
			this.field_35352_t = false;
			if(flag) {
				ServerNBTStorage servernbtstorage = (ServerNBTStorage)this.field_35340_f.get(this.field_35341_g);
				servernbtstorage.serverName = this.field_35349_w.serverName;
				servernbtstorage.serverAddress = this.field_35349_w.serverAddress;
				servernbtstorage.protocolVersion = this.field_35349_w.protocolVersion;
				this.func_35323_q();
			}

			this.mc.displayGuiScreen(this);
		}

	}

	private int parseIntWithDefault(String s, int i) {
		try {
			return Integer.parseInt(s.trim());
		} catch (Exception var4) {
			return i;
		}
	}

	protected void keyTyped(char c, int i) {
		if(c == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(2));
		}

	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		this.field_35350_v = null;
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.field_35342_d.drawScreen(i, j, f);
		this.drawCenteredString(this.fontRenderer, "Play Multiplayer", this.width / 2, 20, 16777215);
		super.drawScreen(i, j, f);
		if(this.field_35350_v != null) {
			this.func_35325_a(this.field_35350_v, i, j);
		}

	}

	private void func_35322_a(int i) {
		this.func_35330_a((ServerNBTStorage)this.field_35340_f.get(i));
	}

	private void func_35330_a(ServerNBTStorage servernbtstorage) {
		VersionManager.instance.changeVersion((Version)Version.versionMap.get(Integer.valueOf(servernbtstorage.protocolVersion)));
		String s = servernbtstorage.serverAddress;
		String[] as = s.split(":");
		if(s.startsWith("[")) {
			int i = s.indexOf("]");
			if(i > 0) {
				String s1 = s.substring(1, i);
				String s2 = s.substring(i + 1).trim();
				if(s2.startsWith(":") && s2.length() > 0) {
					s2 = s2.substring(1);
					as = new String[]{s1, s2};
				} else {
					as = new String[]{s1};
				}
			}
		}

		if(as.length > 2) {
			as = new String[]{s};
		}

		this.mc.displayGuiScreen(new GuiConnecting(this.mc, as[0], as.length <= 1 ? 25565 : this.parseIntWithDefault(as[1], 25565)));
	}

	protected void func_35325_a(String s, int i, int j) {
		if(s != null) {
			int k = i + 12;
			int l = j - 12;
			int i1 = this.fontRenderer.getStringWidth(s);
			this.drawGradientRect(k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(s, k, l, -1);
		}
	}

	static List func_35320_a(GuiMultiplayer guimultiplayer) {
		return guimultiplayer.field_35340_f;
	}

	static int func_35326_a(GuiMultiplayer guimultiplayer, int i) {
		return guimultiplayer.field_35341_g = i;
	}

	static int func_35333_b(GuiMultiplayer guimultiplayer) {
		return guimultiplayer.field_35341_g;
	}

	static GuiButton func_35329_c(GuiMultiplayer guimultiplayer) {
		return guimultiplayer.field_35348_i;
	}

	static GuiButton func_35334_d(GuiMultiplayer guimultiplayer) {
		return guimultiplayer.field_35347_h;
	}

	static GuiButton func_35339_e(GuiMultiplayer guimultiplayer) {
		return guimultiplayer.field_35345_j;
	}

	static void func_35332_b(GuiMultiplayer guimultiplayer, int i) {
		guimultiplayer.func_35322_a(i);
	}
}
