package net.minecraft.src;

import net.minecraft.multiversion.Version;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.input.Keyboard;

public class GuiServerList extends GuiScreen {
	private GuiScreen parentScreen;
	private GuiTextField textBox;
	private ServerNBTStorage data;

	public GuiServerList(GuiScreen guiscreen, ServerNBTStorage servernbtstorage) {
		this.parentScreen = guiscreen;
		this.data = servernbtstorage;
	}

	public void updateScreen() {
		this.textBox.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Join Server"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72 + 12, "Change version"));
		this.textBox = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 116, 200, 20, this.data.serverAddress);
		this.textBox.setMaxStringLength(128);
		((GuiButton)this.controlList.get(0)).enabled = this.textBox.getText().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.enabled) {
			if(guibutton.id == 1) {
				this.parentScreen.deleteWorld(false, 0);
			} else if(guibutton.id == 0) {
				this.data.serverAddress = this.textBox.getText();
				VersionManager.instance.setVersion((Version)Version.versionMap.get(Integer.valueOf(this.data.protocolVersion)));
				this.parentScreen.deleteWorld(true, 0);
			} else if(guibutton.id == 2) {
				this.data.serverAddress = this.textBox.getText();
				this.mc.displayGuiScreen(new GuiChangeVersion(this, this.data));
			}

		}
	}

	protected void keyTyped(char c, int i) {
		this.textBox.textboxKeyTyped(c, i);
		if(c == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		((GuiButton)this.controlList.get(0)).enabled = this.textBox.getText().length() > 0;
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		this.textBox.mouseClicked(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Direct Connect", this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(this.fontRenderer, "Server Address ", this.width / 2 - 100, 100, 10526880);
		this.drawCenteredString(this.fontRenderer, "Current version: " + ((Version)Version.versionMap.get(Integer.valueOf(this.data.protocolVersion))).name, this.width / 2, this.height / 4 - 20, 10526880);
		this.textBox.drawTextBox();
		super.drawScreen(i, j, f);
	}
}
