package net.minecraft.src;

import net.minecraft.multiversion.VersionManager;
import org.lwjgl.opengl.GL11;

public class TextureFX {
	public byte[] imageData = new byte[1024];
	public int iconIndex;
	public boolean anaglyphEnabled = false;
	public int textureId = 0;
	public int tileSize = 1;
	public int tileImage = 0;

	public TextureFX(int i) {
		this.iconIndex = i;
	}

	public void onTick() {
	}

	public void bindImage(RenderEngine renderengine) {
		if(this.tileImage == 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderengine.getTexture(VersionManager.version().textures() + "/terrain.png"));
		} else if(this.tileImage == 1) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderengine.getTexture("/gui/items.png"));
		}

	}
}
