package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen {
	protected String screenTitle = "Edit sign message:";
	private TileEntitySign entitySign;
	private int updateCounter;
	private int editLine = 0;
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;

	public GuiEditSign(TileEntitySign tileentitysign) {
		this.entitySign = tileentitysign;
	}

	public void initGui() {
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if(this.mc.theWorld.multiplayerWorld && VersionManager.version().beta()) {
			this.mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(this.entitySign.xCoord, this.entitySign.yCoord, this.entitySign.zCoord, this.entitySign.signText));
		}

	}

	public void updateScreen() {
		++this.updateCounter;
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.enabled) {
			if(guibutton.id == 0) {
				this.entitySign.onInventoryChanged();
				this.mc.displayGuiScreen((GuiScreen)null);
			}

		}
	}

	protected void keyTyped(char c, int i) {
		if(i == 200) {
			this.editLine = this.editLine - 1 & 3;
		}

		if(i == 208 || i == 28) {
			this.editLine = this.editLine + 1 & 3;
		}

		if(i == 14 && this.entitySign.signText[this.editLine].length() > 0) {
			this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine].substring(0, this.entitySign.signText[this.editLine].length() - 1);
		}

		if(allowedCharacters.indexOf(c) >= 0 && this.entitySign.signText[this.editLine].length() < 15) {
			this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine] + c;
		}

	}

	public void drawScreen(int i, int j, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2), 0.0F, 50.0F);
		float f1 = 93.75F;
		GL11.glScalef(-f1, -f1, -f1);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		Block block = this.entitySign.getBlockType();
		if(block == Block.signPost) {
			float k = (float)(this.entitySign.getBlockMetadata() * 360) / 16.0F;
			GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else {
			int k1 = this.entitySign.getBlockMetadata();
			float f3 = 0.0F;
			if(k1 == 2) {
				f3 = 180.0F;
			}

			if(k1 == 4) {
				f3 = 90.0F;
			}

			if(k1 == 5) {
				f3 = -90.0F;
			}

			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		if(this.updateCounter / 6 % 2 == 0) {
			this.entitySign.lineBeingEdited = this.editLine;
		}

		TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
		this.entitySign.lineBeingEdited = -1;
		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
	}
}
