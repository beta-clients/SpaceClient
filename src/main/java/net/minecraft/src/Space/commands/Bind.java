package net.minecraft.src.Space.commands;
import net.minecraft.src.Space.Client;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
	public Bind(){
		super(".bind", "binds hack to a specified key");
	}
	public void handleCommand(String[] args){
		try{
			String hackName = args[1];
			String key = args[2];
			for(int i = 0; i < Client.hackList.size(); ++i){
				if(hackName.toLowerCase().equals(Client.hacks[i].name.toLowerCase())){
					Client.hacks[i].setKey(Keyboard.getKeyIndex(key.toUpperCase()));
					Client.addChatMessage("[Space] " + Client.hacks[i].name + " binded to " + key.toUpperCase() + ".");
					return;
				}
			}
			Client.addChatMessage("[Space] " + hackName + ": hack not found.");
		}
		catch(Exception e){
			Client.addChatMessage("[Space] Usage: .bind <hack name> <key>");
		}
	}
};