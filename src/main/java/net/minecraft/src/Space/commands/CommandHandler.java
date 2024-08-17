package net.minecraft.src.Space.commands;
import java.util.ArrayList;
public class CommandHandler {
	public static Command[] commands = new Command[16];
	public static ArrayList<Command> commandList = new ArrayList<Command>();
	public static void handleCommand(String chatMessage){
		if(!chatMessage.startsWith(".")){
			return;
		}
		String[] args = chatMessage.split(" ");
		for(int i = 0; i < commandList.size(); ++i){
			if(args[0].equals(commands[i].name)){
				commands[i].handleCommand(args);
				break;
			}
		}
	}
	public static void addCommand(int i, Command cmd){
		commands[i] = cmd;
		commandList.add(cmd);
	}
	public static void loadCommands(){
		addCommand(0, new Bind());
		addCommand(1, new SetTime());
		addCommand(2, new Help());
		addCommand(3, new TimerCmd());
		addCommand(4, new YawCmd());
		addCommand(5, new Friend());
	}
};