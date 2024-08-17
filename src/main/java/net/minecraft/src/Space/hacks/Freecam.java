package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.events.EventPacketSend;
import net.minecraft.src.Space.event.Event;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Space.hacks.Category;

public class Freecam extends Hack{
    public Freecam(int key){
        super("Freecam", key);
	this.category = Category.render;
    }
    public void fly(){
        Client.mc.thePlayer.onGround = false;
        Client.mc.thePlayer.motionX = 0.0F;
        Client.mc.thePlayer.motionY = 0.0F;
        Client.mc.thePlayer.motionZ = 0.0F;
        double d = Client.mc.thePlayer.rotationYaw + 90.0D;
        boolean flag = Keyboard.isKeyDown(Client.mc.gameSettings.keyBindForward.keyCode);
        boolean flag1 = Keyboard.isKeyDown(Client.mc.gameSettings.keyBindBack.keyCode);
        boolean flag2 = Keyboard.isKeyDown(Client.mc.gameSettings.keyBindLeft.keyCode);
        boolean flag3 = Keyboard.isKeyDown(Client.mc.gameSettings.keyBindRight.keyCode);
        if(flag) {
            if(flag2) {
                d -= 45.0D;
            }
            else if(flag3) {
                d += 45.0D;
            }
        }
        else if(flag1) {
            d += 180.0D;
            if(flag2) {
                d += 45.0D;
            }
            else if(flag3) {
                d -= 45.0D;
            }
        }
        else if(flag2) {
            d -= 90.0D;
        }
        else if(flag3) {
            d += 90.0D;
        }
        if(flag || flag2 || flag1 || flag3) {
            Client.mc.thePlayer.motionX = Math.cos(Math.toRadians(d)) / 5.0;
            Client.mc.thePlayer.motionZ = Math.sin(Math.toRadians(d)) / 5.0;
        }
        if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindJump.keyCode)) {
            Client.mc.thePlayer.motionY += 0.2D;
        } else if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindSneak.keyCode)) {
            Client.mc.thePlayer.motionY -= 0.2D;
        }
        if(Client.hacks[5].isToggled){
            Client.mc.thePlayer.motionX *= 1.5;
            Client.mc.thePlayer.motionY *= 1.5;
            Client.mc.thePlayer.motionZ *= 1.5;
        }
    }
    public void onToggle(){
        super.onToggle();
        Client.mc.thePlayer.noClip = !Client.mc.thePlayer.noClip;
    }
    public void handleEvent(Event e){
        if(e instanceof EventPlayerUpdate && Client.mc.isMultiplayerWorld()){
            this.fly();
            ((EntityClientPlayerMP)Client.mc.thePlayer).sendQueue.addToSendQueue(new Packet0KeepAlive());
        }
        else if(e instanceof EventPacketSend){
            EventPacketSend ee = (EventPacketSend) e;
            if(ee.getPacket() instanceof Packet10Flying || ee.getPacket() instanceof Packet11PlayerPosition || ee.getPacket() instanceof Packet12PlayerLook || ee.getPacket() instanceof Packet13PlayerLookMove){
                e.isCancelled = true;
            }
        }
    }
}