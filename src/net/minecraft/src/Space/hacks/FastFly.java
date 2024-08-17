package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.hacks.Category;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.Client;

public class FastFly extends Hack{
    public double speed = 1.5;
    public FastFly(int key){
        super("FastFly", key);
	this.category = Category.movement;
    }
    public void handleEvent(Event e){
	if(e instanceof EventPlayerUpdate){
		if(Client.hacks[4].isToggled){
			Client.mc.thePlayer.motionX *= this.speed;
			Client.mc.thePlayer.motionY *= this.speed;
			Client.mc.thePlayer.motionZ *= this.speed;
		}
	}
    }
};