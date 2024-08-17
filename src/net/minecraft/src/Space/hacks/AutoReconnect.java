package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.events.EventKicked;
import net.minecraft.src.Space.event.events.EventPacketReceive;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.Space.hacks.Category;

public class AutoReconnect extends Hack{
	public AutoReconnect(int key){
		super("AutoReconnect", key);
		this.category = Category.other;
	}
	public void handleEvent(Event e){
		if(e instanceof EventKicked){
			try{
				String[] serverIp = Client.mc.gameSettings.lastServer.split("_");
				Thread.sleep(15000);
				Client.mc.displayGuiScreen(new GuiConnecting(Client.mc, serverIp[0], serverIp.length > 1 ? Integer.parseInt(serverIp[1]) : 25565));
			}
			catch(Exception ex){

			}
		}
	}
};