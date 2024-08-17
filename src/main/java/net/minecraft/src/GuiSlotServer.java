package net.minecraft.src;

import net.minecraft.multiversion.Version;

class GuiSlotServer extends GuiSlot {
	final GuiMultiplayer field_35410_a;

	public GuiSlotServer(GuiMultiplayer guimultiplayer) {
		super(guimultiplayer.mc, guimultiplayer.width, guimultiplayer.height, 32, guimultiplayer.height - 64, 36);
		this.field_35410_a = guimultiplayer;
	}

	protected int getSize() {
		return GuiMultiplayer.func_35320_a(this.field_35410_a).size();
	}

	protected void elementClicked(int i, boolean flag) {
		GuiMultiplayer.func_35326_a(this.field_35410_a, i);
		boolean flag1 = GuiMultiplayer.func_35333_b(this.field_35410_a) >= 0 && GuiMultiplayer.func_35333_b(this.field_35410_a) < this.getSize();
		GuiMultiplayer.func_35329_c(this.field_35410_a).enabled = flag1;
		GuiMultiplayer.func_35334_d(this.field_35410_a).enabled = flag1;
		GuiMultiplayer.func_35339_e(this.field_35410_a).enabled = flag1;
		if(flag && flag1) {
			GuiMultiplayer.func_35332_b(this.field_35410_a, i);
		}

	}

	protected boolean isSelected(int i) {
		return i == GuiMultiplayer.func_35333_b(this.field_35410_a);
	}

	protected int getContentHeight() {
		return GuiMultiplayer.func_35320_a(this.field_35410_a).size() * 36;
	}

	protected void drawBackground() {
		this.field_35410_a.drawDefaultBackground();
	}

	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		ServerNBTStorage servernbtstorage = (ServerNBTStorage)GuiMultiplayer.func_35320_a(this.field_35410_a).get(i);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, servernbtstorage.serverName, j + 2, k + 1, 16777215);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, servernbtstorage.serverAddress, j + 2, k + 12, 3158064);
		this.field_35410_a.drawString(this.field_35410_a.fontRenderer, ((Version)Version.versionMap.get(Integer.valueOf(servernbtstorage.protocolVersion))).name, j + 2, k + 23, 3158064);
	}
}
