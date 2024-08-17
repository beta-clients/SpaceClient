package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPacketReceive;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Packet71Weather;

public class NoWeather extends Hack{
	public NoWeather(int key){
		super("NoWeather", key);
	}
	public void handleEvent(Event e){
		if(e instanceof EventPacketReceive){
			EventPacketReceive ee = (EventPacketReceive)e;
			if(ee.packet instanceof Packet71Weather){
				e.isCancelled = true;
			}
		}
		else if(e instanceof EventPlayerUpdate){
			Client.mc.theWorld.worldProvider.hasNoSky = true;
		}
	}
};