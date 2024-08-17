package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Client;
public class NoClip extends Hack{
    public NoClip(int key){
        super("NoClip", key);
        this.category = Category.movement;
    }
    public void onToggle() {
        if (this.isToggled) {
            Client.mc.thePlayer.noClip = true;
        } else {
            Client.mc.thePlayer.noClip = false;
        }
    }
}
