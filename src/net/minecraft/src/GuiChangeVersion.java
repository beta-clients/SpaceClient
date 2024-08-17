package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.multiversion.Version;
import org.lwjgl.opengl.GL11;

public class GuiChangeVersion extends GuiScreen {
	private List<Version> versionList;
	private GuiScreen parentScreen;
	private GuiButton pageButton;
	private ServerNBTStorage data;
	private boolean page;

	public GuiChangeVersion(GuiScreen parent, ServerNBTStorage servernbtstorage) {
		this.data = servernbtstorage;
		this.parentScreen = parent;
		this.page = true;
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		this.versionList = new ArrayList();
		this.controlList.clear();
		int i = 0;

		for(Iterator i1 = Version.alphaList.iterator(); i1.hasNext(); ++i) {
			Version version = (Version)i1.next();
			this.controlList.add(i, new GuiSmallButton(i, this.width / 2 - 155 + i % 2 * 160, this.height / 3 - 8 + 24 * (i >> 1), version.name));
			this.versionList.add(i, version);
		}

		int var6 = 0;

		for(Iterator var7 = Version.betaList.iterator(); var7.hasNext(); ++var6) {
			Version version1 = (Version)var7.next();
			this.controlList.add(i, new GuiSmallButton(i, this.width / 2 - 155 + var6 % 2 * 160, this.height / 3 - 8 + 24 * (var6 >> 1), version1.name));
			this.versionList.add(i, version1);
			++i;
		}

		this.controlList.add(this.pageButton = new GuiButton(99, this.width / 2 - 100, this.height / 4 + 112, "Alpha"));
		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 4 + 136, stringtranslate.translateKey("gui.cancel")));
	}

	public void drawScreen(int i, int j, float f) {
		for(int i1 = 0; i1 < this.versionList.size(); ++i1) {
			((GuiSmallButton)this.controlList.get(i1)).enabled = ((Version)this.versionList.get(i1)).alpha() == this.page;
			((GuiSmallButton)this.controlList.get(i1)).enabled2 = ((Version)this.versionList.get(i1)).alpha() == this.page;
		}

		this.pageButton.displayString = this.page ? "Alpha" : "Beta";
		this.drawDefaultBackground();
		GL11.glPushMatrix();
		GL11.glScalef(3.0F, 3.0F, 3.0F);
		this.drawCenteredString(this.fontRenderer, "Multiversion", this.width / 6, this.height / 6 - 28, 16777215);
		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 99) {
			this.page = !this.page;
		} else if(guibutton.id == 100) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.data.protocolVersion = ((Version)this.versionList.get(guibutton.id)).protocol.version();
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}
}
