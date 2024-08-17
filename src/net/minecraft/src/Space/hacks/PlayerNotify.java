package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;

public class PlayerNotify extends Hack{
    public PlayerNotify(int key){
        super("PlayerNotify", key);
	this.category = Category.hud;
    }
};