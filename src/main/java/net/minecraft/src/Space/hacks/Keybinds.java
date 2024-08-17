package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventHudUpdate;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Space.hacks.Category;
import org.lwjgl.input.Keyboard;

public class Keybinds extends Hack{
    public Keybinds(int key){
        super("Keybinds", key);
	this.category = Category.hud;
    }
    public void handleEvent(Event e){
        if(e instanceof EventHudUpdate){
            ScaledResolution sr = new ScaledResolution(Client.mc.gameSettings,Client.mc.displayWidth, Client.mc.displayHeight);
	   int cur_pos = 1;
	   for(int i = Client.hackList.size() - 1; i >= 0; --i){
		if(Client.hacks[i].key != -1 && Client.hacks[i].key != 0){
			String s = "[" + Keyboard.getKeyName(Client.hacks[i].key) + "] " + Client.hacks[i].name + " is ";
			Client.mc.fontRenderer.drawStringWithShadow(s, 2, sr.getScaledHeight() - cur_pos * 10, 0xADD8E6);
			if(Client.hacks[i].isToggled){
				Client.mc.fontRenderer.drawStringWithShadow("On", 2 + Client.mc.fontRenderer.getStringWidth(s), sr.getScaledHeight() - cur_pos * 10, '\uff00');
			}
			else{
				Client.mc.fontRenderer.drawStringWithShadow("Off", 2 + Client.mc.fontRenderer.getStringWidth(s), sr.getScaledHeight() - cur_pos * 10, 16711680);
			}
			++cur_pos;
		}
	   }
	}
    }
};