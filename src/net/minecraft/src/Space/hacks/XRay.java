package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.hacks.Category;

public class XRay extends Hack {
    public XRay(int key){
        super("X-Ray", key);
	this.category = Category.render;
    }
    public void onToggle(){
        super.onToggle();
        Client.mc.renderGlobal.loadRenderers();
    }
};