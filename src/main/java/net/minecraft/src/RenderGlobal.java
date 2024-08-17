package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import net.minecraft.src.Space.Client;

public class RenderGlobal implements IWorldAccess {
	public List tileEntities = new ArrayList();
	private World worldObj;
	private RenderEngine renderEngine;
	private List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	private Minecraft mc;
	private RenderBlocks globalRenderBlocks;
	private IntBuffer glOcclusionQueryBase;
	private boolean occlusionEnabled = false;
	private int cloudOffsetX = 0;
	private int starGLCallList;
	private int glSkyList;
	private int glSkyList2;
	private int minBlockX;
	private int minBlockY;
	private int minBlockZ;
	private int maxBlockX;
	private int maxBlockY;
	private int maxBlockZ;
	private int renderDistance = -1;
	private int renderEntitiesStartupCounter = 2;
	private int countEntitiesTotal;
	private int countEntitiesRendered;
	private int countEntitiesHidden;
	int[] dummyBuf50k = new int['\uc350'];
	IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
	private int renderersLoaded;
	private int renderersBeingClipped;
	private int renderersBeingOccluded;
	private int renderersBeingRendered;
	private int renderersSkippingRenderPass;
	private int worldRenderersCheckIndex;
	private List glRenderLists = new ArrayList();
	private RenderList[] allRenderLists = new RenderList[]{new RenderList(), new RenderList(), new RenderList(), new RenderList()};
	int dummyInt0 = 0;
	int glDummyList = GLAllocation.generateDisplayLists(1);
	double prevSortX = -9999.0D;
	double prevSortY = -9999.0D;
	double prevSortZ = -9999.0D;
	public float damagePartialTime;
	int frustrumCheckOffset = 0;

	public RenderGlobal(Minecraft minecraft, RenderEngine renderengine) {
		this.mc = minecraft;
		this.renderEngine = renderengine;
		byte byte0 = 64;
		this.glRenderListBase = GLAllocation.generateDisplayLists(byte0 * byte0 * byte0 * 3);
		this.occlusionEnabled = minecraft.getOpenGlCapsChecker().checkARBOcclusion();
		if(this.occlusionEnabled) {
			this.occlusionResult.clear();
			this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(byte0 * byte0 * byte0);
			this.glOcclusionQueryBase.clear();
			this.glOcclusionQueryBase.position(0);
			this.glOcclusionQueryBase.limit(byte0 * byte0 * byte0);
			ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
		}

		this.starGLCallList = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
		this.renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		Tessellator tessellator = Tessellator.instance;
		this.glSkyList = this.starGLCallList + 1;
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		byte byte1 = 64;
		int i = 256 / byte1 + 2;
		float f = 16.0F;

		int k;
		int i1;
		for(k = -byte1 * i; k <= byte1 * i; k += byte1) {
			for(i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1) {
				tessellator.startDrawingQuads();
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + byte1));
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + byte1));
				tessellator.draw();
			}
		}

		GL11.glEndList();
		this.glSkyList2 = this.starGLCallList + 2;
		GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
		f = -16.0F;
		tessellator.startDrawingQuads();

		for(k = -byte1 * i; k <= byte1 * i; k += byte1) {
			for(i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1) {
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + byte1));
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + byte1));
			}
		}

		tessellator.draw();
		GL11.glEndList();
	}

	private void renderStars() {
		Random random = new Random(10842L);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for(int i = 0; i < 1500; ++i) {
			double d = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.25F + random.nextFloat() * 0.25F);
			double d4 = d * d + d1 * d1 + d2 * d2;
			if(d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d * d + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for(int j = 0; j < 4; ++j) {
					double d17 = 0.0D;
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d24 = d21 * d12 + d17 * d13;
					double d25 = d17 * d12 - d21 * d13;
					double d26 = d25 * d9 - d22 * d10;
					double d28 = d22 * d9 + d25 * d10;
					tessellator.addVertex(d5 + d26, d6 + d24, d7 + d28);
				}
			}
		}

		tessellator.draw();
	}

	public void changeWorld(World world) {
		if(this.worldObj != null) {
			this.worldObj.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0D;
		this.prevSortY = -9999.0D;
		this.prevSortZ = -9999.0D;
		RenderManager.instance.func_852_a(world);
		this.worldObj = world;
		this.globalRenderBlocks = new RenderBlocks(world);
		if(world != null) {
			world.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	public void loadRenderers() {
		Block.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
		this.renderDistance = this.mc.gameSettings.renderDistance;
		int j;
		if(this.worldRenderers != null) {
			for(j = 0; j < this.worldRenderers.length; ++j) {
				this.worldRenderers[j].func_1204_c();
			}
		}

		j = 64 << 3 - this.renderDistance;
		if(j > 400) {
			j = 400;
		}

		this.renderChunksWide = j / 16 + 1;
		this.renderChunksTall = 8;
		this.renderChunksDeep = j / 16 + 1;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		int k = 0;
		int l = 0;
		this.minBlockX = 0;
		this.minBlockY = 0;
		this.minBlockZ = 0;
		this.maxBlockX = this.renderChunksWide;
		this.maxBlockY = this.renderChunksTall;
		this.maxBlockZ = this.renderChunksDeep;

		int entityliving;
		for(entityliving = 0; entityliving < this.worldRenderersToUpdate.size(); ++entityliving) {
			((WorldRenderer)this.worldRenderersToUpdate.get(entityliving)).needsUpdate = false;
		}

		this.worldRenderersToUpdate.clear();
		this.tileEntities.clear();

		for(entityliving = 0; entityliving < this.renderChunksWide; ++entityliving) {
			for(int k1 = 0; k1 < this.renderChunksTall; ++k1) {
				for(int l1 = 0; l1 < this.renderChunksDeep; ++l1) {
					this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving] = new WorldRenderer(this.worldObj, this.tileEntities, entityliving * 16, k1 * 16, l1 * 16, 16, this.glRenderListBase + k);
					if(this.occlusionEnabled) {
						this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving].glOcclusionQuery = this.glOcclusionQueryBase.get(l);
					}

					this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving].isWaitingOnOcclusionQuery = false;
					this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving].isVisible = true;
					this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving].isInFrustum = true;
					this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving].chunkIndex = l++;
					this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving].markDirty();
					this.sortedWorldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving] = this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving];
					this.worldRenderersToUpdate.add(this.worldRenderers[(l1 * this.renderChunksTall + k1) * this.renderChunksWide + entityliving]);
					k += 3;
				}
			}
		}

		if(this.worldObj != null) {
			EntityLiving var7 = this.mc.renderViewEntity;
			if(var7 != null) {
				this.markRenderersForNewPosition(MathHelper.floor_double(var7.posX), MathHelper.floor_double(var7.posY), MathHelper.floor_double(var7.posZ));
				Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var7));
			}
		}

		this.renderEntitiesStartupCounter = 2;
	}
	public void TracerLine(boolean flag, float var3) {
		try {
			boolean bobbing = this.mc.gameSettings.viewBobbing;
			this.mc.gameSettings.viewBobbing = false;
			this.mc.entityRenderer.setupCameraTransform(var3, 0);
			Vec3D forward = null;
			forward = new Vec3D(0.0D, 0.0D, 1.0D);
			forward.rotateAroundX((float)(-Math.toRadians((double)this.mc.thePlayer.rotationPitch)));
			forward.rotateAroundY((float)(-Math.toRadians((double)this.mc.thePlayer.rotationYaw)));
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor3f(255.0F, 0.0F, 0.0F);
			GL11.glLineWidth(3.0F);
			Iterator var6 = this.mc.theWorld.playerEntities.iterator();

			while(var6.hasNext()) {
				EntityPlayer entity = (EntityPlayer)var6.next();
				if(entity.entityId != this.mc.thePlayer.entityId) {
					double d2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)var3;
					double d3 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)var3;
					double d4 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)var3;
					double diffX = d2 - RenderManager.renderPosX;
					double diffY = d3 - RenderManager.renderPosY;
					double diffZ = d4 - RenderManager.renderPosZ;
					Tessellator.instance.startDrawing(1);
					Tessellator.instance.addVertex(forward.xCoord, forward.yCoord, forward.zCoord);
					Tessellator.instance.addVertex(diffX, diffY, diffZ);
					Tessellator.instance.draw();
				}
			}

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			this.mc.gameSettings.viewBobbing = bobbing;
			this.mc.entityRenderer.setupCameraTransform(var3, 0);
		} catch (Exception var19) {
		}

	}
	public void renderEntities(Vec3D vec3d, ICamera icamera, float f) {
		if(Client.hacks[27].isToggled){
			this.TracerLine(true, f);
		}
		if(this.renderEntitiesStartupCounter > 0) {
			--this.renderEntitiesStartupCounter;
		} else {
			TileEntityRenderer.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, f);
			RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.gameSettings, f);
			this.countEntitiesTotal = 0;
			this.countEntitiesRendered = 0;
			this.countEntitiesHidden = 0;
			EntityLiving entityliving = this.mc.renderViewEntity;
			RenderManager.renderPosX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
			RenderManager.renderPosY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
			RenderManager.renderPosZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
			TileEntityRenderer.staticPlayerX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
			TileEntityRenderer.staticPlayerY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
			TileEntityRenderer.staticPlayerZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
			List list = this.worldObj.getLoadedEntityList();
			this.countEntitiesTotal = list.size();

			int k;
			Entity entity1;
			for(k = 0; k < this.worldObj.weatherEffects.size(); ++k) {
				entity1 = (Entity)this.worldObj.weatherEffects.get(k);
				++this.countEntitiesRendered;
				if(entity1.isInRangeToRenderVec3D(vec3d)) {
					RenderManager.instance.renderEntity(entity1, f);
				}
			}

			for(k = 0; k < list.size(); ++k) {
				entity1 = (Entity)list.get(k);
				if(entity1.isInRangeToRenderVec3D(vec3d) && (entity1.ignoreFrustumCheck || icamera.isBoundingBoxInFrustum(entity1.boundingBox)) && (entity1 != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView || this.mc.renderViewEntity.isPlayerSleeping())) {
					int l = MathHelper.floor_double(entity1.posY);
					if(l < 0) {
						l = 0;
					}

					if(l >= 128) {
						l = 127;
					}

					if(this.worldObj.blockExists(MathHelper.floor_double(entity1.posX), l, MathHelper.floor_double(entity1.posZ))) {
						++this.countEntitiesRendered;
						RenderManager.instance.renderEntity(entity1, f);
					}
				}
			}

			for(k = 0; k < this.tileEntities.size(); ++k) {
				TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(k), f);
			}

		}
	}

	public String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
	}

	public String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
	}

	private void markRenderersForNewPosition(int i, int j, int k) {
		i -= 8;
		j -= 8;
		k -= 8;
		this.minBlockX = Integer.MAX_VALUE;
		this.minBlockY = Integer.MAX_VALUE;
		this.minBlockZ = Integer.MAX_VALUE;
		this.maxBlockX = Integer.MIN_VALUE;
		this.maxBlockY = Integer.MIN_VALUE;
		this.maxBlockZ = Integer.MIN_VALUE;
		int l = this.renderChunksWide * 16;
		int i1 = l / 2;

		for(int j1 = 0; j1 < this.renderChunksWide; ++j1) {
			int k1 = j1 * 16;
			int l1 = k1 + i1 - i;
			if(l1 < 0) {
				l1 -= l - 1;
			}

			l1 /= l;
			k1 -= l1 * l;
			if(k1 < this.minBlockX) {
				this.minBlockX = k1;
			}

			if(k1 > this.maxBlockX) {
				this.maxBlockX = k1;
			}

			for(int i2 = 0; i2 < this.renderChunksDeep; ++i2) {
				int j2 = i2 * 16;
				int k2 = j2 + i1 - k;
				if(k2 < 0) {
					k2 -= l - 1;
				}

				k2 /= l;
				j2 -= k2 * l;
				if(j2 < this.minBlockZ) {
					this.minBlockZ = j2;
				}

				if(j2 > this.maxBlockZ) {
					this.maxBlockZ = j2;
				}

				for(int l2 = 0; l2 < this.renderChunksTall; ++l2) {
					int i3 = l2 * 16;
					if(i3 < this.minBlockY) {
						this.minBlockY = i3;
					}

					if(i3 > this.maxBlockY) {
						this.maxBlockY = i3;
					}

					WorldRenderer worldrenderer = this.worldRenderers[(i2 * this.renderChunksTall + l2) * this.renderChunksWide + j1];
					boolean flag = worldrenderer.needsUpdate;
					worldrenderer.setPosition(k1, i3, j2);
					if(!flag && worldrenderer.needsUpdate) {
						this.worldRenderersToUpdate.add(worldrenderer);
					}
				}
			}
		}

	}

	public int sortAndRender(EntityLiving entityliving, int i, double d) {
		for(int d1 = 0; d1 < 10; ++d1) {
			this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
			WorldRenderer worldrenderer = this.worldRenderers[this.worldRenderersCheckIndex];
			if(worldrenderer.needsUpdate && !this.worldRenderersToUpdate.contains(worldrenderer)) {
				this.worldRenderersToUpdate.add(worldrenderer);
			}
		}

		if(this.mc.gameSettings.renderDistance != this.renderDistance) {
			this.loadRenderers();
		}

		if(i == 0) {
			this.renderersLoaded = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
			this.renderersSkippingRenderPass = 0;
		}

		double var34 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * d;
		double d2 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * d;
		double d3 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * d;
		double d4 = entityliving.posX - this.prevSortX;
		double d5 = entityliving.posY - this.prevSortY;
		double d6 = entityliving.posZ - this.prevSortZ;
		if(d4 * d4 + d5 * d5 + d6 * d6 > 16.0D) {
			this.prevSortX = entityliving.posX;
			this.prevSortY = entityliving.posY;
			this.prevSortZ = entityliving.posZ;
			this.markRenderersForNewPosition(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityliving));
		}

		RenderHelper.disableStandardItemLighting();
		byte k = 0;
		int var35;
		if(this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && i == 0) {
			byte l = 0;
			int i1 = 16;
			this.checkOcclusionQueryResult(l, i1);

			int byte0;
			for(byte0 = l; byte0 < i1; ++byte0) {
				this.sortedWorldRenderers[byte0].isVisible = true;
			}

			var35 = k + this.renderSortedRenderers(l, i1, i, d);

			do {
				byte0 = i1;
				i1 *= 2;
				if(i1 > this.sortedWorldRenderers.length) {
					i1 = this.sortedWorldRenderers.length;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_FOG);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				this.checkOcclusionQueryResult(byte0, i1);
				GL11.glPushMatrix();
				float f = 0.0F;
				float f1 = 0.0F;
				float f2 = 0.0F;

				for(int k1 = byte0; k1 < i1; ++k1) {
					if(this.sortedWorldRenderers[k1].skipAllRenderPasses()) {
						this.sortedWorldRenderers[k1].isInFrustum = false;
					} else {
						if(!this.sortedWorldRenderers[k1].isInFrustum) {
							this.sortedWorldRenderers[k1].isVisible = true;
						}

						if(this.sortedWorldRenderers[k1].isInFrustum && !this.sortedWorldRenderers[k1].isWaitingOnOcclusionQuery) {
							float f3 = MathHelper.sqrt_float(this.sortedWorldRenderers[k1].distanceToEntitySquared(entityliving));
							int l1 = (int)(1.0F + f3 / 128.0F);
							if(this.cloudOffsetX % l1 == k1 % l1) {
								WorldRenderer worldrenderer1 = this.sortedWorldRenderers[k1];
								float f4 = (float)((double)worldrenderer1.posXMinus - var34);
								float f5 = (float)((double)worldrenderer1.posYMinus - d2);
								float f6 = (float)((double)worldrenderer1.posZMinus - d3);
								float f7 = f4 - f;
								float f8 = f5 - f1;
								float f9 = f6 - f2;
								if(f7 != 0.0F || f8 != 0.0F || f9 != 0.0F) {
									GL11.glTranslatef(f7, f8, f9);
									f += f7;
									f1 += f8;
									f2 += f9;
								}

								ARBOcclusionQuery.glBeginQueryARB(GL15.GL_SAMPLES_PASSED, this.sortedWorldRenderers[k1].glOcclusionQuery);
								this.sortedWorldRenderers[k1].callOcclusionQueryList();
								ARBOcclusionQuery.glEndQueryARB(GL15.GL_SAMPLES_PASSED);
								this.sortedWorldRenderers[k1].isWaitingOnOcclusionQuery = true;
							}
						}
					}
				}

				GL11.glPopMatrix();
				if(this.mc.gameSettings.anaglyph) {
					if(EntityRenderer.anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_FOG);
				var35 += this.renderSortedRenderers(byte0, i1, i, d);
			} while(i1 < this.sortedWorldRenderers.length);
		} else {
			var35 = k + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, i, d);
		}

		return var35;
	}

	private void checkOcclusionQueryResult(int i, int j) {
		for(int k = i; k < j; ++k) {
			if(this.sortedWorldRenderers[k].isWaitingOnOcclusionQuery) {
				this.occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[k].glOcclusionQuery, GL15.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
				if(this.occlusionResult.get(0) != 0) {
					this.sortedWorldRenderers[k].isWaitingOnOcclusionQuery = false;
					this.occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[k].glOcclusionQuery, GL15.GL_QUERY_RESULT, this.occlusionResult);
					this.sortedWorldRenderers[k].isVisible = this.occlusionResult.get(0) != 0;
				}
			}
		}

	}

	private int renderSortedRenderers(int i, int j, int k, double d) {
		this.glRenderLists.clear();
		int l = 0;

		for(int entityliving = i; entityliving < j; ++entityliving) {
			if(k == 0) {
				++this.renderersLoaded;
				if(this.sortedWorldRenderers[entityliving].skipRenderPass[k]) {
					++this.renderersSkippingRenderPass;
				} else if(!this.sortedWorldRenderers[entityliving].isInFrustum) {
					++this.renderersBeingClipped;
				} else if(this.occlusionEnabled && !this.sortedWorldRenderers[entityliving].isVisible) {
					++this.renderersBeingOccluded;
				} else {
					++this.renderersBeingRendered;
				}
			}

			if(!this.sortedWorldRenderers[entityliving].skipRenderPass[k] && this.sortedWorldRenderers[entityliving].isInFrustum && (!this.occlusionEnabled || this.sortedWorldRenderers[entityliving].isVisible)) {
				int d1 = this.sortedWorldRenderers[entityliving].getGLCallListForPass(k);
				if(d1 >= 0) {
					this.glRenderLists.add(this.sortedWorldRenderers[entityliving]);
					++l;
				}
			}
		}

		EntityLiving var19 = this.mc.renderViewEntity;
		double var20 = var19.lastTickPosX + (var19.posX - var19.lastTickPosX) * d;
		double d2 = var19.lastTickPosY + (var19.posY - var19.lastTickPosY) * d;
		double d3 = var19.lastTickPosZ + (var19.posZ - var19.lastTickPosZ) * d;
		int k1 = 0;

		int i2;
		for(i2 = 0; i2 < this.allRenderLists.length; ++i2) {
			this.allRenderLists[i2].func_859_b();
		}

		for(i2 = 0; i2 < this.glRenderLists.size(); ++i2) {
			WorldRenderer worldrenderer = (WorldRenderer)this.glRenderLists.get(i2);
			int j2 = -1;

			for(int k2 = 0; k2 < k1; ++k2) {
				if(this.allRenderLists[k2].func_862_a(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus)) {
					j2 = k2;
				}
			}

			if(j2 < 0) {
				j2 = k1++;
				this.allRenderLists[j2].func_861_a(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus, var20, d2, d3);
			}

			this.allRenderLists[j2].func_858_a(worldrenderer.getGLCallListForPass(k));
		}

		this.renderAllRenderLists(k, d);
		return l;
	}

	public void renderAllRenderLists(int i, double d) {
		for(int j = 0; j < this.allRenderLists.length; ++j) {
			this.allRenderLists[j].func_860_a();
		}

	}

	public void updateClouds() {
		++this.cloudOffsetX;
	}

	public void renderSky(float f) {
		if(!this.mc.theWorld.worldProvider.isNether) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			Vec3D vec3d = this.worldObj.func_4079_a(this.mc.renderViewEntity, f);
			float f1 = (float)vec3d.xCoord;
			float f2 = (float)vec3d.yCoord;
			float f3 = (float)vec3d.zCoord;
			float f6;
			if(this.mc.gameSettings.anaglyph) {
				float tessellator = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
				float af = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
				f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
				f1 = tessellator;
				f2 = af;
				f3 = f6;
			}

			GL11.glColor3f(f1, f2, f3);
			Tessellator var17 = Tessellator.instance;
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glColor3f(f1, f2, f3);
			GL11.glCallList(this.glSkyList);
			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.disableStandardItemLighting();
			float[] var18 = this.worldObj.worldProvider.calcSunriseSunsetColors(this.worldObj.getCelestialAngle(f), f);
			float f9;
			float f11;
			float f13;
			float f15;
			float f17;
			if(var18 != null) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glPushMatrix();
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				f6 = this.worldObj.getCelestialAngle(f);
				GL11.glRotatef(f6 <= 0.5F ? 0.0F : 180.0F, 0.0F, 0.0F, 1.0F);
				f9 = var18[0];
				f11 = var18[1];
				f13 = var18[2];
				float f20;
				if(this.mc.gameSettings.anaglyph) {
					f15 = (f9 * 30.0F + f11 * 59.0F + f13 * 11.0F) / 100.0F;
					f17 = (f9 * 30.0F + f11 * 70.0F) / 100.0F;
					f20 = (f9 * 30.0F + f13 * 70.0F) / 100.0F;
					f9 = f15;
					f11 = f17;
					f13 = f20;
				}

				var17.startDrawing(6);
				var17.setColorRGBA_F(f9, f11, f13, var18[3]);
				var17.addVertex(0.0D, 100.0D, 0.0D);
				byte var19 = 16;
				var17.setColorRGBA_F(var18[0], var18[1], var18[2], 0.0F);

				for(int var20 = 0; var20 <= var19; ++var20) {
					f20 = (float)var20 * 3.141593F * 2.0F / (float)var19;
					float f21 = MathHelper.sin(f20);
					float f22 = MathHelper.cos(f20);
					var17.addVertex((double)(f21 * 120.0F), (double)(f22 * 120.0F), (double)(-f22 * 40.0F * var18[3]));
				}

				var17.draw();
				GL11.glPopMatrix();
				GL11.glShadeModel(GL11.GL_FLAT);
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glPushMatrix();
			f6 = 1.0F - this.worldObj.func_27162_g(f);
			f9 = 0.0F;
			f11 = 0.0F;
			f13 = 0.0F;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f6);
			GL11.glTranslatef(f9, f11, f13);
			GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(this.worldObj.getCelestialAngle(f) * 360.0F, 1.0F, 0.0F, 0.0F);
			f15 = 30.0F;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
			var17.startDrawingQuads();
			var17.addVertexWithUV((double)(-f15), 100.0D, (double)(-f15), 0.0D, 0.0D);
			var17.addVertexWithUV((double)f15, 100.0D, (double)(-f15), 1.0D, 0.0D);
			var17.addVertexWithUV((double)f15, 100.0D, (double)f15, 1.0D, 1.0D);
			var17.addVertexWithUV((double)(-f15), 100.0D, (double)f15, 0.0D, 1.0D);
			var17.draw();
			f15 = 20.0F;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
			var17.startDrawingQuads();
			var17.addVertexWithUV((double)(-f15), -100.0D, (double)f15, 1.0D, 1.0D);
			var17.addVertexWithUV((double)f15, -100.0D, (double)f15, 0.0D, 1.0D);
			var17.addVertexWithUV((double)f15, -100.0D, (double)(-f15), 0.0D, 0.0D);
			var17.addVertexWithUV((double)(-f15), -100.0D, (double)(-f15), 1.0D, 0.0D);
			var17.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			f17 = this.worldObj.getStarBrightness(f) * f6;
			if(f17 > 0.0F) {
				GL11.glColor4f(f17, f17, f17, f17);
				GL11.glCallList(this.starGLCallList);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glPopMatrix();
			if(this.worldObj.worldProvider.func_28112_c()) {
				GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
			} else {
				GL11.glColor3f(f1, f2, f3);
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glCallList(this.glSkyList2);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
		}
	}

	public void renderClouds(float f) {
		if(!this.mc.theWorld.worldProvider.isNether) {
			if(this.mc.gameSettings.fancyGraphics) {
				this.renderCloudsFancy(f);
			} else {
				GL11.glDisable(GL11.GL_CULL_FACE);
				float f1 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)f);
				byte byte0 = 32;
				int i = 256 / byte0;
				Tessellator tessellator = Tessellator.instance;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				Vec3D vec3d = this.worldObj.func_628_d(f);
				float f2 = (float)vec3d.xCoord;
				float f3 = (float)vec3d.yCoord;
				float f4 = (float)vec3d.zCoord;
				float f6;
				if(this.mc.gameSettings.anaglyph) {
					f6 = (f2 * 30.0F + f3 * 59.0F + f4 * 11.0F) / 100.0F;
					float d = (f2 * 30.0F + f3 * 70.0F) / 100.0F;
					float f8 = (f2 * 30.0F + f4 * 70.0F) / 100.0F;
					f2 = f6;
					f3 = d;
					f4 = f8;
				}

				f6 = 4.882813E-4F;
				double d1 = this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)this.cloudOffsetX + f) * 0.03F);
				double d2 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)f;
				int j = MathHelper.floor_double(d1 / 2048.0D);
				int k = MathHelper.floor_double(d1 / 2048.0D);
				d1 -= (double)(j * 2048);
				d2 -= (double)(k * 2048);
				float f9 = this.worldObj.worldProvider.getCloudHeight() - f1 + 0.33F;
				float f10 = (float)(d1 * (double)f6);
				float f11 = (float)(d2 * (double)f6);
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_F(f2, f3, f4, 0.8F);

				for(int l = -byte0 * i; l < byte0 * i; l += byte0) {
					for(int i1 = -byte0 * i; i1 < byte0 * i; i1 += byte0) {
						tessellator.addVertexWithUV((double)(l + 0), (double)f9, (double)(i1 + byte0), (double)((float)(l + 0) * f6 + f10), (double)((float)(i1 + byte0) * f6 + f11));
						tessellator.addVertexWithUV((double)(l + byte0), (double)f9, (double)(i1 + byte0), (double)((float)(l + byte0) * f6 + f10), (double)((float)(i1 + byte0) * f6 + f11));
						tessellator.addVertexWithUV((double)(l + byte0), (double)f9, (double)(i1 + 0), (double)((float)(l + byte0) * f6 + f10), (double)((float)(i1 + 0) * f6 + f11));
						tessellator.addVertexWithUV((double)(l + 0), (double)f9, (double)(i1 + 0), (double)((float)(l + 0) * f6 + f10), (double)((float)(i1 + 0) * f6 + f11));
					}
				}

				tessellator.draw();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}
		}
	}

	public boolean func_27307_a(double d, double d1, double d2, float f) {
		return false;
	}

	public void renderCloudsFancy(float f) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f1 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)f);
		Tessellator tessellator = Tessellator.instance;
		float f2 = 12.0F;
		float f3 = 4.0F;
		double d = (this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)this.cloudOffsetX + f) * 0.03F)) / (double)f2;
		double d1 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)f) / (double)f2 + (double)0.33F;
		float f4 = this.worldObj.worldProvider.getCloudHeight() - f1 + 0.33F;
		int i = MathHelper.floor_double(d / 2048.0D);
		int j = MathHelper.floor_double(d1 / 2048.0D);
		d -= (double)(i * 2048);
		d1 -= (double)(j * 2048);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Vec3D vec3d = this.worldObj.func_628_d(f);
		float f5 = (float)vec3d.xCoord;
		float f6 = (float)vec3d.yCoord;
		float f7 = (float)vec3d.zCoord;
		float f9;
		float f11;
		float f13;
		if(this.mc.gameSettings.anaglyph) {
			f9 = (f5 * 30.0F + f6 * 59.0F + f7 * 11.0F) / 100.0F;
			f11 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
			f13 = (f5 * 30.0F + f7 * 70.0F) / 100.0F;
			f5 = f9;
			f6 = f11;
			f7 = f13;
		}

		f9 = (float)(d * 0.0D);
		f11 = (float)(d1 * 0.0D);
		f13 = 0.00390625F;
		f9 = (float)MathHelper.floor_double(d) * f13;
		f11 = (float)MathHelper.floor_double(d1) * f13;
		float f14 = (float)(d - (double)MathHelper.floor_double(d));
		float f15 = (float)(d1 - (double)MathHelper.floor_double(d1));
		byte k = 8;
		byte byte0 = 3;
		float f16 = 1.0F / 1024.0F;
		GL11.glScalef(f2, 1.0F, f2);

		for(int l = 0; l < 2; ++l) {
			if(l == 0) {
				GL11.glColorMask(false, false, false, false);
			} else if(this.mc.gameSettings.anaglyph) {
				if(EntityRenderer.anaglyphField == 0) {
					GL11.glColorMask(false, true, true, true);
				} else {
					GL11.glColorMask(true, false, false, true);
				}
			} else {
				GL11.glColorMask(true, true, true, true);
			}

			for(int i1 = -byte0 + 1; i1 <= byte0; ++i1) {
				for(int j1 = -byte0 + 1; j1 <= byte0; ++j1) {
					tessellator.startDrawingQuads();
					float f17 = (float)(i1 * k);
					float f18 = (float)(j1 * k);
					float f19 = f17 - f14;
					float f20 = f18 - f15;
					if(f4 > -f3 - 1.0F) {
						tessellator.setColorRGBA_F(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F);
						tessellator.setNormal(0.0F, -1.0F, 0.0F);
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
					}

					if(f4 <= f3 + 1.0F) {
						tessellator.setColorRGBA_F(f5, f6, f7, 0.8F);
						tessellator.setNormal(0.0F, 1.0F, 0.0F);
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3 - f16), (double)(f20 + (float)k), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3 - f16), (double)(f20 + (float)k), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3 - f16), (double)(f20 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3 - f16), (double)(f20 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
					}

					tessellator.setColorRGBA_F(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F);
					int j2;
					if(i1 > -1) {
						tessellator.setNormal(-1.0F, 0.0F, 0.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + f3), (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + f3), (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						}
					}

					if(i1 <= 1) {
						tessellator.setNormal(1.0F, 0.0F, 0.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + f3), (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + f3), (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						}
					}

					tessellator.setColorRGBA_F(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F);
					if(j1 > -1) {
						tessellator.setNormal(0.0F, 0.0F, -1.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3), (double)(f20 + (float)j2 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3), (double)(f20 + (float)j2 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + (float)j2 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + (float)j2 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
						}
					}

					if(j1 <= 1) {
						tessellator.setNormal(0.0F, 0.0F, 1.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3), (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3), (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
						}
					}

					tessellator.draw();
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public boolean updateRenderers(EntityLiving entityliving, boolean flag) {
		boolean flag1 = false;
		if(flag1) {
			Collections.sort(this.worldRenderersToUpdate, new RenderSorter(entityliving));
			int var17 = this.worldRenderersToUpdate.size() - 1;
			int var18 = this.worldRenderersToUpdate.size();

			for(int var19 = 0; var19 < var18; ++var19) {
				WorldRenderer var20 = (WorldRenderer)this.worldRenderersToUpdate.get(var17 - var19);
				if(!flag) {
					if(var20.distanceToEntitySquared(entityliving) > 256.0F) {
						if(var20.isInFrustum) {
							if(var19 >= 3) {
								return false;
							}
						} else if(var19 >= 1) {
							return false;
						}
					}
				} else if(!var20.isInFrustum) {
					continue;
				}

				var20.updateRenderer();
				this.worldRenderersToUpdate.remove(var20);
				var20.needsUpdate = false;
			}

			return this.worldRenderersToUpdate.size() == 0;
		} else {
			byte byte0 = 2;
			RenderSorter rendersorter = new RenderSorter(entityliving);
			WorldRenderer[] aworldrenderer = new WorldRenderer[byte0];
			ArrayList arraylist = null;
			int l = this.worldRenderersToUpdate.size();
			int i1 = 0;

			int l1;
			WorldRenderer j2;
			int l2;
			int j3;
			label172:
			for(l1 = 0; l1 < l; ++l1) {
				j2 = (WorldRenderer)this.worldRenderersToUpdate.get(l1);
				if(!flag) {
					if(j2.distanceToEntitySquared(entityliving) > 256.0F) {
						for(l2 = 0; l2 < byte0 && (aworldrenderer[l2] == null || rendersorter.doCompare(aworldrenderer[l2], j2) <= 0); ++l2) {
						}

						--l2;
						if(l2 <= 0) {
							continue;
						}

						j3 = l2;

						while(true) {
							--j3;
							if(j3 == 0) {
								aworldrenderer[l2] = j2;
								continue label172;
							}

							aworldrenderer[j3 - 1] = aworldrenderer[j3];
						}
					}
				} else if(!j2.isInFrustum) {
					continue;
				}

				if(arraylist == null) {
					arraylist = new ArrayList();
				}

				++i1;
				arraylist.add(j2);
				this.worldRenderersToUpdate.set(l1, (Object)null);
			}

			if(arraylist != null) {
				if(arraylist.size() > 1) {
					Collections.sort(arraylist, rendersorter);
				}

				for(l1 = arraylist.size() - 1; l1 >= 0; --l1) {
					j2 = (WorldRenderer)arraylist.get(l1);
					j2.updateRenderer();
					j2.needsUpdate = false;
				}
			}

			l1 = 0;

			int var21;
			for(var21 = byte0 - 1; var21 >= 0; --var21) {
				WorldRenderer var22 = aworldrenderer[var21];
				if(var22 != null) {
					if(!var22.isInFrustum && var21 != byte0 - 1) {
						aworldrenderer[var21] = null;
						aworldrenderer[0] = null;
						break;
					}

					aworldrenderer[var21].updateRenderer();
					aworldrenderer[var21].needsUpdate = false;
					++l1;
				}
			}

			var21 = 0;
			l2 = 0;

			for(j3 = this.worldRenderersToUpdate.size(); var21 != j3; ++var21) {
				WorldRenderer worldrenderer4 = (WorldRenderer)this.worldRenderersToUpdate.get(var21);
				if(worldrenderer4 != null) {
					boolean flag2 = false;

					for(int k3 = 0; k3 < byte0 && !flag2; ++k3) {
						if(worldrenderer4 == aworldrenderer[k3]) {
							flag2 = true;
						}
					}

					if(!flag2) {
						if(l2 != var21) {
							this.worldRenderersToUpdate.set(l2, worldrenderer4);
						}

						++l2;
					}
				}
			}

			while(true) {
				--var21;
				if(var21 < l2) {
					return l == i1 + l1;
				}

				this.worldRenderersToUpdate.remove(var21);
			}
		}
	}

	public void drawBlockBreaking(EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		int l;
		if(i == 0) {
			if(this.damagePartialTime > 0.0F) {
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
				int f1 = this.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, f1);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
				GL11.glPushMatrix();
				l = this.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
				Block i1 = l <= 0 ? null : Block.blocksList[l];
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glPolygonOffset(-3.0F, -3.0F);
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				double j1 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
				double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
				double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
				if(i1 == null) {
					i1 = Block.stone;
				}

				GL11.glEnable(GL11.GL_ALPHA_TEST);
				tessellator.startDrawingQuads();
				tessellator.setTranslationD(-j1, -d1, -d2);
				tessellator.disableColor();
				this.globalRenderBlocks.renderBlockUsingTexture(i1, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
				tessellator.draw();
				tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glPolygonOffset(0.0F, 0.0F);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glDepthMask(true);
				GL11.glPopMatrix();
			}
		} else if(itemstack != null) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float var16 = MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
			GL11.glColor4f(var16, var16, var16, MathHelper.sin((float)System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
			l = this.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, l);
			int var17 = movingobjectposition.blockX;
			int var18 = movingobjectposition.blockY;
			int k1 = movingobjectposition.blockZ;
			if(movingobjectposition.sideHit == 0) {
				--var18;
			}

			if(movingobjectposition.sideHit == 1) {
				++var18;
			}

			if(movingobjectposition.sideHit == 2) {
				--k1;
			}

			if(movingobjectposition.sideHit == 3) {
				++k1;
			}

			if(movingobjectposition.sideHit == 4) {
				--var17;
			}

			if(movingobjectposition.sideHit == 5) {
				++var17;
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public void drawSelectionBox(EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f) {
		if(!this.mc.gameSettings.hideGUI) {
			if(i == 0 && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
				GL11.glLineWidth(2.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDepthMask(false);
				float f1 = 0.002F;
				int j = this.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
				if(j > 0) {
					Block.blocksList[j].setBlockBoundsBasedOnState(this.worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
					double d = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
					double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
					double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
					this.drawOutlinedBoundingBox(Block.blocksList[j].getSelectedBoundingBoxFromPool(this.worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ).expand((double)f1, (double)f1, (double)f1).getOffsetBoundingBox(-d, -d1, -d2));
				}

				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
			}

		}
	}

	private void drawOutlinedBoundingBox(AxisAlignedBB axisalignedbb) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(3);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.draw();
		tessellator.startDrawing(3);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.draw();
		tessellator.startDrawing(1);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.draw();
	}

	public void func_949_a(int i, int j, int k, int l, int i1, int j1) {
		int k1 = MathHelper.bucketInt(i, 16);
		int l1 = MathHelper.bucketInt(j, 16);
		int i2 = MathHelper.bucketInt(k, 16);
		int j2 = MathHelper.bucketInt(l, 16);
		int k2 = MathHelper.bucketInt(i1, 16);
		int l2 = MathHelper.bucketInt(j1, 16);

		for(int i3 = k1; i3 <= j2; ++i3) {
			int j3 = i3 % this.renderChunksWide;
			if(j3 < 0) {
				j3 += this.renderChunksWide;
			}

			for(int k3 = l1; k3 <= k2; ++k3) {
				int l3 = k3 % this.renderChunksTall;
				if(l3 < 0) {
					l3 += this.renderChunksTall;
				}

				for(int i4 = i2; i4 <= l2; ++i4) {
					int j4 = i4 % this.renderChunksDeep;
					if(j4 < 0) {
						j4 += this.renderChunksDeep;
					}

					int k4 = (j4 * this.renderChunksTall + l3) * this.renderChunksWide + j3;
					WorldRenderer worldrenderer = this.worldRenderers[k4];
					if(!worldrenderer.needsUpdate) {
						this.worldRenderersToUpdate.add(worldrenderer);
						worldrenderer.markDirty();
					}
				}
			}
		}

	}

	public void markBlockAndNeighborsNeedsUpdate(int i, int j, int k) {
		this.func_949_a(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
	}

	public void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1) {
		this.func_949_a(i - 1, j - 1, k - 1, l + 1, i1 + 1, j1 + 1);
	}

	public void clipRenderersByFrustrum(ICamera icamera, float f) {
		for(int i = 0; i < this.worldRenderers.length; ++i) {
			if(!this.worldRenderers[i].skipAllRenderPasses() && (!this.worldRenderers[i].isInFrustum || (i + this.frustrumCheckOffset & 15) == 0)) {
				this.worldRenderers[i].updateInFrustrum(icamera);
			}
		}

		++this.frustrumCheckOffset;
	}

	public void playRecord(String s, int i, int j, int k) {
		if(s != null) {
			this.mc.ingameGUI.setRecordPlayingMessage("C418 - " + s);
		}

		this.mc.sndManager.playStreaming(s, (float)i, (float)j, (float)k, 1.0F, 1.0F);
	}

	public void playSound(String s, double d, double d1, double d2, float f, float f1) {
		float f2 = 16.0F;
		if(f > 1.0F) {
			f2 *= f;
		}

		if(this.mc.renderViewEntity.getDistanceSq(d, d1, d2) < (double)(f2 * f2)) {
			this.mc.sndManager.playSound(s, (float)d, (float)d1, (float)d2, f, f1);
		}

	}

	public void spawnParticle(String s, double d, double d1, double d2, double d3, double d4, double d5) {
		if(this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
			double d6 = this.mc.renderViewEntity.posX - d;
			double d7 = this.mc.renderViewEntity.posY - d1;
			double d8 = this.mc.renderViewEntity.posZ - d2;
			double d9 = 16.0D;
			if(d6 * d6 + d7 * d7 + d8 * d8 <= d9 * d9) {
				if(s.equals("bubble")) {
					this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("smoke")) {
					this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("note")) {
					this.mc.effectRenderer.addEffect(new EntityNoteFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("portal")) {
					this.mc.effectRenderer.addEffect(new EntityPortalFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("explode")) {
					this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("flame")) {
					this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("lava")) {
					this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, d, d1, d2));
				} else if(s.equals("footstep")) {
					this.mc.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.worldObj, d, d1, d2));
				} else if(s.equals("splash")) {
					this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("largesmoke")) {
					this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, d, d1, d2, d3, d4, d5, 2.5F));
				} else if(s.equals("reddust")) {
					this.mc.effectRenderer.addEffect(new EntityReddustFX(this.worldObj, d, d1, d2, (float)d3, (float)d4, (float)d5));
				} else if(s.equals("snowballpoof")) {
					this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, d, d1, d2, Item.snowball));
				} else if(s.equals("snowshovel")) {
					this.mc.effectRenderer.addEffect(new EntitySnowShovelFX(this.worldObj, d, d1, d2, d3, d4, d5));
				} else if(s.equals("slime")) {
					this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, d, d1, d2, Item.slimeBall));
				} else if(s.equals("heart")) {
					this.mc.effectRenderer.addEffect(new EntityHeartFX(this.worldObj, d, d1, d2, d3, d4, d5));
				}

			}
		}
	}

	public void obtainEntitySkin(Entity entity) {
		entity.updateCloak();
		if(entity.skinUrl != null) {
			this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
		}

		if(entity.cloakUrl != null) {
			this.renderEngine.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
		}

	}

	public void releaseEntitySkin(Entity entity) {
		if(entity.skinUrl != null) {
			this.renderEngine.releaseImageData(entity.skinUrl);
		}

		if(entity.cloakUrl != null) {
			this.renderEngine.releaseImageData(entity.cloakUrl);
		}

	}

	public void updateAllRenderers() {
		for(int i = 0; i < this.worldRenderers.length; ++i) {
			if(this.worldRenderers[i].isChunkLit && !this.worldRenderers[i].needsUpdate) {
				this.worldRenderersToUpdate.add(this.worldRenderers[i]);
				this.worldRenderers[i].markDirty();
			}
		}

	}

	public void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity) {
	}

	public void func_28137_f() {
		GLAllocation.func_28194_b(this.glRenderListBase);
	}

	public void func_28136_a(EntityPlayer entityplayer, int i, int j, int k, int l, int i1) {
		Random random = this.worldObj.rand;
		int i2;
		switch(i) {
		case 1000:
			this.worldObj.playSoundEffect((double)j, (double)k, (double)l, "random.click", 1.0F, 1.0F);
			break;
		case 1001:
			this.worldObj.playSoundEffect((double)j, (double)k, (double)l, "random.click", 1.0F, 1.2F);
			break;
		case 1002:
			this.worldObj.playSoundEffect((double)j, (double)k, (double)l, "random.old_bow", 1.0F, 1.2F);
			break;
		case 1003:
			if(Math.random() < 0.5D) {
				this.worldObj.playSoundEffect((double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, "random.old_door_open", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			} else {
				this.worldObj.playSoundEffect((double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, "random.old_door_close", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			break;
		case 1004:
			this.worldObj.playSoundEffect((double)((float)j + 0.5F), (double)((float)k + 0.5F), (double)((float)l + 0.5F), "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
			break;
		case 1005:
			if(Item.itemsList[i1] instanceof ItemRecord) {
				this.worldObj.playRecord(((ItemRecord)Item.itemsList[i1]).recordName, j, k, l);
			} else {
				this.worldObj.playRecord((String)null, j, k, l);
			}
			break;
		case 2000:
			int j1 = i1 % 3 - 1;
			int k1 = i1 / 3 % 3 - 1;
			double d = (double)j + (double)j1 * 0.6D + 0.5D;
			double d1 = (double)k + 0.5D;
			double d2 = (double)l + (double)k1 * 0.6D + 0.5D;

			for(i2 = 0; i2 < 10; ++i2) {
				double var31 = random.nextDouble() * 0.2D + 0.01D;
				double d4 = d + (double)j1 * 0.01D + (random.nextDouble() - 0.5D) * (double)k1 * 0.5D;
				double d5 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
				double d6 = d2 + (double)k1 * 0.01D + (random.nextDouble() - 0.5D) * (double)j1 * 0.5D;
				double d7 = (double)j1 * var31 + random.nextGaussian() * 0.01D;
				double d8 = -0.03D + random.nextGaussian() * 0.01D;
				double d9 = (double)k1 * var31 + random.nextGaussian() * 0.01D;
				this.spawnParticle("smoke", d4, d5, d6, d7, d8, d9);
			}

			return;
		case 2001:
			i2 = i1 & 255;
			if(i2 > 0) {
				Block block = Block.blocksList[i2];
				this.mc.sndManager.playSound(block.stepSound.stepSoundDir(), (float)j + 0.5F, (float)k + 0.5F, (float)l + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			}

			this.mc.effectRenderer.addBlockDestroyEffects(j, k, l, i1 & 255, i1 >> 8 & 255);
		}

	}
}
