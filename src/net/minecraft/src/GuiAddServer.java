package net.minecraft.src;

import net.minecraft.multiversion.Version;
import org.lwjgl.input.Keyboard;

public class GuiAddServer extends GuiScreen {
	private GuiScreen field_35362_a;
	private GuiTextField addressBox;
	private GuiTextField nameBox;
	private ServerNBTStorage data;
	private String name;
	private String ip;

	public GuiAddServer(GuiScreen guiscreen, ServerNBTStorage servernbtstorage) {
		this.field_35362_a = guiscreen;
		this.data = servernbtstorage;
		this.name = this.data.serverName;
		this.ip = this.data.serverAddress;
	}

	public void updateScreen() {
		this.nameBox.updateCursorCounter();
		this.addressBox.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, 100, 20, "Done"));
		this.controlList.add(new GuiButton(1, this.width / 2, this.height / 4 + 120 + 12, 100, 20, stringtranslate.translateKey("gui.cancel")));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96 + 12, "Change version"));
		this.nameBox = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 76, 200, 20, this.name);
		this.nameBox.isFocused = true;
		this.nameBox.setMaxStringLength(32);
		this.addressBox = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 116, 200, 20, this.ip);
		this.addressBox.setMaxStringLength(128);
		((GuiButton)this.controlList.get(0)).enabled = this.addressBox.getText().length() > 0 && this.nameBox.getText().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.enabled) {
			if(guibutton.id == 2) {
				this.mc.gameSettings.saveOptions();
				this.name = this.nameBox.getText();
				this.ip = this.addressBox.getText();
				this.mc.displayGuiScreen(new GuiChangeVersion(this, this.data));
			} else if(guibutton.id == 1) {
				this.field_35362_a.deleteWorld(false, 0);
			} else if(guibutton.id == 0) {
				this.data.serverName = this.nameBox.getText();
				this.data.serverAddress = this.addressBox.getText();
				this.field_35362_a.deleteWorld(true, 0);
			}

		}
	}

	protected void keyTyped(char c, int i) {
		this.nameBox.textboxKeyTyped(c, i);
		this.addressBox.textboxKeyTyped(c, i);
		if(c == 9) {
			if(this.nameBox.isFocused) {
				this.nameBox.isFocused = false;
				this.addressBox.isFocused = true;
			} else {
				this.nameBox.isFocused = true;
				this.addressBox.isFocused = false;
			}
		}

		if(c == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		((GuiButton)this.controlList.get(0)).enabled = this.addressBox.getText().length() > 0 && this.nameBox.getText().length() > 0;
		if(((GuiButton)this.controlList.get(0)).enabled) {
			String s = this.addressBox.getText().trim();
			String[] as = s.split(":");
			if(as.length > 2) {
				((GuiButton)this.controlList.get(0)).enabled = false;
			}
		}

	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		this.addressBox.mouseClicked(i, j, k);
		this.nameBox.mouseClicked(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Edit Server Info", this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(this.fontRenderer, "Server Name", this.width / 2 - 100, 63, 10526880);
		this.drawString(this.fontRenderer, "Server Address", this.width / 2 - 100, 104, 10526880);
		this.drawCenteredString(this.fontRenderer, "Current version: " + ((Version)Version.versionMap.get(Integer.valueOf(this.data.protocolVersion))).name, this.width / 2, this.height / 4 + 90, 10526880);
		this.nameBox.drawTextBox();
		this.addressBox.drawTextBox();
		super.drawScreen(i, j, f);
	}
}
