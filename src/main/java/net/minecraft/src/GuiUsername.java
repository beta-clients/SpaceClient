package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiUsername extends GuiScreen {
    private GuiScreen parentScreen;
    private GuiTextField textBox;

    public GuiUsername(GuiScreen guiscreen) {
        this.parentScreen = guiscreen;
    }

    @Override
    public void updateScreen() {
        this.textBox.updateCursorCounter();
    }

    @Override
    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Change Username"));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, stringTranslate.translateKey("gui.cancel")));
        this.textBox = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 116, 200, 20, mc.session.username);
        this.textBox.setMaxStringLength(128);
        ((GuiButton)this.controlList.get(0)).enabled = !this.textBox.getText().isEmpty();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 0) {
                mc.session.username = textBox.getText();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        this.textBox.textboxKeyTyped(var1, var2);
        if (var1 == 13) {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }

        ((GuiButton)this.controlList.get(0)).enabled = !this.textBox.getText().isEmpty();
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.textBox.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Username Changer", this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, "Username", this.width / 2 - 100, 100, 10526880);
        this.textBox.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
