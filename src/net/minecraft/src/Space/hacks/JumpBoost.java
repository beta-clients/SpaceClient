package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.hacks.Category;

public class JumpBoost extends Hack{
    public JumpBoost(int key){
        super("Jump Boost", key);
	this.category = Category.movement;
    }
};