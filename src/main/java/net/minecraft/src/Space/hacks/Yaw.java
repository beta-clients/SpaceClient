package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.hacks.Category;

public class Yaw extends Hack{
	public float current_yaw = 0.0F;
	public Yaw(int key){
		super("Yaw", key);
		this.category = Category.other;
	}
	public void onToggle(){
		super.onToggle();
		this.current_yaw = Client.mc.thePlayer.rotationYaw;
	}
	public void handleEvent(Event e){
		if(e instanceof EventPlayerUpdate){
			Client.mc.thePlayer.rotationYaw = current_yaw;
		}
	}
};
