package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.Packet15Place;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.Space.hacks.Category;

public class Scaffold extends Hack{
    public int radius = 0;
    public Scaffold(int key){
        super("Scaffold", key);
	this.category = Category.movement;
    }
    public boolean canPlaceBlock(int x, int y, int z) {
		int id = Client.mc.theWorld.getBlockId(x, y, z);
		return id == 0 || id == 10 || id == 11 || id == 8 || id == 9;
	}

	public void placeBlock(int x, int y, int z) {
		if(!this.canPlaceBlock(x - 1, y, z)) {
			Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.getCurrentEquippedItem(), x - 1, y, z, 5);
		} else if(!this.canPlaceBlock(x + 1, y, z)) {
			Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.getCurrentEquippedItem(), x + 1, y, z, 4);
		} else if(!this.canPlaceBlock(x, y, z - 1)) {
			Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.getCurrentEquippedItem(), x, y, z - 1, 3);
		} else if(!this.canPlaceBlock(x, y, z + 1)) {
			Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.getCurrentEquippedItem(), x, y, z + 1, 2);
		} else if(!this.canPlaceBlock(x, y - 1, z)) {
			Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.getCurrentEquippedItem(), x, y - 1, z, 1);
		} else if(!this.canPlaceBlock(x, y + 1, z)) {
			Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.getCurrentEquippedItem(), x, y - 1, z, 0);
		}

	}
    public void handleEvent(Event e){
        if(e instanceof EventPlayerUpdate){
	    int plrX = (int)Math.floor(Client.mc.thePlayer.posX);
	    int plrY = (int)Math.floor(Client.mc.thePlayer.posY);
	    int plrZ = (int)Math.floor(Client.mc.thePlayer.posZ);

	   for(int x = plrX - radius; x <= plrX + radius; ++x) {
	   	for(int z = plrZ - radius; z <= plrZ + radius; ++z) {
			this.placeBlock(x, plrY - 2, z);
		}
 	   }
      }
    }
}