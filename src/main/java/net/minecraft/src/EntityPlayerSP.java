package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Space.event.EventRegistry;
import net.minecraft.src.Space.event.events.EventPlayerUpdate;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	protected Minecraft mc;
	private MouseFilter field_21903_bJ = new MouseFilter();
	private MouseFilter field_21904_bK = new MouseFilter();
	private MouseFilter field_21902_bL = new MouseFilter();

	public EntityPlayerSP(Minecraft minecraft, World world, Session session, int i) {
		super(world);
		this.mc = minecraft;
		this.dimension = i;
		if(session != null && session.username != null && session.username.length() > 0) {
			this.skinUrl = "http://icebergcraft.com:6543/api/Minecraft/GetSkinByUsername/?username=" + session.username + ".png";
		}

		this.username = session.username;
	}

	public void moveEntity(double d, double d1, double d2) {
		super.moveEntity(d, d1, d2);
	}

	public void updatePlayerActionState() {
		super.updatePlayerActionState();
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
	}

	public void onLivingUpdate() {
		if(!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
			this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
		}

		this.prevTimeInPortal = this.timeInPortal;
		if(this.inPortal) {
			if(!this.worldObj.multiplayerWorld && this.ridingEntity != null) {
				this.mountEntity((Entity)null);
			}

			if(this.mc.currentScreen != null) {
				this.mc.displayGuiScreen((GuiScreen)null);
			}

			if(this.timeInPortal == 0.0F) {
				this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
			}

			this.timeInPortal += 0.0125F;
			if(this.timeInPortal >= 1.0F) {
				this.timeInPortal = 1.0F;
				if(!this.worldObj.multiplayerWorld) {
					this.timeUntilPortal = 10;
					this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
					this.mc.usePortal();
				}
			}

			this.inPortal = false;
		} else {
			if(this.timeInPortal > 0.0F) {
				this.timeInPortal -= 0.05F;
			}

			if(this.timeInPortal < 0.0F) {
				this.timeInPortal = 0.0F;
			}
		}

		if(this.timeUntilPortal > 0) {
			--this.timeUntilPortal;
		}

		this.movementInput.updatePlayerMoveState(this);
		if(this.movementInput.sneak && this.ySize < 0.2F) {
			this.ySize = 0.2F;
		}

		this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
		this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
		EventPlayerUpdate e = new EventPlayerUpdate();
		EventRegistry.handleEvent(e);
		super.onLivingUpdate();
	}

	public void resetPlayerKeyState() {
		this.movementInput.resetKeyState();
	}

	public void handleKeyPress(int i, boolean flag) {
		this.movementInput.checkKeyForMovementInput(i, flag);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.score = nbttagcompound.getInteger("Score");
	}

	public void closeScreen() {
		super.closeScreen();
		this.mc.displayGuiScreen((GuiScreen)null);
	}

	public void displayGUIEditSign(TileEntitySign tileentitysign) {
		this.mc.displayGuiScreen(new GuiEditSign(tileentitysign));
	}

	public void displayGUIChest(IInventory iinventory) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, iinventory));
	}

	public void displayWorkbenchGUI(int i, int j, int k) {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, i, j, k));
	}

	public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, tileentityfurnace));
	}

	public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {
		this.mc.displayGuiScreen(new GuiDispenser(this.inventory, tileentitydispenser));
	}

	public void onItemPickup(Entity entity, int i) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void sendChatMessage(String s) {
	}

	public boolean isSneaking() {
		return this.movementInput.sneak && !this.sleeping;
	}

	public void setHealth(int i) {
		int j = this.health - i;
		if(j <= 0) {
			this.health = i;
			if(j < 0) {
				this.heartsLife = this.heartsHalvesLife / 2;
			}
		} else {
			this.field_9346_af = j;
			this.prevHealth = this.health;
			this.heartsLife = this.heartsHalvesLife;
			this.damageEntity(j);
			this.hurtTime = this.maxHurtTime = 10;
		}

	}

	public void respawnPlayer() {
		this.mc.respawn(false, 0);
	}

	public void func_6420_o() {
	}

	public void addChatMessage(String s) {
		this.mc.ingameGUI.addChatMessageTranslate(s);
	}

	public void addStat(StatBase statbase, int i) {
		if(statbase != null) {
			if(statbase.func_25067_a()) {
				Achievement achievement = (Achievement)statbase;
				if(achievement.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement)) {
					if(!this.mc.statFileWriter.hasAchievementUnlocked(achievement)) {
						this.mc.guiAchievement.queueTakenAchievement(achievement);
					}

					this.mc.statFileWriter.readStat(statbase, i);
				}
			} else {
				this.mc.statFileWriter.readStat(statbase, i);
			}

		}
	}

	private boolean isBlockTranslucent(int i, int j, int k) {
		return this.worldObj.isBlockNormalCube(i, j, k);
	}

	protected boolean pushOutOfBlocks(double d, double d1, double d2) {
		int i = MathHelper.floor_double(d);
		int j = MathHelper.floor_double(d1);
		int k = MathHelper.floor_double(d2);
		double d3 = d - (double)i;
		double d4 = d2 - (double)k;
		if(this.isBlockTranslucent(i, j, k) || this.isBlockTranslucent(i, j + 1, k)) {
			boolean flag = !this.isBlockTranslucent(i - 1, j, k) && !this.isBlockTranslucent(i - 1, j + 1, k);
			boolean flag1 = !this.isBlockTranslucent(i + 1, j, k) && !this.isBlockTranslucent(i + 1, j + 1, k);
			boolean flag2 = !this.isBlockTranslucent(i, j, k - 1) && !this.isBlockTranslucent(i, j + 1, k - 1);
			boolean flag3 = !this.isBlockTranslucent(i, j, k + 1) && !this.isBlockTranslucent(i, j + 1, k + 1);
			byte byte0 = -1;
			double d5 = 9999.0D;
			if(flag && d3 < d5) {
				d5 = d3;
				byte0 = 0;
			}

			if(flag1 && 1.0D - d3 < d5) {
				d5 = 1.0D - d3;
				byte0 = 1;
			}

			if(flag2 && d4 < d5) {
				d5 = d4;
				byte0 = 4;
			}

			if(flag3 && 1.0D - d4 < d5) {
				double f = 1.0D - d4;
				byte0 = 5;
			}

			float f1 = 0.1F;
			if(byte0 == 0) {
				this.motionX = (double)(-f1);
			}

			if(byte0 == 1) {
				this.motionX = (double)f1;
			}

			if(byte0 == 4) {
				this.motionZ = (double)(-f1);
			}

			if(byte0 == 5) {
				this.motionZ = (double)f1;
			}
		}

		return false;
	}
}
