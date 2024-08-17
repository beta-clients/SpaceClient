package net.minecraft.src.Space.event.events;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Packet;
import net.minecraft.src.NetHandler;

public class EventPacketReceive extends Event{
    public Packet packet;

    public EventPacketReceive(Packet packet){
        this.packet = packet;
    }
    public Packet getPacket(){
	return this.packet;
    }
    public void process(NetHandler nh){
        if(!this.isCancelled){
            this.packet.processPacket(nh);
        }
    }
}