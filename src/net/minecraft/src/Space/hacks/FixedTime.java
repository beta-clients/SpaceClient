package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPacketReceive;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.Space.hacks.Category;

public class FixedTime extends Hack{
	public long fixedWorldTime = 0;
	public FixedTime(int key){
		super("FixedTime", key);
		this.category = Category.other;
	}
	public void handleEvent(Event e){
		if(e instanceof EventPacketReceive){
			EventPacketReceive ee = (EventPacketReceive)e;
			if(ee.packet instanceof Packet4UpdateTime){
				e.isCancelled = true;
			}
		}
		else if(e instanceof EventPlayerUpdate){
			Client.mc.theWorld.worldInfo.worldTime = this.fixedWorldTime;
		}
	}
	public void onToggle(){
		super.onToggle();
		if(this.isToggled){
			this.fixedWorldTime = Client.mc.theWorld.worldInfo.worldTime;
		}
	}
};