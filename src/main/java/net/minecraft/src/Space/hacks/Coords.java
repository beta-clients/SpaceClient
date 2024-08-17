package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventHudUpdate;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Space.hacks.Category;

public class Coords extends Hack{
    public Coords(int key){
        super("Coords", key);
	this.category = Category.hud;
    }
    public void handleEvent(Event e){
        if(e instanceof EventHudUpdate){
            ScaledResolution sr = new ScaledResolution(Client.mc.gameSettings,Client.mc.displayWidth, Client.mc.displayHeight);
            int x = (int)Math.floor(Client.mc.thePlayer.posX);
            int y = (int)Math.floor(Client.mc.thePlayer.posY);
            int z = (int)Math.floor(Client.mc.thePlayer.posZ);
            Client.mc.fontRenderer.drawStringWithShadow("XYZ: " + x + ", " + y + ", " + z, sr.getScaledWidth() - Client.mc.fontRenderer.getStringWidth("XYZ: " + x + ", " + y + ", " + z), sr.getScaledHeight() - 10, 0xADD8E6);
        }
    }
};