package net.minecraft.src.Space.event.events;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Packet;

public class EventPacketSend extends Event{
    public Packet packet;

    public EventPacketSend(Packet packet){
        this.packet = packet;
    }
    public Packet getPacket(){
        return this.packet;
    }
}