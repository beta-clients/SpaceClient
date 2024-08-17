package net.minecraft.src.Space.commands;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Timer;

public class TimerCmd extends Command{
	public TimerCmd(){
		super(".timer", "changes amount of ticks per second");
	}
	public void handleCommand(String[] args){
		if(!Client.hacks[21].isToggled){
			Client.addChatMessage("[Space] Timer hack not enabled. Enable it to use .timer.");
			return;
		}
		try{
			float speed = Float.parseFloat(args[1]);
			((Timer)Client.hacks[21]).speed = speed;
			Client.addChatMessage("[Space] Timer speed set to " + args[1] + ".");
		}
		catch(Exception e){
			Client.addChatMessage("[Space] Usage: .timer <speed>");
		}
	}
};