package net.minecraft.src.Space;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Space.hacks.KillAura;
import net.minecraft.src.EnumOptions;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;
import net.minecraft.src.ScaledResolution;

public class ClickGui extends GuiScreen {
	private int updateCounter2 = 0;
	long prevtime = -1;
	long prevtime2 = -1;
	private int updateCounter = 0;
	private int x0 = -1;
	private int x1 = -1;
	private int x2 = -1;
	private int x3 = -1;
	private int x4 = -1;
	private void addClickGuiButton(int id, int x, int y){
		GuiButton gb = new GuiButton(id,x,y,"");
		gb.width = 75;
		gb.displayString = Client.hacks[id].name;
		this.controlList.add(gb);
	}
	private void addKillauraButton(int x, int y){
		GuiButton gb = new GuiButton(7,x,y,"");
		gb.width = 75;
		if(!Client.hacks[7].isToggled){
			gb.displayString = Client.hacks[7].name;
		}
		else{
			if(((KillAura)Client.hacks[7]).mode == 0){
				gb.displayString = Client.hacks[7].name + ": Mobs";
			}
			else if(((KillAura)Client.hacks[7]).mode == 1){
				gb.displayString = Client.hacks[7].name + ": Animals";
			}
			else if(((KillAura)Client.hacks[7]).mode == 2){
				gb.displayString = Client.hacks[7].name + ": Players";
			}
			else{
				gb.displayString = Client.hacks[7].name + ": All";
			}
		}
		this.controlList.add(gb);
	}
	private void addSettingsPageButton(int id, int x, int y){
		GuiButton gb = new GuiButton(id,x,y,"Settings");
		gb.width = 100;
		this.controlList.add(gb);
	}
	public void initGui() {
		this.updateCounter2 = 0;
		this.controlList.clear();
		int previ = -1;
		int category_pos0 = 0;
		int category_pos1 = 0;
		int category_pos2 = 0;
		int category_pos3 = 0;
		int category_pos4 = 0;
		this.x0 = 12;
		this.x1 = this.x0 + this.fontRenderer.getStringWidth("Movement") + 60;
		this.x2 = this.x1 + this.fontRenderer.getStringWidth("Render") + 60;
		this.x3 = this.x2 + this.fontRenderer.getStringWidth("HUD") + 60;
		this.x4 = this.x3 + this.fontRenderer.getStringWidth("Combat") + 60;
		for(int i = 0; i < Client.hackList.size(); ++i){
			if(Client.hacks[i].category == Category.movement){
				addClickGuiButton(i, this.x0, 22 + category_pos0 * 20);
				++category_pos0;
			}
			else if(Client.hacks[i].category == Category.render){
				addClickGuiButton(i, this.x1, 22 + category_pos1 * 20);
				++category_pos1;
			}
			else if(Client.hacks[i].category == Category.hud){
				addClickGuiButton(i, this.x2, 22 + category_pos2 * 20);
				++category_pos2;
			}
			else if(Client.hacks[i].category == Category.combat){
				if(i == 7){
					addKillauraButton(this.x3, 22 + category_pos3 * 20);
				}
				else{
					addClickGuiButton(i, this.x3, 22 + category_pos3 * 20);
				}
				++category_pos3;
			}
			else if(Client.hacks[i].category == Category.other){
				addClickGuiButton(i, this.x4, 22 + category_pos4 * 20);
				++category_pos4;
			}
		}
		ScaledResolution sr = new ScaledResolution(Client.mc.gameSettings,Client.mc.displayWidth, Client.mc.displayHeight);
		addSettingsPageButton(Client.hackList.size(), sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() - 20);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == Client.hackList.size()){
			Client.mc.displayGuiScreen(new SettingsPage());
			return;
		}
		if(System.currentTimeMillis() - prevtime < 10){
			return;
		}
		if(var1.id == 7){
			if(System.currentTimeMillis() - prevtime2 < 10){
				return;
			}
			if(!Client.hacks[7].isToggled){
				Client.hacks[7].toggle();
				((KillAura)Client.hacks[7]).mode = 0;
			}
			else if(((KillAura)Client.hacks[7]).mode != 3){
				++((KillAura)Client.hacks[7]).mode;
			}
			else if(((KillAura)Client.hacks[7]).mode == 3){
				Client.hacks[7].toggle();
			}
			if(!Client.hacks[7].isToggled){
				var1.displayString = Client.hacks[7].name + ": OFF";
			}
			else if(((KillAura)Client.hacks[7]).mode == 0){
				var1.displayString = Client.hacks[7].name + ": Mobs";
			}
			else if(((KillAura)Client.hacks[7]).mode == 1){
				var1.displayString = Client.hacks[7].name + ": Animals";
			}
			else if(((KillAura)Client.hacks[7]).mode == 2){
				var1.displayString = Client.hacks[7].name + ": Players";
			}
			else{
				var1.displayString = Client.hacks[7].name + ": All";
			}
			prevtime2 = System.currentTimeMillis();
			return;
		}
		Client.hacks[var1.id].toggle();
		Client.hacks[var1.id].onToggle();
		prevtime = System.currentTimeMillis();
	}
	
	public void updateScreen() {
		super.updateScreen();
		++this.updateCounter;
	}

	public void drawScreen(int var1, int var2, float var3) {
		//this.drawDefaultBackground();
		this.drawString(this.fontRenderer, "Movement", x0, 12, 14737632);
		this.drawString(this.fontRenderer, "Render", x1, 12, 14737632);
		this.drawString(this.fontRenderer, "HUD", x2, 12, 14737632);
		this.drawString(this.fontRenderer, "Combat", x3, 12, 14737632);
		this.drawString(this.fontRenderer, "Other", x4, 12, 14737632);
		super.drawScreen(var1, var2, var3);
	}
}
