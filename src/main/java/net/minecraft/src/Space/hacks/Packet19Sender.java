package net.minecraft.src.Space.hacks;

import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPacketSend;

public class Packet19Sender extends Hack{
    public int packetAmount = 0;
    public int maxMovPacketAmount = 30;
    public double speed = 1.0;
    public Packet19Sender(int key){
        super("Packet19Sender", key);
	    this.category = Category.other;
    }
    public void handleEvent(Event e){
       if(e instanceof EventPacketSend && ((EventPacketSend)e).getPacket() instanceof Packet10Flying){
          Packet10Flying p = (Packet10Flying)((EventPacketSend)e).getPacket();
		   if(this.packetAmount >= this.maxMovPacketAmount){
			   Client.mc.getSendQueue().addToSendQueue(new Packet19EntityAction(Client.mc.thePlayer, 3));
			   this.packetAmount = 0;
		   }
		   ++this.packetAmount;
       }
    }
}