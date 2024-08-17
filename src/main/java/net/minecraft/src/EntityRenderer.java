package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.NVFogDistance;
import org.lwjgl.util.glu.GLU;

public class EntityRenderer {
	public boolean hideHandWithGUI = true;
	public static boolean field_28135_a = false;
	public static int anaglyphField;
	private Minecraft mc;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();
	private MouseFilter mouseFilterDummy1 = new MouseFilter();
	private MouseFilter mouseFilterDummy2 = new MouseFilter();
	private MouseFilter mouseFilterDummy3 = new MouseFilter();
	private MouseFilter mouseFilterDummy4 = new MouseFilter();
	private float field_22228_r = 4.0F;
	private float field_22227_s = 4.0F;
	private float field_22226_t = 0.0F;
	private float field_22225_u = 0.0F;
	private float field_22224_v = 0.0F;
	private float field_22223_w = 0.0F;
	private float field_22222_x = 0.0F;
	private float field_22221_y = 0.0F;
	private float field_22220_z = 0.0F;
	private float field_22230_A = 0.0F;
	private boolean cloudFog = false;
	private double cameraZoom = 1.0D;
	private double cameraYaw = 0.0D;
	private double cameraPitch = 0.0D;
	private long prevFrameTime = System.currentTimeMillis();
	private long field_28133_I = 0L;
	private Random random = new Random();
	private int rainSoundCounter = 0;
	volatile int field_1394_b = 0;
	volatile int field_1393_c = 0;
	FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
	float fogColorRed;
	float fogColorGreen;
	float fogColorBlue;
	private float fogColor2;
	private float fogColor1;

	public EntityRenderer(Minecraft minecraft) {
		this.mc = minecraft;
		this.itemRenderer = new ItemRenderer(minecraft);
	}

	public void updateRenderer() {
		this.fogColor2 = this.fogColor1;
		this.field_22227_s = this.field_22228_r;
		this.field_22225_u = this.field_22226_t;
		this.field_22223_w = this.field_22224_v;
		this.field_22221_y = this.field_22222_x;
		this.field_22230_A = this.field_22220_z;
		if(this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		float f = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
		float f1 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
		float f2 = f * (1.0F - f1) + f1;
		this.fogColor1 += (f2 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
	}

	public void getMouseOver(float f) {
		if(this.mc.renderViewEntity != null) {
			if(this.mc.theWorld != null) {
				double d = (double)this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(d, f);
				double d1 = d;
				Vec3D vec3d = this.mc.renderViewEntity.getPosition(f);
				if(this.mc.objectMouseOver != null) {
					d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
				}

				if(this.mc.playerController instanceof PlayerControllerTest) {
					d = 32.0D;
				} else {
					if(d1 > 3.0D) {
						d1 = 3.0D;
					}

					d = d1;
				}

				Vec3D vec3d1 = this.mc.renderViewEntity.getLook(f);
				Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
				this.pointedEntity = null;
				float f1 = 1.0F;
				List list = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand((double)f1, (double)f1, (double)f1));
				double d2 = 0.0D;

				for(int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity)list.get(i);
					if(entity.canBeCollidedWith()) {
						float f2 = entity.getCollisionBorderSize();
						AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
						MovingObjectPosition movingobjectposition = axisalignedbb.func_1169_a(vec3d, vec3d2);
						if(axisalignedbb.isVecInside(vec3d)) {
							if(0.0D < d2 || d2 == 0.0D) {
								this.pointedEntity = entity;
								d2 = 0.0D;
							}
						} else if(movingobjectposition != null) {
							double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
							if(d3 < d2 || d2 == 0.0D) {
								this.pointedEntity = entity;
								d2 = d3;
							}
						}
					}
				}

				if(this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
					this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
				}

			}
		}
	}

	private float getFOVModifier(float f) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f1 = 70.0F;
		if(entityliving.isInsideOfMaterial(Material.water)) {
			f1 = 60.0F;
		}

		if(entityliving.health <= 0) {
			float f2 = (float)entityliving.deathTime + f;
			f1 /= (1.0F - 500.0F / (f2 + 500.0F)) * 2.0F + 1.0F;
		}

		return f1 + this.field_22221_y + (this.field_22222_x - this.field_22221_y) * f;
	}

	private void hurtCameraEffect(float f) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f1 = (float)entityliving.hurtTime - f;
		float f3;
		if(entityliving.health <= 0) {
			f3 = (float)entityliving.deathTime + f;
			GL11.glRotatef(40.0F - 8000.0F / (f3 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f1 >= 0.0F) {
			f1 /= (float)entityliving.maxHurtTime;
			f1 = MathHelper.sin(f1 * f1 * f1 * f1 * 3.141593F);
			f3 = entityliving.attackedAtYaw;
			GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f1 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float f) {
		if(this.mc.renderViewEntity instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer)this.mc.renderViewEntity;
			float f1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f2 = -(entityplayer.distanceWalkedModified + f1 * f);
			float f3 = entityplayer.field_775_e + (entityplayer.field_774_f - entityplayer.field_775_e) * f;
			float f4 = entityplayer.cameraPitch + (entityplayer.field_9328_R - entityplayer.cameraPitch) * f;
			GL11.glTranslatef(MathHelper.sin(f2 * 3.141593F) * f3 * 0.5F, -Math.abs(MathHelper.cos(f2 * 3.141593F) * f3), 0.0F);
			GL11.glRotatef(MathHelper.sin(f2 * 3.141593F) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(f2 * 3.141593F - 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
		}
	}

	private void orientCamera(float f) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f1 = entityliving.yOffset - 1.62F;
		double d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
		double d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f - (double)f1;
		double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
		GL11.glRotatef(this.field_22230_A + (this.field_22220_z - this.field_22230_A) * f, 0.0F, 0.0F, 1.0F);
		if(entityliving.isPlayerSleeping()) {
			f1 = (float)((double)f1 + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);
			if(!this.mc.gameSettings.field_22273_E) {
				int d3 = this.mc.theWorld.getBlockId(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
				if(d3 == Block.blockBed.blockID) {
					int j = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
					int f3 = j & 3;
					GL11.glRotatef((float)(f3 * 90), 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f + 180.0F, 0.0F, -1.0F, 0.0F);
				GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f, -1.0F, 0.0F, 0.0F);
			}
		} else if(this.mc.gameSettings.thirdPersonView) {
			double var27 = (double)(this.field_22227_s + (this.field_22228_r - this.field_22227_s) * f);
			float f5;
			float var28;
			if(this.mc.gameSettings.field_22273_E) {
				var28 = this.field_22225_u + (this.field_22226_t - this.field_22225_u) * f;
				f5 = this.field_22223_w + (this.field_22224_v - this.field_22223_w) * f;
				GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
				GL11.glRotatef(f5, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
			} else {
				var28 = entityliving.rotationYaw;
				f5 = entityliving.rotationPitch;
				double d4 = (double)(-MathHelper.sin(var28 / 180.0F * 3.141593F) * MathHelper.cos(f5 / 180.0F * 3.141593F)) * var27;
				double d5 = (double)(MathHelper.cos(var28 / 180.0F * 3.141593F) * MathHelper.cos(f5 / 180.0F * 3.141593F)) * var27;
				double d6 = (double)(-MathHelper.sin(f5 / 180.0F * 3.141593F)) * var27;

				for(int l = 0; l < 8; ++l) {
					float f6 = (float)((l & 1) * 2 - 1);
					float f7 = (float)((l >> 1 & 1) * 2 - 1);
					float f8 = (float)((l >> 2 & 1) * 2 - 1);
					f6 *= 0.1F;
					f7 *= 0.1F;
					f8 *= 0.1F;
					MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(d + (double)f6, d1 + (double)f7, d2 + (double)f8), Vec3D.createVector(d - d4 + (double)f6 + (double)f8, d1 - d6 + (double)f7, d2 - d5 + (double)f8));
					if(movingobjectposition != null) {
						double d7 = movingobjectposition.hitVec.distanceTo(Vec3D.createVector(d, d1, d2));
						if(d7 < var27) {
							var27 = d7;
						}
					}
				}

				GL11.glRotatef(entityliving.rotationPitch - f5, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entityliving.rotationYaw - var28, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
				GL11.glRotatef(var28 - entityliving.rotationYaw, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(f5 - entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if(!this.mc.gameSettings.field_22273_E) {
			GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f + 180.0F, 0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(0.0F, f1, 0.0F);
		d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
		d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f - (double)f1;
		d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
		this.cloudFog = this.mc.renderGlobal.func_27307_a(d, d1, d2, f);
	}

	public void setupCameraTransform(float f, int i) {
		this.farPlaneDistance = (float)(256 >> this.mc.gameSettings.renderDistance);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float f1 = 0.07F;
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(-(i * 2 - 1)) * f1, 0.0F, 0.0F);
		}

		if(this.cameraZoom != 1.0D) {
			GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
			GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
			GLU.gluPerspective(this.getFOVModifier(f), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
		} else {
			GLU.gluPerspective(this.getFOVModifier(f), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(f);
		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(f);
		}

		float f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * f;
		if(f2 > 0.0F) {
			float f3 = 5.0F / (f2 * f2 + 5.0F) - f2 * 0.04F;
			f3 *= f3;
			GL11.glRotatef(((float)this.rendererUpdateCount + f) * 20.0F, 0.0F, 1.0F, 1.0F);
			GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
			GL11.glRotatef(-((float)this.rendererUpdateCount + f) * 20.0F, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(f);
	}

	private void func_4135_b(float f, int i) {
		GL11.glLoadIdentity();
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		GL11.glPushMatrix();
		this.hurtCameraEffect(f);
		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(f);
		}

		if(!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping() && (!this.mc.gameSettings.hideGUI || this.mc.gameSettings.hideGUI && !this.hideHandWithGUI)) {
			this.itemRenderer.renderItemInFirstPerson(f);
		}

		GL11.glPopMatrix();
		if(!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping()) {
			this.itemRenderer.renderOverlays(f);
			this.hurtCameraEffect(f);
		}

		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(f);
		}

	}

	public void updateCameraAndRender(float f) {
		if(!Display.isActive()) {
			if(System.currentTimeMillis() - this.prevFrameTime > 500L) {
				this.mc.displayInGameMenu();
			}
		} else {
			this.prevFrameTime = System.currentTimeMillis();
		}

		if(this.mc.inGameHasFocus) {
			this.mc.mouseHelper.mouseXYChange();
			float scaledresolution = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float i = scaledresolution * scaledresolution * scaledresolution * 8.0F;
			float j = (float)this.mc.mouseHelper.deltaX * i;
			float k = (float)this.mc.mouseHelper.deltaY * i;
			byte i1 = 1;
			if(this.mc.gameSettings.invertMouse) {
				i1 = -1;
			}

			if(this.mc.gameSettings.smoothCamera) {
				j = this.mouseFilterXAxis.func_22386_a(j, 0.05F * i);
				k = this.mouseFilterYAxis.func_22386_a(k, 0.05F * i);
			}

			this.mc.thePlayer.func_346_d(j, k * (float)i1);
		}

		if(!this.mc.skipRenderWorld) {
			field_28135_a = this.mc.gameSettings.anaglyph;
			ScaledResolution scaledresolution1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int i1 = scaledresolution1.getScaledWidth();
			int j1 = scaledresolution1.getScaledHeight();
			int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
			int i11 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
			short c = 200;
			if(this.mc.gameSettings.limitFramerate == 1) {
				c = 120;
			}

			if(this.mc.gameSettings.limitFramerate == 2) {
				c = 40;
			}

			long l2;
			if(this.mc.theWorld != null) {
				if(this.mc.gameSettings.limitFramerate == 0) {
					this.renderWorld(f, 0L);
				} else {
					this.renderWorld(f, this.field_28133_I + (long)(1000000000 / c));
				}

				if(this.mc.gameSettings.limitFramerate == 2) {
					l2 = (this.field_28133_I + (long)(1000000000 / c) - System.nanoTime()) / 1000000L;
					if(l2 > 0L && l2 < 500L) {
						try {
							Thread.sleep(l2);
						} catch (InterruptedException var12) {
							var12.printStackTrace();
						}
					}
				}

				this.field_28133_I = System.nanoTime();
				if(!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
					this.mc.ingameGUI.renderGameOverlay(f, this.mc.currentScreen != null, k1, i11);
				}
			} else {
				GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				this.func_905_b();
				if(this.mc.gameSettings.limitFramerate == 2) {
					l2 = (this.field_28133_I + (long)(1000000000 / c) - System.nanoTime()) / 1000000L;
					if(l2 < 0L) {
						l2 += 10L;
					}

					if(l2 > 0L && l2 < 500L) {
						try {
							Thread.sleep(l2);
						} catch (InterruptedException var11) {
							var11.printStackTrace();
						}
					}
				}

				this.field_28133_I = System.nanoTime();
			}

			if(this.mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.mc.currentScreen.drawScreen(k1, i11, f);
				if(this.mc.currentScreen != null && this.mc.currentScreen.field_25091_h != null) {
					this.mc.currentScreen.field_25091_h.func_25087_a(f);
				}
			}

		}
	}

	public void renderWorld(float f, long l) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		this.getMouseOver(f);
		EntityLiving entityliving = this.mc.renderViewEntity;
		RenderGlobal renderglobal = this.mc.renderGlobal;
		EffectRenderer effectrenderer = this.mc.effectRenderer;
		double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
		double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
		double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
		IChunkProvider ichunkprovider = this.mc.theWorld.getIChunkProvider();
		if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate i = (ChunkProviderLoadOrGenerate)ichunkprovider;
			int frustrum = MathHelper.floor_float((float)((int)d)) >> 4;
			int l1 = MathHelper.floor_float((float)((int)d2)) >> 4;
			i.setCurrentChunkOver(frustrum, l1);
		}

		for(int var19 = 0; var19 < 2; ++var19) {
			if(this.mc.gameSettings.anaglyph) {
				anaglyphField = var19;
				if(anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(f);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.setupCameraTransform(f, var19);
			ClippingHelperImpl.getInstance();
			if(this.mc.gameSettings.renderDistance < 2) {
				this.setupFog(-1, f);
				renderglobal.renderSky(f);
			}

			GL11.glEnable(GL11.GL_FOG);
			this.setupFog(1, f);
			if(this.mc.gameSettings.ambientOcclusion) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			Frustrum var20 = new Frustrum();
			var20.setPosition(d, d1, d2);
			this.mc.renderGlobal.clipRenderersByFrustrum(var20, f);
			if(var19 == 0) {
				while(!this.mc.renderGlobal.updateRenderers(entityliving, false) && l != 0L) {
					long var21 = l - System.nanoTime();
					if(var21 < 0L || var21 > 1000000000L) {
						break;
					}
				}
			}

			this.setupFog(0, f);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png"));
			RenderHelper.disableStandardItemLighting();
			renderglobal.sortAndRender(entityliving, 0, (double)f);
			GL11.glShadeModel(GL11.GL_FLAT);
			RenderHelper.enableStandardItemLighting();
			renderglobal.renderEntities(entityliving.getPosition(f), var20, f);
			effectrenderer.func_1187_b(entityliving, f);
			RenderHelper.disableStandardItemLighting();
			this.setupFog(0, f);
			effectrenderer.renderParticles(entityliving, f);
			EntityPlayer entityplayer1;
			if(this.mc.objectMouseOver != null && entityliving.isInsideOfMaterial(Material.water) && entityliving instanceof EntityPlayer) {
				entityplayer1 = (EntityPlayer)entityliving;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderglobal.drawBlockBreaking(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), f);
				renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), f);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog(0, f);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png"));
			if(this.mc.gameSettings.fancyGraphics) {
				if(this.mc.gameSettings.ambientOcclusion) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glColorMask(false, false, false, false);
				int var22 = renderglobal.sortAndRender(entityliving, 1, (double)f);
				if(this.mc.gameSettings.anaglyph) {
					if(anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				if(var22 > 0) {
					renderglobal.renderAllRenderLists(1, (double)f);
				}

				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				renderglobal.sortAndRender(entityliving, 1, (double)f);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if(this.cameraZoom == 1.0D && entityliving instanceof EntityPlayer && this.mc.objectMouseOver != null && !entityliving.isInsideOfMaterial(Material.water)) {
				entityplayer1 = (EntityPlayer)entityliving;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderglobal.drawBlockBreaking(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), f);
				renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), f);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			this.renderRainSnow(f);
			GL11.glDisable(GL11.GL_FOG);
			if(this.pointedEntity == null) {
			}

			this.setupFog(0, f);
			GL11.glEnable(GL11.GL_FOG);
			renderglobal.renderClouds(f);
			GL11.glDisable(GL11.GL_FOG);
			this.setupFog(1, f);
			if(this.cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.func_4135_b(f, var19);
			}

			if(!this.mc.gameSettings.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	private void addRainParticles() {
		float f = this.mc.theWorld.func_27162_g(1.0F);
		if(!this.mc.gameSettings.fancyGraphics) {
			f /= 2.0F;
		}

		if(f != 0.0F) {
			this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
			EntityLiving entityliving = this.mc.renderViewEntity;
			World world = this.mc.theWorld;
			int i = MathHelper.floor_double(entityliving.posX);
			int j = MathHelper.floor_double(entityliving.posY);
			int k = MathHelper.floor_double(entityliving.posZ);
			byte byte0 = 10;
			double d = 0.0D;
			double d1 = 0.0D;
			double d2 = 0.0D;
			int l = 0;

			for(int i1 = 0; i1 < (int)(100.0F * f * f); ++i1) {
				int j1 = i + this.random.nextInt(byte0) - this.random.nextInt(byte0);
				int k1 = k + this.random.nextInt(byte0) - this.random.nextInt(byte0);
				int l1 = world.findTopSolidBlock(j1, k1);
				int i2 = world.getBlockId(j1, l1 - 1, k1);
				if(l1 <= j + byte0 && l1 >= j - byte0 && world.getWorldChunkManager().getBiomeGenAt(j1, k1).canSpawnLightningBolt()) {
					float f1 = this.random.nextFloat();
					float f2 = this.random.nextFloat();
					if(i2 > 0) {
						if(Block.blocksList[i2].blockMaterial == Material.lava) {
							this.mc.effectRenderer.addEffect(new EntitySmokeFX(world, (double)((float)j1 + f1), (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (double)((float)k1 + f2), 0.0D, 0.0D, 0.0D));
						} else {
							++l;
							if(this.random.nextInt(l) == 0) {
								d = (double)((float)j1 + f1);
								d1 = (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY;
								d2 = (double)((float)k1 + f2);
							}

							this.mc.effectRenderer.addEffect(new EntityRainFX(world, (double)((float)j1 + f1), (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (double)((float)k1 + f2)));
						}
					}
				}
			}

			if(l > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
				this.rainSoundCounter = 0;
				if(d1 > entityliving.posY + 1.0D && world.findTopSolidBlock(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posZ)) > MathHelper.floor_double(entityliving.posY)) {
					this.mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F);
				} else {
					this.mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F);
				}
			}

		}
	}

	protected void renderRainSnow(float f) {
		float f1 = this.mc.theWorld.func_27162_g(f);
		if(f1 > 0.0F) {
			EntityLiving entityliving = this.mc.renderViewEntity;
			World world = this.mc.theWorld;
			int i = MathHelper.floor_double(entityliving.posX);
			int j = MathHelper.floor_double(entityliving.posY);
			int k = MathHelper.floor_double(entityliving.posZ);
			Tessellator tessellator = Tessellator.instance;
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/snow.png"));
			double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
			double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
			double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
			int l = MathHelper.floor_double(d1);
			byte i1 = 5;
			if(this.mc.gameSettings.fancyGraphics) {
				i1 = 10;
			}

			BiomeGenBase[] abiomegenbase = world.getWorldChunkManager().func_4069_a(i - i1, k - i1, i1 * 2 + 1, i1 * 2 + 1);
			int j1 = 0;

			int l1;
			int j2;
			BiomeGenBase biomegenbase1;
			int l2;
			int j3;
			int l3;
			float f4;
			for(l1 = i - i1; l1 <= i + i1; ++l1) {
				for(j2 = k - i1; j2 <= k + i1; ++j2) {
					biomegenbase1 = abiomegenbase[j1++];
					if(biomegenbase1.getEnableSnow()) {
						l2 = world.findTopSolidBlock(l1, j2);
						if(l2 < 0) {
							l2 = 0;
						}

						j3 = l2;
						if(l2 < l) {
							j3 = l;
						}

						l3 = j - i1;
						int f2 = j + i1;
						if(l3 < l2) {
							l3 = l2;
						}

						if(f2 < l2) {
							f2 = l2;
						}

						f4 = 1.0F;
						if(l3 != f2) {
							this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 + j2 * j2 * 418711 + j2 * 13761));
							float d3 = (float)this.rendererUpdateCount + f;
							float f6 = ((float)(this.rendererUpdateCount & 511) + f) / 512.0F;
							float d4 = this.random.nextFloat() + d3 * 0.01F * (float)this.random.nextGaussian();
							float f8 = this.random.nextFloat() + d3 * (float)this.random.nextGaussian() * 0.001F;
							double f9 = (double)((float)l1 + 0.5F) - entityliving.posX;
							double d6 = (double)((float)j2 + 0.5F) - entityliving.posZ;
							float f11 = MathHelper.sqrt_double(f9 * f9 + d6 * d6) / (float)i1;
							tessellator.startDrawingQuads();
							float f12 = world.getLightBrightness(l1, j3, j2);
							GL11.glColor4f(f12, f12, f12, ((1.0F - f11 * f11) * 0.3F + 0.5F) * f1);
							tessellator.setTranslationD(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
							tessellator.addVertexWithUV((double)(l1 + 0), (double)l3, (double)j2 + 0.5D, (double)(0.0F * f4 + d4), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)(l1 + 1), (double)l3, (double)j2 + 0.5D, (double)(1.0F * f4 + d4), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)(l1 + 1), (double)f2, (double)j2 + 0.5D, (double)(1.0F * f4 + d4), (double)((float)f2 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)(l1 + 0), (double)f2, (double)j2 + 0.5D, (double)(0.0F * f4 + d4), (double)((float)f2 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 0), (double)(0.0F * f4 + d4), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 1), (double)(1.0F * f4 + d4), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)f2, (double)(j2 + 1), (double)(1.0F * f4 + d4), (double)((float)f2 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)f2, (double)(j2 + 0), (double)(0.0F * f4 + d4), (double)((float)f2 * f4 / 4.0F + f6 * f4 + f8));
							tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
							tessellator.draw();
						}
					}
				}
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/rain.png"));
			if(this.mc.gameSettings.fancyGraphics) {
				i1 = 10;
			}

			j1 = 0;

			for(l1 = i - i1; l1 <= i + i1; ++l1) {
				for(j2 = k - i1; j2 <= k + i1; ++j2) {
					biomegenbase1 = abiomegenbase[j1++];
					if(biomegenbase1.canSpawnLightningBolt()) {
						l2 = world.findTopSolidBlock(l1, j2);
						j3 = j - i1;
						l3 = j + i1;
						if(j3 < l2) {
							j3 = l2;
						}

						if(l3 < l2) {
							l3 = l2;
						}

						float var37 = 1.0F;
						if(j3 != l3) {
							this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 + j2 * j2 * 418711 + j2 * 13761));
							f4 = ((float)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + j2 * j2 * 418711 + j2 * 13761 & 31) + f) / 32.0F * (3.0F + this.random.nextFloat());
							double var38 = (double)((float)l1 + 0.5F) - entityliving.posX;
							double var39 = (double)((float)j2 + 0.5F) - entityliving.posZ;
							float var40 = MathHelper.sqrt_double(var38 * var38 + var39 * var39) / (float)i1;
							tessellator.startDrawingQuads();
							float f10 = world.getLightBrightness(l1, 128, j2) * 0.85F + 0.15F;
							GL11.glColor4f(f10, f10, f10, ((1.0F - var40 * var40) * 0.5F + 0.5F) * f1);
							tessellator.setTranslationD(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
							tessellator.addVertexWithUV((double)(l1 + 0), (double)j3, (double)j2 + 0.5D, (double)(0.0F * var37), (double)((float)j3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)(l1 + 1), (double)j3, (double)j2 + 0.5D, (double)(1.0F * var37), (double)((float)j3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)(l1 + 1), (double)l3, (double)j2 + 0.5D, (double)(1.0F * var37), (double)((float)l3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)(l1 + 0), (double)l3, (double)j2 + 0.5D, (double)(0.0F * var37), (double)((float)l3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)j3, (double)(j2 + 0), (double)(0.0F * var37), (double)((float)j3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)j3, (double)(j2 + 1), (double)(1.0F * var37), (double)((float)j3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 1), (double)(1.0F * var37), (double)((float)l3 * var37 / 4.0F + f4 * var37));
							tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 0), (double)(0.0F * var37), (double)((float)l3 * var37 / 4.0F + f4 * var37));
							tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
							tessellator.draw();
						}
					}
				}
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		}
	}

	public void func_905_b() {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.field_25121_a, scaledresolution.field_25120_b, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float f) {
		World world = this.mc.theWorld;
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f1 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
		f1 = 1.0F - (float)Math.pow((double)f1, 0.25D);
		Vec3D vec3d = world.func_4079_a(this.mc.renderViewEntity, f);
		float f2 = (float)vec3d.xCoord;
		float f3 = (float)vec3d.yCoord;
		float f4 = (float)vec3d.zCoord;
		Vec3D vec3d1 = world.getFogColor(f);
		this.fogColorRed = (float)vec3d1.xCoord;
		this.fogColorGreen = (float)vec3d1.yCoord;
		this.fogColorBlue = (float)vec3d1.zCoord;
		this.fogColorRed += (f2 - this.fogColorRed) * f1;
		this.fogColorGreen += (f3 - this.fogColorGreen) * f1;
		this.fogColorBlue += (f4 - this.fogColorBlue) * f1;
		float f5 = world.func_27162_g(f);
		float f7;
		float f10;
		if(f5 > 0.0F) {
			f7 = 1.0F - f5 * 0.5F;
			f10 = 1.0F - f5 * 0.4F;
			this.fogColorRed *= f7;
			this.fogColorGreen *= f7;
			this.fogColorBlue *= f10;
		}

		f7 = world.func_27166_f(f);
		if(f7 > 0.0F) {
			f10 = 1.0F - f7 * 0.5F;
			this.fogColorRed *= f10;
			this.fogColorGreen *= f10;
			this.fogColorBlue *= f10;
		}

		if(this.cloudFog) {
			Vec3D f101 = world.func_628_d(f);
			this.fogColorRed = (float)f101.xCoord;
			this.fogColorGreen = (float)f101.yCoord;
			this.fogColorBlue = (float)f101.zCoord;
		} else if(entityliving.isInsideOfMaterial(Material.water)) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.02F;
			this.fogColorBlue = 0.2F;
		} else if(entityliving.isInsideOfMaterial(Material.lava)) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}

		f10 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * f;
		this.fogColorRed *= f10;
		this.fogColorGreen *= f10;
		this.fogColorBlue *= f10;
		if(this.mc.gameSettings.anaglyph) {
			float f11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			float f12 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float f13 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = f11;
			this.fogColorGreen = f12;
			this.fogColorBlue = f13;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog(int i, float f) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		GL11.glFog(GL11.GL_FOG_COLOR, this.func_908_a(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f3;
		float f6;
		float f9;
		float f12;
		float f15;
		float f18;
		if(this.cloudFog) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			f3 = 1.0F;
			f6 = 1.0F;
			f9 = 1.0F;
			if(this.mc.gameSettings.anaglyph) {
				f12 = (f3 * 30.0F + f6 * 59.0F + f9 * 11.0F) / 100.0F;
				f15 = (f3 * 30.0F + f6 * 70.0F) / 100.0F;
				f18 = (f3 * 30.0F + f9 * 70.0F) / 100.0F;
			}
		} else if(entityliving.isInsideOfMaterial(Material.water)) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			f3 = 0.4F;
			f6 = 0.4F;
			f9 = 0.9F;
			if(this.mc.gameSettings.anaglyph) {
				f12 = (f3 * 30.0F + f6 * 59.0F + f9 * 11.0F) / 100.0F;
				f15 = (f3 * 30.0F + f6 * 70.0F) / 100.0F;
				f18 = (f3 * 30.0F + f9 * 70.0F) / 100.0F;
			}
		} else if(entityliving.isInsideOfMaterial(Material.lava)) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			f3 = 0.4F;
			f6 = 0.3F;
			f9 = 0.3F;
			if(this.mc.gameSettings.anaglyph) {
				f12 = (f3 * 30.0F + f6 * 59.0F + f9 * 11.0F) / 100.0F;
				f15 = (f3 * 30.0F + f6 * 70.0F) / 100.0F;
				f18 = (f3 * 30.0F + f9 * 70.0F) / 100.0F;
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
			if(i < 0) {
				GL11.glFogf(GL11.GL_FOG_START, 0.0F);
				GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance * 0.8F);
			}

			if(GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(NVFogDistance.GL_FOG_DISTANCE_MODE_NV, NVFogDistance.GL_EYE_RADIAL_NV);
			}

			if(this.mc.theWorld.worldProvider.isNether) {
				GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			}
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}

	private FloatBuffer func_908_a(float f, float f1, float f2, float f3) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(f).put(f1).put(f2).put(f3);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}
}
