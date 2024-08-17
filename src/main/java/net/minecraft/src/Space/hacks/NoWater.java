package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;

public class NoWater extends Hack{
    public NoWater(int key){
        super("NoWater", key);
	this.category = Category.other;
    }
};