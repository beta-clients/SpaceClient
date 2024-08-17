package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.KillAura;
import net.minecraft.src.Space.hacks.AutoTunnel;
import net.minecraft.src.Space.hacks.Timer;
import net.minecraft.src.Space.hacks.FastFly;
import net.minecraft.src.Space.hacks.Scaffold;
import net.minecraft.src.Space.hacks.Packet19Sender;
public class GuiSlider extends GuiButton {
	public float sliderValue = 1.0F;
	public boolean dragging = false;
	private EnumOptions idFloat = null;
	public int mode;

	public GuiSlider(int var1, int var2, int var3, EnumOptions var4, String var5, float var6, int mode) {
		super(var1, var2, var3, 150, 20, var5);
		this.idFloat = var4;
		this.sliderValue = var6;
		this.mode = mode;
	}
	public GuiSlider(int var1, int var2, int var3, EnumOptions var4, String var5, float var6) {
		super(var1, var2, var3, 150, 20, var5);
		this.idFloat = var4;
		this.sliderValue = var6;
		this.mode = 0;
	}

	protected int getHoverState(boolean var1) {
		return 0;
	}

	protected void mouseDragged(Minecraft var1, int var2, int var3) {
		if(this.enabled2) {
			if(this.dragging) {
				this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
				if(this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}

				if(this.sliderValue > 1.0F) {
					this.sliderValue = 1.0F;
				}

				var1.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
				if(mode == 0){
					this.displayString = var1.gameSettings.getKeyBinding(this.idFloat);
				}
				else if(mode == 1){
					((KillAura)Client.hacks[7]).radius  = Math.round(10 * this.sliderValue);
					this.displayString = "KillAura radius: " + ((KillAura)Client.hacks[7]).radius;
				}
				else if(mode == 2){
					((Timer)Client.hacks[21]).speed = Math.round((9.0 * this.sliderValue + 1.0) * 100.0) / (float)100.0;
					this.displayString = "Timer speed: " + ((Timer)Client.hacks[21]).speed;
				}
				else if(mode == 3){
					((AutoTunnel)Client.hacks[23]).distance = Math.round(5.0 * this.sliderValue * 100.0) / 100.0;
					this.displayString = "AutoTunnel distance: " + ((AutoTunnel)Client.hacks[23]).distance;
				}
				else if(mode == 4){
					((FastFly)Client.hacks[5]).speed = Math.round((4.0 * this.sliderValue + 1.0) * 100.0) / 100.0;
					this.displayString = "FastFly Speed: " + ((FastFly)Client.hacks[5]).speed;
				}
				else if(mode == 5){
					((Scaffold)Client.hacks[20]).radius = Math.round(2 * this.sliderValue);
					this.displayString = "Scaffold radius: " + ((Scaffold)Client.hacks[20]).radius;
				}
				else if(mode == 6){
					((Packet19Sender)Client.hacks[33]).maxMovPacketAmount = Math.round(this.sliderValue * 199) + 1;
					this.displayString = "MaxMovPacketAmount: " + ((Packet19Sender)Client.hacks[33]).maxMovPacketAmount;
				}
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
			this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
		}
	}

	public boolean mousePressed(Minecraft var1, int var2, int var3) {
		if(super.mousePressed(var1, var2, var3)) {
			this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
			if(this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}

			if(this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}

			var1.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
			if(this.mode == 0){
				this.displayString = var1.gameSettings.getKeyBinding(this.idFloat);
			}
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	public void mouseReleased(int var1, int var2) {
		this.dragging = false;
	}
}
