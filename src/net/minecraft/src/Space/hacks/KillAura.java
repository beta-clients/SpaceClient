package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.Client;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.Entity;
import net.minecraft.src.AxisAlignedBB;
import java.util.List;
import net.minecraft.src.Space.hacks.Category;
import net.minecraft.src.Space.FriendSystem;

public class KillAura extends Hack{
    public int mode = 0;
    public int radius = 10;
    public KillAura(int key){
        super("KillAura", key);
	this.category = Category.combat;
    }
    boolean canBeAttacked(Entity entity){
        if(mode == 0){
            return entity instanceof EntityMob;
        }
        else if(mode == 1){
            return entity instanceof EntityAnimal && !(entity instanceof EntityMob);
        }
        else if(mode == 2){
            return entity instanceof EntityPlayer;
        }
        else{
            return entity instanceof EntityMob || entity instanceof EntityAnimal || entity instanceof EntityPlayer;
        }
    }
    public void handleEvent(Event e){
        if(e instanceof EventPlayerUpdate){
            if(Client.mc.isMultiplayerWorld()){
                List<?> list = Client.mc.theWorld.getEntitiesWithinAABBExcludingEntity(Client.mc.thePlayer, AxisAlignedBB.getBoundingBox(Client.mc.thePlayer.posX - radius, Client.mc.thePlayer.posY - radius, Client.mc.thePlayer.posZ - radius, Client.mc.thePlayer.posX + radius, Client.mc.thePlayer.posY + radius, Client.mc.thePlayer.posZ + radius));
                for(int i = 0; i < list.size(); ++i){
                    Entity ee = (Entity)list.get(i);
		    if(ee instanceof EntityPlayer){
 			if(FriendSystem.friendList.contains(((EntityPlayer)ee).username)){
			    continue;
			}
		    }
                    if(this.canBeAttacked(ee)) {
                        ((EntityClientPlayerMP)Client.mc.thePlayer).sendQueue.addToSendQueue(new Packet7UseEntity(0, ee.entityId, 1));
                    }
                }
            }
        }
    }
}