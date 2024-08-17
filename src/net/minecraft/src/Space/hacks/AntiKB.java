package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.hacks.Category;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPacketReceive;
import net.minecraft.src.Packet28EntityVelocity;

public class AntiKB extends Hack{
	public AntiKB(int key){
		super("AntiKB", key);
		this.category = Category.combat;
	}
	public void handleEvent(Event e){
		if(e instanceof EventPacketReceive){
			EventPacketReceive epr = (EventPacketReceive)e;
			if(epr.getPacket() instanceof Packet28EntityVelocity){
				e.isCancelled = true;
			}
		}
	}
};