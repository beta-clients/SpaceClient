package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Space.event.events.EventHudUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.Client;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.Entity;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import java.util.List;
import net.minecraft.src.Space.hacks.Category;

public class Radar extends Hack{
    private int radius = 256;
    public Radar(int key){
        super("Radar", key);
	this.category = Category.hud;
    }
    public void handleEvent(Event e){
        if(e instanceof EventHudUpdate){
            if(Client.mc.isMultiplayerWorld()){
                List<?> list = Client.mc.theWorld.getEntitiesWithinAABBExcludingEntity(Client.mc.thePlayer, AxisAlignedBB.getBoundingBox(Client.mc.thePlayer.posX - radius, Client.mc.thePlayer.posY - radius, Client.mc.thePlayer.posZ - radius, Client.mc.thePlayer.posX + radius, Client.mc.thePlayer.posY + radius, Client.mc.thePlayer.posZ + radius));
		ScaledResolution sr = new ScaledResolution(Client.mc.gameSettings,Client.mc.displayWidth, Client.mc.displayHeight);
		int cnt = 0;
                for(int i = 0; i < list.size(); ++i){
                    Entity ee = (Entity)list.get(i);
                    if(ee instanceof EntityOtherPlayerMP){
			  EntityOtherPlayerMP eee = (EntityOtherPlayerMP)ee;
			  int x = (int)Math.floor(eee.posX);
            		  int y = (int)Math.floor(eee.posY);
            		  int z = (int)Math.floor(eee.posZ);
			  String text = eee.username + ": " + x + ", " + y + ", " + z;
			  Client.mc.fontRenderer.drawStringWithShadow(text, 2, 12 + 10 * cnt, 0xADD8E6);
			 ++cnt;
		    }
                }
            }
        }
    }
}