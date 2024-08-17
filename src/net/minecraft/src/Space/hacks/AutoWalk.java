package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;

public class AutoWalk extends Hack{
	public AutoWalk(int key){
        	super("AutoWalk", key);
		this.category = Category.movement;
	}
	public void handleEvent(Event e){
		if(e instanceof EventPlayerUpdate){
			Client.mc.thePlayer.movementInput.moveForward = 1.0F;
		}
	}
};