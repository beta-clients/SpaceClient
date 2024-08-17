package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.events.EventPacketSend;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemFood;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Packet8UpdateHealth;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Space.hacks.Category;

public class AutoEat extends Hack{
	public AutoEat(int key){
		super("AutoEat", key);
		this.category = Category.other;
	}
	public void handleEvent(Event e){
		if(e instanceof EventPlayerUpdate){
			if(Client.mc.thePlayer.health < 13){
				for(int i = 0; i < 9; ++i) {
					int prevItem = Client.mc.thePlayer.inventory.currentItem;
					ItemStack stack = Client.mc.thePlayer.inventory.getStackInSlot(i);
					if(stack != null && stack.getItem() instanceof ItemFood) {
						Client.mc.thePlayer.inventory.currentItem = i;
						Client.mc.playerController.sendUseItem(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.inventory.getStackInSlot(i));
						Client.mc.thePlayer.inventory.currentItem = prevItem;
					}
				}
			}
		}
	}
};