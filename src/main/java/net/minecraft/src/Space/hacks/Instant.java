package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventMouseClick;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;

public class Instant extends Hack{
    public Instant(int key){
        super("Instant", key);
	this.category = Category.other;
    }
    public void handleEvent(Event e){
        if(e instanceof EventMouseClick){
            EventMouseClick emc = (EventMouseClick)e;
            while(Client.mc.theWorld.getBlockId(emc.blockX, emc.blockY, emc.blockZ) != 0){
                Client.mc.playerController.sendBlockRemoving(emc.blockX, emc.blockY, emc.blockZ, Client.mc.objectMouseOver.sideHit);
            }
        }
    }
};