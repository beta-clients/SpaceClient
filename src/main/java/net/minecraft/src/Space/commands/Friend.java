package net.minecraft.src.Space.commands;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.FriendSystem;

public class Friend extends Command{
	public Friend(){
	    super(".friend", "adds or removes friends from the friend list");
	}
	public void handleCommand(String[] args){
	    try{
		String cmd = args[1];
		if(cmd.equals("add")){
		    String friendName = args[2];
		    FriendSystem.friendList.add(friendName);
		    Client.addChatMessage("[Space] Friend " + friendName + " added to friend list.");
	        }
		else if(cmd.equals("remove")){
		    String friendName = args[2];
		    FriendSystem.friendList.remove(friendName);
		    Client.addChatMessage("[Space] Friend " + friendName + " removed from friend list.");
		}
		else if(cmd.equals("list")){
		    Client.addChatMessage("[Space] Friend list:");
		    String s = "";
		    for(int i = 0; i < FriendSystem.friendList.size(); ++i){
			if(i < FriendSystem.friendList.size() - 1){
			    s += FriendSystem.friendList.get(i) + ", ";
			}
			else{
			    s += FriendSystem.friendList.get(i);
			}
		    }
		    Client.addChatMessage("[Space] " + s);
		}
	    }
	    catch(Exception e){
		System.out.println("An error occured.");
	    }
	}
};