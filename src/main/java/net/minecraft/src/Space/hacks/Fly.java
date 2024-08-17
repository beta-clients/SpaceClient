package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.event.events.EventPacketSend;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet0KeepAlive;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.Space.hacks.Category;

public class Fly extends Hack{
    public double posX = Double.NaN;
    public double posY = Double.NaN;
    public double posZ = Double.NaN;
    public int packetAmount = 0;
   public double distanceBetweenPackets = 0.2125;
    public Fly(int key){
        super("Fly", key);
	this.category = Category.movement;
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
    }
    public void onToggle(){
	super.onToggle();
	this.packetAmount = 0;
    }
    public double distanceBetweenSentPackets(Packet10Flying movpk) {
		double xDiff = movpk.xPosition - this.posX;
		double zDiff = movpk.zPosition - this.posZ;
		double yDiff = movpk.yPosition - this.posY;
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    public void handleEvent(Event e){
        if(e instanceof EventPlayerUpdate){
            this.fly();
        }
	/*else if(e instanceof EventPacketSend && ((EventPacketSend)e).packet instanceof Packet10Flying){
		Packet10Flying p = (Packet10Flying)((EventPacketSend)e).packet;
		int i = this.packetAmount++;
		if(i > 10) {
			this.packetAmount = 0;
			if(Keyboard.isKeyDown(Client.mc.gameSettings.keyBindJump.keyCode)) {
            			Client.mc.getSendQueue().addToSendQueue(new Packet19EntityAction(Client.mc.thePlayer, 3));
        		}
		}
		if(this.posX == Double.NaN && this.posY == Double.NaN && this.posZ == Double.NaN) {
			this.posX = p.xPosition;
			this.posY = p.yPosition;
			this.posZ = p.zPosition;
		}
		else if(this.distanceBetweenSentPackets(p) < this.distanceBetweenPackets) {
			e.isCancelled = true;
			return;
		}
		this.posX = p.xPosition;
		this.posY = p.yPosition;
		this.posZ = p.zPosition;
	}*/
    }
}