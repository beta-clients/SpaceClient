package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.Space.hacks.Category;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;

public class InventoryWalk extends Hack{
    public InventoryWalk(int key){
        super("InventoryWalk", key);
	this.category = Category.movement;
    }
    public void onToggle(){
	super.onToggle();
    }
    public void handleEvent(Event e){
        if(e instanceof EventPlayerUpdate){
	    if(Client.mc.currentScreen instanceof GuiInventory){
                if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindForward.keyCode)){
		    Client.mc.thePlayer.movementInput.moveForward += 1.0F;
	        }
		if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindBack.keyCode)){
		    Client.mc.thePlayer.movementInput.moveForward -= 1.0F;
		}
		if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindLeft.keyCode)){
		    Client.mc.thePlayer.movementInput.moveStrafe += 1.0F;
		}
		if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindRight.keyCode)){
		    Client.mc.thePlayer.movementInput.moveStrafe -= 1.0F;
		}
	    }
        }
    }
}