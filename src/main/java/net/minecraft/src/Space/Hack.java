package net.minecraft.src.Space;
import net.minecraft.src.Space.event.Event;
public class Hack {
	public int category = -1;
	public boolean isToggled = false;
	public int key;
	public String name;
	public Hack(String name, int key){
		this.key = key;
		this.name = name;
	}
	public void toggle(){
		this.isToggled = !this.isToggled;
	}
	public void setToggle(boolean tog){
		this.isToggled = tog;
	}
	public void setKey(int i){this.key = i;}
	public void onToggle(){
		if(this.isToggled){
			if(Client.hacks[29].isToggled){
				Client.addChatMessage("[Space] " + name + " enabled.");
			}
		}
		else{
			if(Client.hacks[29].isToggled){
				Client.addChatMessage("[Space] " + name + " disabled.");
			}
		}
	}
	public void onUpdate(){}
	public void handleEvent(Event e){}
};