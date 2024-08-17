package net.minecraft.src.Space.hacks;
import net.minecraft.src.Space.Hack;
import net.minecraft.src.Space.event.Event;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Vec3D;
import java.awt.Color;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemTool;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Vec3D;
import net.minecraft.src.Space.hacks.Category;

public class AutoTunnel extends Hack{
    public double distance = 5.0;
    public AutoTunnel(int key){
        super("AutoTunnel", key);
	this.category = Category.movement;
    }
    public void handleEvent(Event e) {
		int playerX;
		int playerY;
		int playerZ;
		int preX;
		CopyOnWriteArrayList breakables;
		CopyOnWriteArrayList placeables;
		Iterator it32;
		Vec3D v32;
		if(e instanceof EventPlayerUpdate && Client.mc.isMultiplayerWorld()) {
			EntityClientPlayerMP player1 = (EntityClientPlayerMP)Client.mc.thePlayer;
			int[] offsets1 = this.offsets();
			playerX = (int)Client.mc.thePlayer.posX;
			playerY = (int)Client.mc.thePlayer.posY;
			playerZ = (int)Client.mc.thePlayer.posZ;
			preX = playerX + offsets1[0];
			int preZ1 = playerZ + offsets1[1];
			breakables = this.findBreak(preX, playerY, preZ1);
			placeables = this.findPlace(preX, playerY, preZ1);
			CopyOnWriteArrayList lava1 = this.findLava(preX, playerY, preZ1);

			if(Client.mc.theWorld.getBlockId(preX, playerY - 2, preZ1) == 39 || Client.mc.theWorld.getBlockId(preX, playerY - 2, preZ1) == 40) {
				player1.sendQueue.addToSendQueue(new Packet14BlockDig(0, preX, playerY - 2, preZ1, 1));
				player1.sendQueue.addToSendQueue(new Packet14BlockDig(2, preX, playerY - 2, preZ1, 1));
			}

			if(!breakables.isEmpty()) {

				if(Client.mc.thePlayer.inventory.getCurrentItem() != null && Client.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemTool) {
					it32 = breakables.iterator();

					while(it32.hasNext()) {
						v32 = (Vec3D)it32.next();
						player1.sendQueue.addToSendQueue(new Packet14BlockDig(0, (int)v32.xCoord, (int)v32.yCoord, (int)v32.zCoord, 1));
						player1.sendQueue.addToSendQueue(new Packet14BlockDig(2, (int)v32.xCoord, (int)v32.yCoord, (int)v32.zCoord, 1));
					}
				}
			}

			int[] values22;
		}

	}

	public int[] offsets() {
		int[] offsets = new int[2];
		if(Client.mc.thePlayer.posX < 0.0D && Client.mc.thePlayer.posZ < 0.0D) {
			offsets[0] = -1;
			offsets[1] = -1;
		}

		if(Client.mc.thePlayer.posX > 0.0D && Client.mc.thePlayer.posZ > 0.0D) {
			offsets[0] = 0;
			offsets[1] = 0;
		}

		if(Client.mc.thePlayer.posX > 0.0D && Client.mc.thePlayer.posZ < 0.0D) {
			offsets[0] = 0;
			offsets[1] = -1;
		}

		if(Client.mc.thePlayer.posX < 0.0D && Client.mc.thePlayer.posZ > 0.0D) {
			offsets[0] = -1;
			offsets[1] = 0;
		}

		return offsets;
	}

	public int[] facingDir() {
		int[] facing = new int[2];
		int dir = MathHelper.floor_double((double)(Client.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch(dir) {
		case 0:
			facing[0] = 0;
			facing[1] = 1;
			break;
		case 1:
			facing[0] = -1;
			facing[1] = 0;
			break;
		case 2:
			facing[0] = 0;
			facing[1] = -1;
			break;
		case 3:
			facing[0] = 1;
			facing[1] = 0;
		}

		return facing;
	}

	public void silentplace(int X, int Y, int Z) {
		int[] facing = this.facingDir();
		int i2;
		int prevItem2;
		ItemStack stack2;
		int[] values2;
		if(Client.mc.theWorld.isAirBlock(X - facing[0], Y, Z - facing[1])) {
			for(i2 = 0; i2 < 9; ++i2) {
				prevItem2 = Client.mc.thePlayer.inventory.currentItem;
				stack2 = Client.mc.thePlayer.inventory.getStackInSlot(i2);
				if(stack2 != null && stack2.getItem() instanceof ItemBlock) {
					Client.mc.thePlayer.inventory.currentItem = i2;
					values2 = this.getDir(new Vec3D((double)(X - facing[0]), (double)Y, (double)(Z - facing[1])));
					if(Client.mc.thePlayer.posX - Math.floor(Client.mc.thePlayer.posX) > 0.3D && Client.mc.thePlayer.posX - Math.floor(Client.mc.thePlayer.posX) < 0.7D && Client.mc.thePlayer.posZ - Math.floor(Client.mc.thePlayer.posZ) > 0.3D && Client.mc.thePlayer.posZ - Math.floor(Client.mc.thePlayer.posZ) < 0.7D) {
						Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.inventory.getCurrentItem(), values2[0], values2[1], values2[2], values2[3]);
					}

					Client.mc.thePlayer.inventory.currentItem = prevItem2;
				}
			}
		}

		if(Client.mc.theWorld.isAirBlock(X - facing[0], Y - 1, Z - facing[1])) {
			for(i2 = 0; i2 < 9; ++i2) {
				prevItem2 = Client.mc.thePlayer.inventory.currentItem;
				stack2 = Client.mc.thePlayer.inventory.getStackInSlot(i2);
				if(stack2 != null && stack2.getItem() instanceof ItemBlock) {
					Client.mc.thePlayer.inventory.currentItem = i2;
					values2 = this.getDir(new Vec3D((double)(X - facing[0]), (double)(Y - 1), (double)(Z - facing[1])));
					if(Client.mc.thePlayer.posX - Math.floor(Client.mc.thePlayer.posX) > 0.3D && Client.mc.thePlayer.posX - Math.floor(Client.mc.thePlayer.posX) < 0.7D && Client.mc.thePlayer.posZ - Math.floor(Client.mc.thePlayer.posZ) > 0.3D && Client.mc.thePlayer.posZ - Math.floor(Client.mc.thePlayer.posZ) < 0.7D) {
						Client.mc.playerController.sendPlaceBlock(Client.mc.thePlayer, Client.mc.theWorld, Client.mc.thePlayer.inventory.getCurrentItem(), values2[0], values2[1], values2[2], values2[3]);
					}

					Client.mc.thePlayer.inventory.currentItem = prevItem2;
				}
			}
		}

	}

	public CopyOnWriteArrayList<Vec3D> findLava(int x, int y, int z) {
		int[] facingDir = this.facingDir();
		CopyOnWriteArrayList lava = new CopyOnWriteArrayList();

		for(int i = -1; i < 2; ++i) {
			for(int j = -2; j < 2; ++j) {
				for(int k = 1; k < 4; ++k) {
					Vec3D blockPos2;
					if(facingDir[0] == 0 && (Client.mc.theWorld.getBlockId(x + i, y + j, z + facingDir[1] * k) == 10 || Client.mc.theWorld.getBlockId(x + i, y + j, z + facingDir[1] * k) == 11)) {
						blockPos2 = new Vec3D((double)(x + i), (double)(y + j), (double)(z + facingDir[1] * k));
						lava.add(blockPos2);
					}

					if(facingDir[1] == 0 && (Client.mc.theWorld.getBlockId(x + facingDir[0] * k, y + j, z + i) == 10 || Client.mc.theWorld.getBlockId(x + facingDir[0] * k, y + j, z + i) == 11)) {
						blockPos2 = new Vec3D((double)(x + facingDir[0] * k), (double)(y + j), (double)(z + i));
						lava.add(blockPos2);
					}
				}
			}
		}

		return lava;
	}

	public CopyOnWriteArrayList<Vec3D> findPlace(int x, int y, int z) {
		int[] facingDir = this.facingDir();
		CopyOnWriteArrayList placeable = new CopyOnWriteArrayList();
		Vec3D i2;
		if(Client.mc.theWorld.getBlockMaterial(x, y - 2, z).getIsLiquid() || Client.mc.theWorld.isAirBlock(x, y - 2, z)) {
			i2 = new Vec3D((double)x, (double)(y - 2), (double)z);
			placeable.add(i2);
		}
		return placeable;
	}

	public CopyOnWriteArrayList<Vec3D> findBreak(int x, int y, int z) {
		int[] facingDir = this.facingDir();
		CopyOnWriteArrayList breakable = new CopyOnWriteArrayList();

		for(int i = -1; i < 1; ++i) {
			for(int j = 1; j < this.distance; ++j) {
				if(!Client.mc.theWorld.isAirBlock(x + facingDir[0] * j, y + i, z + facingDir[1] * j)) {
					CopyOnWriteArrayList blacklist = new CopyOnWriteArrayList(new Integer[]{Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11)});
					if(!blacklist.contains(Integer.valueOf(Client.mc.theWorld.getBlockId(x + facingDir[0] * j, y + i, z + facingDir[1] * j)))) {
						Vec3D blockPos = new Vec3D((double)(x + facingDir[0] * j), (double)(y + i), (double)(z + facingDir[1] * j));
						breakable.add(blockPos);
					}
				}
			}
		}

		return breakable;
	}

	public int[] getDir(Vec3D blockpos) {
		int[] values6;
		if(!Client.mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord - 1, (int)blockpos.zCoord)) {
			values6 = new int[]{(int)blockpos.xCoord + 0, (int)blockpos.yCoord - 1, (int)blockpos.zCoord + 0, 1};
			return values6;
		} else if(!Client.mc.theWorld.isAirBlock((int)blockpos.xCoord + 1, (int)blockpos.yCoord, (int)blockpos.zCoord)) {
			values6 = new int[]{(int)blockpos.xCoord + 1, (int)blockpos.yCoord + 0, (int)blockpos.zCoord + 0, 4};
			return values6;
		} else if(!Client.mc.theWorld.isAirBlock((int)blockpos.xCoord - 1, (int)blockpos.yCoord, (int)blockpos.zCoord)) {
			values6 = new int[]{(int)blockpos.xCoord - 1, (int)blockpos.yCoord + 0, (int)blockpos.zCoord + 0, 5};
			return values6;
		} else if(!Client.mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord, (int)blockpos.zCoord + 1)) {
			values6 = new int[]{(int)blockpos.xCoord + 0, (int)blockpos.yCoord + 0, (int)blockpos.zCoord + 1, 2};
			return values6;
		} else if(!Client.mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord, (int)blockpos.zCoord - 1)) {
			values6 = new int[]{(int)blockpos.xCoord + 0, (int)blockpos.yCoord + 0, (int)blockpos.zCoord - 1, 3};
			return values6;
		} else if(!Client.mc.theWorld.isAirBlock((int)blockpos.xCoord, (int)blockpos.yCoord - 1, (int)blockpos.zCoord)) {
			values6 = new int[]{(int)blockpos.xCoord + 0, (int)blockpos.yCoord - 1, (int)blockpos.zCoord};
			return values6;
		} else {
			return new int[4];
		}
	}

};