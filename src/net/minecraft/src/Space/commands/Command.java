package net.minecraft.src.Space.commands;

public class Command {
	public String name;
	public String description;
	public Command(String name){
		this.name = name;
		this.description = "";
	}
	public Command(String name, String description){
		this.name = name;
		this.description = description;
	}
	public void handleCommand(String[] args){}
};