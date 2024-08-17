package net.minecraft.src.Space.commands;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.FixedTime;

public class SetTime extends Command {
	public SetTime(){
		super(".settime", "sets time to a specified number");
	}
	public void handleCommand(String[] args){
		try{
			if(Client.hacks[16].isToggled){
				long time = Long.parseLong(args[1]);
				((FixedTime)Client.hacks[16]).fixedWorldTime = time;
				Client.addChatMessage("[Space] Time set to " + time + ".");
			}
			else{
				Client.addChatMessage("[Space] FixedTime hack is not enabled. Enable it to use .settime.");
			}
		}
		catch(Exception e){
			Client.addChatMessage("[Space] Usage: .settime <time>");
		}
	}
};