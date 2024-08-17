package net.minecraft.src.Space.commands;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Yaw;

public class YawCmd extends Command{
	public YawCmd(){
		super(".yaw", "changes player's rotation yaw");
	}
	public void handleCommand(String[] args){
		if(!Client.hacks[24].isToggled){
			Client.addChatMessage("[Space] Yaw hack not enabled. Enable it to use .yaw.");
			return;
		}
		try{
			float yaw = Float.parseFloat(args[1]);
			((Yaw)Client.hacks[24]).current_yaw = yaw;
			Client.addChatMessage("[Space] Rotation yaw set to " + args[1] + ".");
		}
		catch(Exception e){
			Client.addChatMessage("[Space] Usage: .yaw <rotation yaw>");
		}
	}
};