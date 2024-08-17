package net.minecraft.src.Space;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Space.hacks.*;
import net.minecraft.src.EnumOptions;
import org.lwjgl.input.Keyboard;

public class SettingsPage extends GuiScreen {
	private int updateCounter2 = 0;
	long prevtime = -1;
	long prevtime2 = -1;
	private int updateCounter = 0;
	private void addFirstPageButton(int id, int x, int y){
		GuiButton gb = new GuiButton(id,x,y,"Back");
		gb.width = 50;
		this.controlList.add(gb);
	}
	public void initGui() {
		this.updateCounter2 = 0;
		this.controlList.clear();
		this.controlList.add(new GuiSlider(0, 2, 12, null, "KillAura radius: " + Integer.toString(((KillAura)Client.hacks[7]).radius), ((float)((KillAura)Client.hacks[7]).radius) / 10.0F, 1));
		this.controlList.add(new GuiSlider(1, 162, 12, null, "Timer speed: " + Float.toString(((Timer)Client.hacks[21]).speed), ((float)((Timer)Client.hacks[21]).speed) / 10.0F, 2));
		this.controlList.add(new GuiSlider(2, 2, 42, null, "AutoTunnel distance: " + Double.toString(((AutoTunnel)Client.hacks[23]).distance), ((float)((AutoTunnel)Client.hacks[23]).distance) / 5.0F, 3));
		this.controlList.add(new GuiSlider(3, 162, 42, null, "FastFly speed: " + Double.toString(((FastFly)Client.hacks[5]).speed), ((float)((FastFly)Client.hacks[5]).speed) / 5.0F, 4));
		this.controlList.add(new GuiSlider(4, 2, 72, null, "Scaffold radius: " + Integer.toString(((Scaffold)Client.hacks[20]).radius), ((float)((Scaffold)Client.hacks[20]).radius) / 2.0F, 5));
		this.controlList.add(new GuiSlider(5, 162, 72, null, "MaxMovPacketAmount: " + ((Packet19Sender)Client.hacks[33]).maxMovPacketAmount, ((float)((Packet19Sender)Client.hacks[33]).maxMovPacketAmount) / 200.0F, 6));
		this.addFirstPageButton(7, 2, 132);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == 7){
			Client.mc.displayGuiScreen(new ClickGui());
			return;
		}
	}
	
	public void updateScreen() {
		super.updateScreen();
		++this.updateCounter;
	}

	public void drawScreen(int var1, int var2, float var3) {
		//this.drawDefaultBackground();
		super.drawScreen(var1, var2, var3);
	}
}
