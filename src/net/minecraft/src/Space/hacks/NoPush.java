package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.hacks.Category;

public class NoPush extends Hack{
    public NoPush(int key){
        super("NoPush", key);
	this.category = Category.other;
    }
};