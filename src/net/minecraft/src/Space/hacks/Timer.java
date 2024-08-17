package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.hacks.Category;

public class Timer extends Hack{
	public float speed = 1.0F;
	public Timer(int key){
		super("Timer", key);
		this.category = Category.other;
	}
	public void onToggle(){
		if(!this.isToggled){
			Client.mc.timer.ticksPerSecond = 20.0F;
		}
	}
	public void handleEvent(Event e){
		if(e instanceof EventPlayerUpdate){
			Client.mc.timer.ticksPerSecond = 20.0F * speed;
		}
	}
};
