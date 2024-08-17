package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.Client;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.Entity;
import net.minecraft.src.AxisAlignedBB;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.src.Space.hacks.Category;

public class VisualRange extends Hack{
    private int radius = 256;
    private List<String> prevList = new ArrayList<String>();
    public VisualRange(int key){
        super("VisualRange", key);
	this.category = Category.other;
    }
    public void handleEvent(Event e){
        if(e instanceof EventPlayerUpdate){
            if(Client.mc.isMultiplayerWorld()){
                List<?> list = Client.mc.theWorld.getEntitiesWithinAABBExcludingEntity(Client.mc.thePlayer, AxisAlignedBB.getBoundingBox(Client.mc.thePlayer.posX - radius, Client.mc.thePlayer.posY - radius, Client.mc.thePlayer.posZ - radius, Client.mc.thePlayer.posX + radius, Client.mc.thePlayer.posY + radius, Client.mc.thePlayer.posZ + radius));
		List<String> usernameList = new ArrayList<String>();
		int cnt = 0;
                for(int i = 0; i < list.size(); ++i){
                    	Entity ee = (Entity)list.get(i);
			if(ee instanceof EntityOtherPlayerMP){
				EntityOtherPlayerMP eee = (EntityOtherPlayerMP)ee;
				usernameList.add(eee.username);
			}
                }
		for(int i = 0; i < usernameList.size(); ++i){
			String player = usernameList.get(i);
			if(prevList.indexOf(player) == -1){
				Client.addChatMessage("[Space] " + player + " has entered your visual range.");
			}
		}
		for(int i = 0; i < prevList.size(); ++i){
			String player = prevList.get(i);
			if(usernameList.indexOf(player) == -1){
				Client.addChatMessage("[Space] " + player + " has left your visual range.");
			}
		}
		prevList = usernameList;
            }
        }
    }
}