package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;

public class Tracers extends Hack {
    public Tracers(int key){
        super("Tracers", key);
	this.category = Category.render;
    }
    public void onToggle(){
        super.onToggle();
        Client.mc.renderGlobal.loadRenderers();
    }
};