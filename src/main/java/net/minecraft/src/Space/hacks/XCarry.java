package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPacketSend;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Space.hacks.Category;

public class XCarry extends Hack{
	public XCarry(int key){
		super("xCarry", key);
		this.category = Category.other;
	}
	public void handleEvent(Event e){
		if(e instanceof EventPacketSend){
			EventPacketSend eps = (EventPacketSend)e;
			if(eps.getPacket() instanceof Packet101CloseWindow){
				e.isCancelled = true;
			}
		}
	}
};