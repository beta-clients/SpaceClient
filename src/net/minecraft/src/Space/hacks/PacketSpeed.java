package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;
import net.minecraft.src.Space.event.events.EventPacketSend;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet19EntityAction;

public class PacketSpeed extends Hack{
    public int packetAmount = 0;
    public int multiplier = 4;
    public int maxPacketAmount = 30;
    public double speed = 5;
    public PacketSpeed(int key){
        super("PacketSpeed", key);
	this.category = Category.movement;
    }
    public void handleEvent(Event e){
       if(e instanceof EventPacketSend && ((EventPacketSend)e).getPacket() instanceof Packet10Flying){
          Packet10Flying p = (Packet10Flying)((EventPacketSend)e).getPacket();
	  if(p instanceof Packet11PlayerPosition){
	      Packet11PlayerPosition p11 = (Packet11PlayerPosition)p;
	      p11.yaw = Client.mc.thePlayer.rotationYaw;
	      for(int i = 0; i < multiplier - 1; ++i){
	         Client.mc.getSendQueue().netManager.addToSendQueueWithoutEvent(p11);
              }
          }
	  else if(p instanceof Packet13PlayerLookMove){
	      Packet13PlayerLookMove p13 = (Packet13PlayerLookMove)p;
	      p13.yaw = Client.mc.thePlayer.rotationYaw;
	      for(int i = 0; i < multiplier - 1; ++i){
	         Client.mc.getSendQueue().netManager.addToSendQueueWithoutEvent(p13);
              }
          }
		   if(this.packetAmount >= this.maxPacketAmount /* || distanceBetweenSentPackets(p) >= this.maxDist*/){
			   Client.mc.getSendQueue().addToSendQueue(new Packet19EntityAction(Client.mc.thePlayer, 3));
			   this.packetAmount = 0;
		   }
	  ++this.packetAmount;
       }
    }
}