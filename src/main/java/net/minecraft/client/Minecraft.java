package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import net.minecraft.multiversion.VersionManager;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderLoadOrGenerate;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOS2;
import net.minecraft.src.EnumOSMappingHelper;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiErrorScreen;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.GuiUnused;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.MinecraftImpl;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerTest;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.SaveConverterMcRegion;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.Space.commands.CommandHandler;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.StatList;
import net.minecraft.src.StatStringFormatKeyInv;
import net.minecraft.src.Teleporter;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;
import net.minecraft.src.ThreadCheckHasPaid;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.ThreadSleepForever;
import net.minecraft.src.Timerr;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldRenderer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import net.minecraft.src.Space.Client;
import net.minecraft.src.Space.ClickGui;
import net.minecraft.src.Space.event.EventRegistry;
import net.minecraft.src.Space.event.events.EventMouseClick;
import net.minecraft.src.Space.event.events.EventHudUpdate;
import net.minecraft.src.Space.hacks.Timer;

public abstract class Minecraft implements Runnable {
	public static byte[] field_28006_b = new byte[10485760];
	private static Minecraft theMinecraft;
	public PlayerController playerController;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	public Timerr timer = new Timerr(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EntityLiving renderViewEntity;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean hideQuitButton = true;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer;
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public GuiAchievement guiAchievement = new GuiAchievement(this);
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public ModelBiped field_9242_w = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	protected MinecraftApplet mcApplet;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	private File mcDataDir;
	private ISaveFormat saveLoader;
	public static long[] frameTimes = new long[512];
	public static long[] tickTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	public static long hasPaidCheckTime = 0L;
	public StatFileWriter statFileWriter;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	public boolean inGameHasFocus = false;
	private int mouseTicksRan = 0;
	public boolean isRaining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;

	private Client client;
	private CommandHandler cmdHandler;

	public Minecraft(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag) {
		StatList.func_27360_a();
		this.tempDisplayHeight = j;
		this.fullscreen = flag;
		this.mcApplet = minecraftapplet;
		new ThreadSleepForever(this, "Timer hack thread");
		this.mcCanvas = canvas;
		this.displayWidth = i;
		this.displayHeight = j;
		this.fullscreen = flag;
		if(minecraftapplet == null || "true".equals(minecraftapplet.getParameter("stand-alone"))) {
			this.hideQuitButton = false;
		}

		theMinecraft = this;
		VersionManager.setMinecraft(this);

		client = new Client(this);
		client.loadHacks();
		cmdHandler = new CommandHandler();
		cmdHandler.loadCommands();
	}

	public void onMinecraftCrash(UnexpectedThrowable unexpectedthrowable) {
		this.hasCrashed = true;
		this.displayUnexpectedThrowable(unexpectedthrowable);
	}

	public abstract void displayUnexpectedThrowable(UnexpectedThrowable var1);

	public void setServer(String s, int i) {
		this.serverName = s;
		this.serverPort = i;
	}

	public void startGame() throws LWJGLException {
		if(this.mcCanvas != null) {
			Graphics exception = this.mcCanvas.getGraphics();
			if(exception != null) {
				exception.setColor(Color.BLACK);
				exception.fillRect(0, 0, this.displayWidth, this.displayHeight);
				exception.dispose();
			}

			Display.setParent(this.mcCanvas);
		} else if(this.fullscreen) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if(this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if(this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

		Display.setTitle("SpaceClient " + Client.currentVersion);

		try {
			PixelFormat exception1 = new PixelFormat();
			exception1 = exception1.withDepthBits(24);
			Display.create(exception1);
		} catch (LWJGLException var6) {
			var6.printStackTrace();

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException var5) {
			}

			Display.create();
		}

		this.mcDataDir = getMinecraftDir();
		this.saveLoader = new SaveConverterMcRegion(new File(this.mcDataDir, "saves"));
		this.gameSettings = new GameSettings(this, this.mcDataDir);
		this.texturePackList = new TexturePackList(this, this.mcDataDir);
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
		ColorizerWater.func_28182_a(this.renderEngine.func_28149_a("/misc/watercolor.png"));
		ColorizerGrass.func_28181_a(this.renderEngine.func_28149_a("/misc/grasscolor.png"));
		ColorizerFoliage.func_28152_a(this.renderEngine.func_28149_a("/misc/foliagecolor.png"));
		this.entityRenderer = new EntityRenderer(this);
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
		AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		this.loadScreen();
		Keyboard.create();
		Mouse.create();
		this.mouseHelper = new MouseHelper(this.mcCanvas);

		try {
			Controllers.create();
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.renderEngine.registerTextureFX(this.textureLavaFX);
		this.renderEngine.registerTextureFX(this.textureWaterFX);
		this.renderEngine.registerTextureFX(new TexturePortalFX());
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWatchFX(this));
		this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		try {
			this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
			this.downloadResourcesThread.start();
		} catch (Exception var3) {
		}

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
		} else {
			this.displayGuiScreen(new GuiMainMenu());
		}

	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution scaledresolution = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.field_25121_a, scaledresolution.field_25120_b, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(16777215);
		tessellator.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		short c = 256;
		short c1 = 256;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.setColorOpaque_I(16777215);
		this.func_6274_a((scaledresolution.getScaledWidth() - c) / 2, (scaledresolution.getScaledHeight() - c1) / 2, 0, 0, c, c1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		Display.swapBuffers();
	}

	public void func_6274_a(int i, int j, int k, int l, int i1, int j1) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(i + 0), (double)(j + j1), 0.0D, (double)((float)(k + 0) * f), (double)((float)(l + j1) * f1));
		tessellator.addVertexWithUV((double)(i + i1), (double)(j + j1), 0.0D, (double)((float)(k + i1) * f), (double)((float)(l + j1) * f1));
		tessellator.addVertexWithUV((double)(i + i1), (double)(j + 0), 0.0D, (double)((float)(k + i1) * f), (double)((float)(l + 0) * f1));
		tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), 0.0D, (double)((float)(k + 0) * f), (double)((float)(l + 0) * f1));
		tessellator.draw();
	}

	public static File getMinecraftDir() {
		if(minecraftDir == null) {
			minecraftDir = getAppDir("minecraft");
		}

		return minecraftDir;
	}

	public static File getAppDir(String s) {
		String s1 = System.getProperty("user.home", ".");
		File file;
		switch(EnumOSMappingHelper.enumOSMappingArray[getOs().ordinal()]) {
		case 1:
		case 2:
			file = new File(s1, '.' + s + '/');
			break;
		case 3:
			String s2 = System.getenv("APPDATA");
			if(s2 != null) {
				file = new File(s2, "." + s + '/');
			} else {
				file = new File(s1, '.' + s + '/');
			}
			break;
		case 4:
			file = new File(s1, "Library/Application Support/" + s);
			break;
		default:
			file = new File(s1, s + '/');
		}

		if(!file.exists() && !file.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + file);
		} else {
			return file;
		}
	}

	private static EnumOS2 getOs() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? EnumOS2.windows : (s.contains("mac") ? EnumOS2.macos : (s.contains("solaris") ? EnumOS2.solaris : (s.contains("sunos") ? EnumOS2.solaris : (s.contains("linux") ? EnumOS2.linux : (s.contains("unix") ? EnumOS2.linux : EnumOS2.unknown)))));
	}

	public ISaveFormat getSaveLoader() {
		return this.saveLoader;
	}

	public void displayGuiScreen(GuiScreen guiscreen) {
		if(!(this.currentScreen instanceof GuiUnused)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(guiscreen instanceof GuiMainMenu) {
				this.statFileWriter.func_27175_b();
			}

			this.statFileWriter.syncStats();
			if(guiscreen == null && this.theWorld == null) {
				guiscreen = new GuiMainMenu();
			} else if(guiscreen == null && this.thePlayer.health <= 0) {
				guiscreen = new GuiGameOver();
			}

			if(guiscreen instanceof GuiMainMenu) {
				this.ingameGUI.clearChatMessages();
			}

			this.currentScreen = (GuiScreen)guiscreen;
			if(guiscreen != null) {
				this.setIngameNotInFocus();
				ScaledResolution scaledresolution = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
				int i = scaledresolution.getScaledWidth();
				int j = scaledresolution.getScaledHeight();
				((GuiScreen)guiscreen).setWorldAndResolution(this, i, j);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}

		}
	}

	private void checkGLError(String s) {
		int i = GL11.glGetError();
		if(i != 0) {
			String s1 = GLU.gluErrorString(i);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + s);
			System.out.println(i + ": " + s1);
		}

	}

	public void shutdownMinecraftApplet() {
		try {
			this.statFileWriter.func_27175_b();
			this.statFileWriter.syncStats();
			if(this.mcApplet != null) {
				this.mcApplet.clearApplet();
			}

			try {
				if(this.downloadResourcesThread != null) {
					this.downloadResourcesThread.closeMinecraft();
				}
			} catch (Exception var9) {
			}

			System.out.println("Stopping!");

			try {
				this.changeWorld1((World)null);
			} catch (Throwable var8) {
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable var7) {
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Client.saveHacks();
			Display.destroy();
			if(!this.hasCrashed) {
				System.exit(0);
			}

		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception var17) {
			var17.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Failed to start game", var17));
			return;
		}

		try {
			long throwable = System.currentTimeMillis();
			int i = 0;

			while(this.running) {
				try {
					if(this.mcApplet != null && !this.mcApplet.isActive()) {
						break;
					}

					AxisAlignedBB.clearBoundingBoxPool();
					Vec3D.initialize();
					if(this.mcCanvas == null && Display.isCloseRequested()) {
						this.shutdown();
					}

					if(this.isGamePaused && this.theWorld != null) {
						float outofmemoryerror = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = outofmemoryerror;
					} else {
						this.timer.updateTimer();
					}

					long var23 = System.nanoTime();

					for(int l2 = 0; l2 < this.timer.elapsedTicks; ++l2) {
						++this.ticksRan;

						try {
							this.runTick();
						} catch (MinecraftException var16) {
							this.theWorld = null;
							this.changeWorld1((World)null);
							this.displayGuiScreen(new GuiConflictWarning());
						}
					}

					long var24 = System.nanoTime() - var23;
					this.checkGLError("Pre render");
					this.sndManager.func_338_a(this.thePlayer, this.timer.renderPartialTicks);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					if(this.theWorld != null) {
						this.theWorld.updatingLighting();
					}

					if(!Keyboard.isKeyDown(Keyboard.KEY_F7)) {
						Display.update();
					}

					if(this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
						this.gameSettings.thirdPersonView = false;
					}

					if(!this.skipRenderWorld) {
						if(this.playerController != null) {
							this.playerController.setPartialTime(this.timer.renderPartialTicks);
						}

						this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
					}

					if(!Display.isActive()) {
						if(this.fullscreen) {
							this.toggleFullscreen();
						}

						Thread.sleep(10L);
					}

					if(!this.gameSettings.showDebugInfo) {
						this.prevFrameTime = System.nanoTime();
					}

					this.guiAchievement.updateAchievementWindow();
					Thread.yield();
					if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
						Display.update();
					}

					this.screenshotListener();
					if(this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
						this.displayWidth = this.mcCanvas.getWidth();
						this.displayHeight = this.mcCanvas.getHeight();
						if(this.displayWidth <= 0) {
							this.displayWidth = 1;
						}

						if(this.displayHeight <= 0) {
							this.displayHeight = 1;
						}

						this.resize(this.displayWidth, this.displayHeight);
					}

					this.checkGLError("Post render");
					++i;

					for(this.isGamePaused = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= throwable + 1000L; i = 0) {
						this.debug = i + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
						WorldRenderer.chunksUpdated = 0;
						throwable += 1000L;
					}
				} catch (MinecraftException var18) {
					this.theWorld = null;
					this.changeWorld1((World)null);
					this.displayGuiScreen(new GuiConflictWarning());
				} catch (OutOfMemoryError var19) {
					this.func_28002_e();
					this.displayGuiScreen(new GuiErrorScreen());
					System.gc();
				}
			}
		} catch (MinecraftError var20) {
		} catch (Throwable var21) {
			this.func_28002_e();
			var21.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Unexpected error", var21));
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	public void func_28002_e() {
		try {
			field_28006_b = new byte[0];
			this.renderGlobal.func_28137_f();
		} catch (Throwable var4) {
		}

		try {
			System.gc();
			AxisAlignedBB.func_28196_a();
			Vec3D.func_28215_a();
		} catch (Throwable var3) {
		}

		try {
			System.gc();
			this.changeWorld1((World)null);
		} catch (Throwable var2) {
		}

		System.gc();
	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
				this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
			}
		} else {
			this.isTakingScreenshot = false;
		}

	}

	private void displayDebugInfo(long l) {
		long l1 = 16666666L;
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = System.nanoTime();
		}

		long l2 = System.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = l;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = l2 - this.prevFrameTime;
		this.prevFrameTime = l2;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(7);
		int i = (int)(l1 / 200000L);
		tessellator.setColorOpaque_I(536870912);
		tessellator.addVertex(0.0D, (double)(this.displayHeight - i), 0.0D);
		tessellator.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator.addVertex((double)frameTimes.length, (double)(this.displayHeight - i), 0.0D);
		tessellator.setColorOpaque_I(538968064);
		tessellator.addVertex(0.0D, (double)(this.displayHeight - i * 2), 0.0D);
		tessellator.addVertex(0.0D, (double)(this.displayHeight - i), 0.0D);
		tessellator.addVertex((double)frameTimes.length, (double)(this.displayHeight - i), 0.0D);
		tessellator.addVertex((double)frameTimes.length, (double)(this.displayHeight - i * 2), 0.0D);
		tessellator.draw();
		long l3 = 0L;

		int k;
		for(k = 0; k < frameTimes.length; ++k) {
			l3 += frameTimes[k];
		}

		k = (int)(l3 / 200000L / (long)frameTimes.length);
		tessellator.startDrawing(7);
		tessellator.setColorOpaque_I(541065216);
		tessellator.addVertex(0.0D, (double)(this.displayHeight - k), 0.0D);
		tessellator.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator.addVertex((double)frameTimes.length, (double)(this.displayHeight - k), 0.0D);
		tessellator.draw();
		tessellator.startDrawing(1);

		for(int i1 = 0; i1 < frameTimes.length; ++i1) {
			int j1 = (i1 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			int k1 = j1 * j1 / 255;
			k1 = k1 * k1 / 255;
			int i2 = k1 * k1 / 255;
			i2 = i2 * i2 / 255;
			if(frameTimes[i1] > l1) {
				tessellator.setColorOpaque_I(-16777216 + k1 * 65536);
			} else {
				tessellator.setColorOpaque_I(-16777216 + k1 * 256);
			}

			long l4 = frameTimes[i1] / 200000L;
			long l5 = tickTimes[i1] / 200000L;
			tessellator.addVertex((double)((float)i1 + 0.5F), (double)((float)((long)this.displayHeight - l4) + 0.5F), 0.0D);
			tessellator.addVertex((double)((float)i1 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
			tessellator.setColorOpaque_I(-16777216 + k1 * 65536 + k1 * 256 + k1 * 1);
			tessellator.addVertex((double)((float)i1 + 0.5F), (double)((float)((long)this.displayHeight - l4) + 0.5F), 0.0D);
			tessellator.addVertex((double)((float)i1 + 0.5F), (double)((float)((long)this.displayHeight - (l4 - l5)) + 0.5F), 0.0D);
		}

		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void shutdown() {
		this.running = false;
	}

	public void setIngameFocus() {
		if(Display.isActive()) {
			if(!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen)null);
				this.leftClickCounter = 10000;
				this.mouseTicksRan = this.ticksRan + 10000;
			}
		}
	}

	public void setIngameNotInFocus() {
		if(this.inGameHasFocus) {
			if(this.thePlayer != null) {
				this.thePlayer.resetPlayerKeyState();
			}

			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void func_6254_a(int i, boolean flag) {
		if(!this.playerController.field_1064_b) {
			if(!flag) {
				this.leftClickCounter = 0;
			}

			if(i != 0 || this.leftClickCounter <= 0) {
				if(flag && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && i == 0) {
					int j = this.objectMouseOver.blockX;
					int k = this.objectMouseOver.blockY;
					int l = this.objectMouseOver.blockZ;
					this.playerController.sendBlockRemoving(j, k, l, this.objectMouseOver.sideHit);
					this.effectRenderer.addBlockHitEffects(j, k, l, this.objectMouseOver.sideHit);
				} else {
					this.playerController.resetBlockRemoving();
				}

			}
		}
	}

	private void clickMouse(int i) {
		if(i != 0 || this.leftClickCounter <= 0) {
			if(i == 0) {
				this.thePlayer.swingItem();
			}

			boolean flag = true;
			if(this.objectMouseOver == null) {
				if(i == 0 && !(this.playerController instanceof PlayerControllerTest)) {
					this.leftClickCounter = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if(i == 0) {
					this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(i == 1) {
					this.playerController.interactWithEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int itemstack = this.objectMouseOver.blockX;
				int k = this.objectMouseOver.blockY;
				int l = this.objectMouseOver.blockZ;
				int i1 = this.objectMouseOver.sideHit;
				if(i == 0) {
					this.playerController.clickBlock(itemstack, k, l, this.objectMouseOver.sideHit);
					EventMouseClick e = new EventMouseClick(itemstack, k, l);
					EventRegistry.handleEvent(e);
				} else {
					ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();
					int j1 = itemstack1 == null ? 0 : itemstack1.stackSize;
					if(this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, itemstack1, itemstack, k, l, i1)) {
						flag = false;
						this.thePlayer.swingItem();
					}

					if(itemstack1 == null) {
						return;
					}

					if(itemstack1.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(itemstack1.stackSize != j1) {
						this.entityRenderer.itemRenderer.func_9449_b();
					}
				}
			}

			if(flag && i == 1) {
				ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();
				if(itemstack1 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemstack1)) {
					this.entityRenderer.itemRenderer.func_9450_c();
				}
			}

		}
	}

	public void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			if(this.fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			} else {
				if(this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			}

			if(this.currentScreen != null) {
				this.resize(this.displayWidth, this.displayHeight);
			}

			Display.setFullscreen(this.fullscreen);
			Display.update();
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}

	private void resize(int i, int j) {
		if(i <= 0) {
			i = 1;
		}

		if(j <= 0) {
			j = 1;
		}

		this.displayWidth = i;
		this.displayHeight = j;
		if(this.currentScreen != null) {
			ScaledResolution scaledresolution = new ScaledResolution(this.gameSettings, i, j);
			int k = scaledresolution.getScaledWidth();
			int l = scaledresolution.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, k, l);
		}

	}

	private void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			int i = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(i == Block.grass.blockID) {
				i = Block.dirt.blockID;
			}

			if(i == Block.stairDouble.blockID) {
				i = Block.stairSingle.blockID;
			}

			if(i == Block.bedrock.blockID) {
				i = Block.stone.blockID;
			}

			this.thePlayer.inventory.setCurrentItem(i, this.playerController instanceof PlayerControllerTest);
		}

	}

	private void func_28001_B() {
		(new ThreadCheckHasPaid(this)).start();
	}

	public void runTick() {
		if(this.ticksRan == 6000) {
			this.func_28001_B();
		}

		this.statFileWriter.func_27178_d();
		this.ingameGUI.updateTick();
		this.entityRenderer.getMouseOver(1.0F);
		int k;
		if(this.thePlayer != null) {
			IChunkProvider i = this.theWorld.getIChunkProvider();
			if(i instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)i;
				k = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int i1 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				chunkproviderloadorgenerate.setCurrentChunkOver(k, i1);
			}
		}

		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.updateController();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture(VersionManager.version().textures() + "/terrain.png"));
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null) {
			if(this.thePlayer.health <= 0) {
				this.displayGuiScreen((GuiScreen)null);
			} else if(this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.multiplayerWorld) {
				this.displayGuiScreen(new GuiSleepMP());
			}
		} else if(this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen != null) {
			this.leftClickCounter = 10000;
			this.mouseTicksRan = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.field_25091_h.func_25088_a();
				this.currentScreen.updateScreen();
			}
		}

		if(this.currentScreen == null || this.currentScreen.field_948_f) {
			label313:
			while(true) {
				while(true) {
					while(true) {
						long var5;
						do {
							if(!Mouse.next()) {
								if(this.leftClickCounter > 0) {
									--this.leftClickCounter;
								}

								while(true) {
									while(true) {
										do {
											if(!Keyboard.next()) {
												if(this.currentScreen == null) {
													if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
														this.clickMouse(0);
														this.mouseTicksRan = this.ticksRan;
													}

													if(Mouse.isButtonDown(1) && (float)(this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
														this.clickMouse(1);
														this.mouseTicksRan = this.ticksRan;
													}
												}

												this.func_6254_a(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.inGameHasFocus);
												break label313;
											}

											this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
										} while(!Keyboard.getEventKeyState());

										if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
											this.toggleFullscreen();
										} else {
											if(this.currentScreen != null) {
												this.currentScreen.handleKeyboardInput();
											} else {
												if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
													this.displayInGameMenu();
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
													this.forceReload();
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F1) {
													this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F3) {
													this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
													this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F8) {
													this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
												}

												if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
													this.displayGuiScreen(new GuiInventory(this.thePlayer));
												}

												if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
													this.thePlayer.dropCurrentItem();
												}

												if(this.isMultiplayerWorld() && Keyboard.getEventKey() == this.gameSettings.keyBindChat.keyCode) {
													this.displayGuiScreen(new GuiChat());
												}

												if(this.gameSettings.hideGUI && Keyboard.getEventKey() == Keyboard.KEY_TAB) {
													this.entityRenderer.hideHandWithGUI = !this.entityRenderer.hideHandWithGUI;
												}
											}

											for(int var6 = 0; var6 < 9; ++var6) {
												if(Keyboard.getEventKey() == Keyboard.KEY_1 + var6) {
													this.thePlayer.inventory.currentItem = var6;
												}
											}

											if(Keyboard.getEventKey() == this.gameSettings.keyBindToggleFog.keyCode) {
												this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
											}
											if(Keyboard.getEventKey() == Keyboard.KEY_RSHIFT){
													this.displayGuiScreen(new ClickGui());
												}
											for(int i = 0; i < Client.hackList.size(); ++i){
												if(Keyboard.getEventKey() == Client.hacks[i].key) {
													Client.hacks[i].toggle();
													Client.hacks[i].onToggle();
												}
											}
										}
									}
								}
							}

							var5 = System.currentTimeMillis() - this.systemTime;
						} while(var5 > 200L);

						k = Mouse.getEventDWheel();
						if(k != 0) {
							this.thePlayer.inventory.changeCurrentItem(k);
							if(this.gameSettings.field_22275_C) {
								if(k > 0) {
									k = 1;
								}

								if(k < 0) {
									k = -1;
								}

								this.gameSettings.field_22272_F += (float)k * 0.25F;
							}
						}

						if(this.currentScreen == null) {
							if(!this.inGameHasFocus && Mouse.getEventButtonState()) {
								this.setIngameFocus();
							} else {
								if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
									this.clickMouse(0);
									this.mouseTicksRan = this.ticksRan;
								}

								if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
									this.clickMouse(1);
									this.mouseTicksRan = this.ticksRan;
								}

								if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
									this.clickMiddleMouseButton();
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}
				}
			}
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.joinPlayerCounter;
				if(this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(this.theWorld.multiplayerWorld) {
				this.theWorld.difficultySetting = 3;
			}

			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			if(!this.isGamePaused) {
				if(this.theWorld.field_27172_i > 0) {
					--this.theWorld.field_27172_i;
				}

				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused || this.isMultiplayerWorld()) {
				this.theWorld.setAllowedMobSpawns(this.gameSettings.difficulty > 0, true);
				this.theWorld.tick();
			}

			if(!this.isGamePaused && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

		this.systemTime = System.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.downloadResourcesThread.reloadResources();
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.multiplayerWorld;
	}

	public void startWorld(String s, String s1, long l) {
		this.changeWorld1((World)null);
		System.gc();
		if(this.saveLoader.isOldMapFormat(s)) {
			this.convertMapFormat(s, s1);
		} else {
			ISaveHandler isavehandler = this.saveLoader.getSaveLoader(s, false);
			World world = null;
			world = new World(isavehandler, s1, l);
			if(world.isNewWorld) {
				this.statFileWriter.readStat(StatList.createWorldStat, 1);
				this.statFileWriter.readStat(StatList.startGameStat, 1);
				this.changeWorld2(world, "Generating level");
			} else {
				this.statFileWriter.readStat(StatList.loadWorldStat, 1);
				this.statFileWriter.readStat(StatList.startGameStat, 1);
				this.changeWorld2(world, "Loading level");
			}
		}

	}

	public void usePortal() {
		System.out.println("Toggling dimension!!");
		if(this.thePlayer.dimension == -1) {
			this.thePlayer.dimension = 0;
		} else {
			this.thePlayer.dimension = -1;
		}

		this.theWorld.setEntityDead(this.thePlayer);
		this.thePlayer.isDead = false;
		double d = this.thePlayer.posX;
		double d1 = this.thePlayer.posZ;
		double d2 = 8.0D;
		World world1;
		if(this.thePlayer.dimension == -1) {
			d /= d2;
			d1 /= d2;
			this.thePlayer.setLocationAndAngles(d, this.thePlayer.posY, d1, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			if(this.thePlayer.isEntityAlive()) {
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			world1 = null;
			world1 = new World(this.theWorld, WorldProvider.getProviderForDimension(-1));
			this.changeWorld(world1, "Entering the Nether", this.thePlayer);
		} else {
			d *= d2;
			d1 *= d2;
			this.thePlayer.setLocationAndAngles(d, this.thePlayer.posY, d1, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			if(this.thePlayer.isEntityAlive()) {
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			world1 = null;
			world1 = new World(this.theWorld, WorldProvider.getProviderForDimension(0));
			this.changeWorld(world1, "Leaving the Nether", this.thePlayer);
		}

		this.thePlayer.worldObj = this.theWorld;
		if(this.thePlayer.isEntityAlive()) {
			this.thePlayer.setLocationAndAngles(d, this.thePlayer.posY, d1, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			(new Teleporter()).func_4107_a(this.theWorld, this.thePlayer);
		}

	}

	public void changeWorld1(World world) {
		this.changeWorld2(world, "");
	}

	public void changeWorld2(World world, String s) {
		this.changeWorld(world, s, (EntityPlayer)null);
	}

	public void changeWorld(World world, String s, EntityPlayer entityplayer) {
		this.statFileWriter.func_27175_b();
		this.statFileWriter.syncStats();
		this.renderViewEntity = null;
		this.loadingScreen.printText(s);
		this.loadingScreen.displayLoadingString("");
		this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.saveWorldIndirectly(this.loadingScreen);
		}

		this.theWorld = world;
		if(world != null) {
			this.playerController.func_717_a(world);
			if(!this.isMultiplayerWorld()) {
				if(entityplayer == null) {
					this.thePlayer = (EntityPlayerSP)world.func_4085_a(EntityPlayerSP.class);
				}
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(world != null) {
					world.entityJoinedWorld(this.thePlayer);
				}
			}

			if(!world.multiplayerWorld) {
				this.func_6255_d(s);
			}

			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(world);
				this.thePlayer.preparePlayerToSpawn();
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(world);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(world);
			}

			this.playerController.func_6473_b(this.thePlayer);
			if(entityplayer != null) {
				world.emptyMethod1();
			}

			IChunkProvider ichunkprovider = world.getIChunkProvider();
			if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
				int i = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int j = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
			}

			world.spawnPlayerWithLoadedChunks(this.thePlayer);
			if(world.isNewWorld) {
				world.saveWorldIndirectly(this.loadingScreen);
			}

			this.renderViewEntity = this.thePlayer;
		} else {
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void convertMapFormat(String s, String s1) {
		this.loadingScreen.printText("Converting World to " + this.saveLoader.func_22178_a());
		this.loadingScreen.displayLoadingString("This may take a while :)");
		this.saveLoader.convertMapFormat(s, this.loadingScreen);
		this.startWorld(s, s1, 0L);
	}

	private void func_6255_d(String s) {
		this.loadingScreen.printText(s);
		this.loadingScreen.displayLoadingString("Building terrain");
		short c = 128;
		int i = 0;
		int j = c * 2 / 16 + 1;
		j *= j;
		IChunkProvider ichunkprovider = this.theWorld.getIChunkProvider();
		ChunkCoordinates chunkcoordinates = this.theWorld.getSpawnPoint();
		if(this.thePlayer != null) {
			chunkcoordinates.x = (int)this.thePlayer.posX;
			chunkcoordinates.z = (int)this.thePlayer.posZ;
		}

		if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate k = (ChunkProviderLoadOrGenerate)ichunkprovider;
			k.setCurrentChunkOver(chunkcoordinates.x >> 4, chunkcoordinates.z >> 4);
		}

		for(int var10 = -c; var10 <= c; var10 += 16) {
			for(int l = -c; l <= c; l += 16) {
				this.loadingScreen.setLoadingProgress(i++ * 100 / j);
				this.theWorld.getBlockId(chunkcoordinates.x + var10, 64, chunkcoordinates.z + l);

				while(this.theWorld.updatingLighting()) {
				}
			}
		}

		this.loadingScreen.displayLoadingString("Simulating world for a bit");
		boolean var9 = true;
		this.theWorld.func_656_j();
	}

	public void installResource(String s, File file) {
		int i = s.indexOf("/");
		String s1 = s.substring(0, i);
		s = s.substring(i + 1);
		if(s1.equalsIgnoreCase("sound")) {
			this.sndManager.addSound(s, file);
		} else if(s1.equalsIgnoreCase("newsound")) {
			this.sndManager.addSound(s, file);
		} else if(s1.equalsIgnoreCase("streaming")) {
			this.sndManager.addStreaming(s, file);
		} else if(s1.equalsIgnoreCase("music")) {
			this.sndManager.addMusic(s, file);
		} else if(s1.equalsIgnoreCase("newmusic")) {
			this.sndManager.addMusic(s, file);
		}

	}

	public OpenGlCapsChecker getOpenGlCapsChecker() {
		return this.glCapabilities;
	}

	public String func_6241_m() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	public String func_6262_n() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	public String func_21002_o() {
		return this.theWorld.func_21119_g();
	}

	public String func_6245_o() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.func_687_d();
	}

	public void respawn(boolean flag, int i) {
		if(!this.theWorld.multiplayerWorld && !this.theWorld.worldProvider.canRespawnHere()) {
			this.usePortal();
		}

		ChunkCoordinates chunkcoordinates = null;
		ChunkCoordinates chunkcoordinates1 = null;
		boolean flag1 = true;
		if(this.thePlayer != null && !flag) {
			chunkcoordinates = this.thePlayer.getPlayerSpawnCoordinate();
			if(chunkcoordinates != null) {
				chunkcoordinates1 = EntityPlayer.func_25060_a(this.theWorld, chunkcoordinates);
				if(chunkcoordinates1 == null) {
					this.thePlayer.addChatMessage("tile.bed.notValid");
				}
			}
		}

		if(chunkcoordinates1 == null) {
			chunkcoordinates1 = this.theWorld.getSpawnPoint();
			flag1 = false;
		}

		IChunkProvider ichunkprovider = this.theWorld.getIChunkProvider();
		if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate j = (ChunkProviderLoadOrGenerate)ichunkprovider;
			j.setCurrentChunkOver(chunkcoordinates1.x >> 4, chunkcoordinates1.z >> 4);
		}

		this.theWorld.setSpawnLocation();
		this.theWorld.updateEntityList();
		int j1 = 0;
		if(this.thePlayer != null) {
			j1 = this.thePlayer.entityId;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		this.renderViewEntity = null;
		this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(this.theWorld);
		this.thePlayer.dimension = i;
		this.renderViewEntity = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		if(flag1) {
			this.thePlayer.setPlayerSpawnCoordinate(chunkcoordinates);
			this.thePlayer.setLocationAndAngles((double)((float)chunkcoordinates1.x + 0.5F), (double)((float)chunkcoordinates1.y + 0.1F), (double)((float)chunkcoordinates1.z + 0.5F), 0.0F, 0.0F);
		}

		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.entityId = j1;
		this.thePlayer.func_6420_o();
		this.playerController.func_6473_b(this.thePlayer);
		this.func_6255_d("Respawning");
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

	public static void func_6269_a(String s, String s1) {
		startMainThread(s, s1, (String)null);
	}

	public static void startMainThread(String s, String s1, String s2) {
		boolean flag = false;
		Frame frame = new Frame("Minecraft");
		Canvas canvas = new Canvas();
		frame.setLayout(new BorderLayout());
		frame.add(canvas, "Center");
		canvas.setPreferredSize(new Dimension(854, 480));
		frame.pack();
		frame.setLocationRelativeTo((Component)null);
		MinecraftImpl minecraftimpl = new MinecraftImpl(frame, canvas, (MinecraftApplet)null, 854, 480, flag, frame);
		Thread thread = new Thread(minecraftimpl, "Minecraft main thread");
		thread.setPriority(10);
		minecraftimpl.minecraftUri = "www.minecraft.net";
		if(s != null && s1 != null) {
			minecraftimpl.session = new Session(s, s1);
		} else {
			minecraftimpl.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");
		}

		if(s2 != null) {
			String[] as = s2.split(":");
			minecraftimpl.setServer(as[0], Integer.parseInt(as[1]));
		}

		frame.setVisible(true);
		frame.addWindowListener(new GameWindowListener(minecraftimpl, thread));
		thread.start();
	}

	public NetClientHandler getSendQueue() {
		return this.thePlayer instanceof EntityClientPlayerMP ? ((EntityClientPlayerMP)this.thePlayer).sendQueue : null;
	}

	public static void main(String[] args) {
		String s = null;
		String s1 = null;
		s = "Player" + System.currentTimeMillis() % 1000L;
		if(args.length > 0) {
			s = args[0];
		}

		s1 = "-";
		if(args.length > 1) {
			s1 = args[1];
		}

		func_6269_a(s, s1);
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion;
	}

	public static boolean isDebugInfoEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.showDebugInfo;
	}

	public boolean lineIsCommand(String s) {
		if(!s.startsWith("/")) {
		}

		return false;
	}
}
