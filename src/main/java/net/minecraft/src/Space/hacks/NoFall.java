package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPacketSend;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Space.hacks.Category;

public class NoFall extends Hack{
    public NoFall(int key){
        super("NoFall", key);
	this.category = Category.other;
    }
    public void onGroundFunc(EventPacketSend e){
        if(e.getPacket() instanceof Packet10Flying){
            Packet10Flying packet = (Packet10Flying)e.getPacket();
            packet.onGround = true;
        }
        if(e.getPacket() instanceof Packet11PlayerPosition){
            Packet11PlayerPosition packet = (Packet11PlayerPosition)e.getPacket();
            packet.onGround = true;
        }
        if(e.getPacket() instanceof Packet12PlayerLook){
            Packet12PlayerLook packet = (Packet12PlayerLook)e.getPacket();
            packet.onGround = true;
        }
        if(e.getPacket() instanceof Packet13PlayerLookMove){
            Packet13PlayerLookMove packet = (Packet13PlayerLookMove)e.getPacket();
            packet.onGround = true;
        }
    }
    public void handleEvent(Event e){
        if(e instanceof EventPacketSend){
            onGroundFunc((EventPacketSend) e);
        }
    }
};