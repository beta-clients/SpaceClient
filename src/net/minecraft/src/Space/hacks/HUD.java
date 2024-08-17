package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventHudUpdate;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Space.hacks.Category;

public class HUD extends Hack{
    public HUD(int key){
        super("HUD", key);
	this.category = Category.hud;
    }
    public void handleEvent(Event e){
        if(e instanceof EventHudUpdate){
            ScaledResolution sr = new ScaledResolution(Client.mc.gameSettings,Client.mc.displayWidth, Client.mc.displayHeight);
	   int cur_pos = 0;
	   for(int i = 0; i < Client.hackList.size(); ++i){
	   	if(Client.hacks[i].isToggled){
			Client.mc.fontRenderer.drawStringWithShadow(Client.hacks[i].name, sr.getScaledWidth() - Client.mc.fontRenderer.getStringWidth(Client.hacks[i].name), 2 + cur_pos * 10, 0xADD8E6);
			++cur_pos;
		}
	   }
        }
    }
};