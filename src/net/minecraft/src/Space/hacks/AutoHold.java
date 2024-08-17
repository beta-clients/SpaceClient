package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import java.awt.Robot;
import java.awt.event.InputEvent;
import net.minecraft.src.Space.hacks.Category;

public class AutoHold extends Hack{
	public AutoHold(int key){
		super("AutoHold", key);
		this.category = Category.other;
	}
	private int getCurrentDirection() {
		int f = (((int)Math.floor((Client.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D)) & 3);
		return f;
		// f = 0 - +Z
		// f = 1 - -X
		// f = 2 - -Z
		// f = 3 - +X
	}
	public void handleEvent(Event e) {
		if(e instanceof EventPlayerUpdate){
			try{
				Robot r = new Robot();
				int mask = InputEvent.BUTTON1_DOWN_MASK;
				if(Client.mc.currentScreen == null){
					r.mousePress(mask);
				}
			}
			catch(Exception ee){}
		}
	}
};