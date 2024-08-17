package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererPiston extends TileEntitySpecialRenderer {
	private RenderBlocks field_31071_b;

	public void func_31070_a(TileEntityPiston tileentitypiston, double d, double d1, double d2, float f) {
		Block block = Block.blocksList[tileentitypiston.getStoredBlockID()];
		if(block != null && tileentitypiston.func_31008_a(f) < 1.0F) {
			Tessellator tessellator = Tessellator.instance;
			this.bindTextureByName(VersionManager.version().textures() + "/terrain.png");
			RenderHelper.disableStandardItemLighting();
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			if(Minecraft.isAmbientOcclusionEnabled()) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			} else {
				GL11.glShadeModel(GL11.GL_FLAT);
			}

			tessellator.startDrawingQuads();
			tessellator.setTranslationD((double)((float)d - (float)tileentitypiston.xCoord + tileentitypiston.func_31017_b(f)), (double)((float)d1 - (float)tileentitypiston.yCoord + tileentitypiston.func_31014_c(f)), (double)((float)d2 - (float)tileentitypiston.zCoord + tileentitypiston.func_31013_d(f)));
			tessellator.setColorOpaque(1, 1, 1);
			if(block == Block.pistonExtension && tileentitypiston.func_31008_a(f) < 0.5F) {
				this.field_31071_b.func_31079_a(block, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord, false);
			} else if(tileentitypiston.func_31012_k() && !tileentitypiston.func_31015_b()) {
				Block.pistonExtension.func_31052_a_(((BlockPistonBase)block).func_31040_i());
				this.field_31071_b.func_31079_a(Block.pistonExtension, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord, tileentitypiston.func_31008_a(f) < 0.5F);
				Block.pistonExtension.func_31051_a();
				tessellator.setTranslationD((double)((float)d - (float)tileentitypiston.xCoord), (double)((float)d1 - (float)tileentitypiston.yCoord), (double)((float)d2 - (float)tileentitypiston.zCoord));
				this.field_31071_b.func_31078_d(block, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord);
			} else {
				this.field_31071_b.func_31075_a(block, tileentitypiston.xCoord, tileentitypiston.yCoord, tileentitypiston.zCoord);
			}

			tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			RenderHelper.enableStandardItemLighting();
		}

	}

	public void func_31069_a(World world) {
		this.field_31071_b = new RenderBlocks(world);
	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		this.func_31070_a((TileEntityPiston)tileentity, d, d1, d2, f);
	}
}
