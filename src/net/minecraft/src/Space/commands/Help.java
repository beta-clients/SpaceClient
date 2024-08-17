package net.minecraft.src.Space.commands;
import net.minecraft.src.Space.Client;

public class Help extends Command{
	public Help(){
		super(".help", "displays this list");
	}
	public void handleCommand(String[] args){
		for(int i = 0; i < CommandHandler.commandList.size(); ++i){
			Client.addChatMessage("[Space] " + CommandHandler.commands[i].name + " - " + CommandHandler.commands[i].description);
		}
	}
};