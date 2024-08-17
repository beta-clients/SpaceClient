package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import net.minecraft.multiversion.VersionManager;

public class TerrainTextureManager {
	private float[] field_1181_a = new float[768];
	private int[] field_1180_b = new int[5120];
	private int[] field_1186_c = new int[5120];
	private int[] field_1185_d = new int[5120];
	private int[] field_1184_e = new int[5120];
	private int[] field_1183_f = new int[34];
	private int[] field_1182_g = new int[768];

	public TerrainTextureManager() {
		try {
			BufferedImage i = ImageIO.read(TerrainTextureManager.class.getResource(VersionManager.version().textures() + "/terrain.png"));
			int[] ai = new int[65536];
			i.getRGB(0, 0, 256, 256, ai, 0, 256);

			for(int j = 0; j < 256; ++j) {
				int k = 0;
				int l = 0;
				int i1 = 0;
				int j1 = j % 16 * 16;
				int k1 = j / 16 * 16;
				int l1 = 0;

				for(int i2 = 0; i2 < 16; ++i2) {
					for(int j2 = 0; j2 < 16; ++j2) {
						int k2 = ai[j2 + j1 + (i2 + k1) * 256];
						int l2 = k2 >> 24 & 255;
						if(l2 > 128) {
							k += k2 >> 16 & 255;
							l += k2 >> 8 & 255;
							i1 += k2 & 255;
							++l1;
						}
					}

					if(l1 == 0) {
						++l1;
					}

					this.field_1181_a[j * 3 + 0] = (float)(k / l1);
					this.field_1181_a[j * 3 + 1] = (float)(l / l1);
					this.field_1181_a[j * 3 + 2] = (float)(i1 / l1);
				}
			}
		} catch (IOException var14) {
			var14.printStackTrace();
		}

		for(int var15 = 0; var15 < 256; ++var15) {
			if(Block.blocksList[var15] != null) {
				this.field_1182_g[var15 * 3 + 0] = Block.blocksList[var15].getBlockTextureFromSide(1);
				this.field_1182_g[var15 * 3 + 1] = Block.blocksList[var15].getBlockTextureFromSide(2);
				this.field_1182_g[var15 * 3 + 2] = Block.blocksList[var15].getBlockTextureFromSide(3);
			}
		}

	}

	public void func_799_a(IsoImageBuffer isoimagebuffer) {
		World world = isoimagebuffer.worldObj;
		if(world == null) {
			isoimagebuffer.field_1351_f = true;
			isoimagebuffer.field_1352_e = true;
		} else {
			int i = isoimagebuffer.field_1354_c * 16;
			int j = isoimagebuffer.field_1353_d * 16;
			int k = i + 16;
			int l = j + 16;
			Chunk chunk = world.getChunkFromChunkCoords(isoimagebuffer.field_1354_c, isoimagebuffer.field_1353_d);
			if(chunk.func_21167_h()) {
				isoimagebuffer.field_1351_f = true;
				isoimagebuffer.field_1352_e = true;
			} else {
				isoimagebuffer.field_1351_f = false;
				Arrays.fill(this.field_1186_c, 0);
				Arrays.fill(this.field_1185_d, 0);
				Arrays.fill(this.field_1183_f, 160);

				for(int i1 = l - 1; i1 >= j; --i1) {
					for(int j1 = k - 1; j1 >= i; --j1) {
						int k1 = j1 - i;
						int l1 = i1 - j;
						int i2 = k1 + l1;
						boolean flag = true;

						for(int j2 = 0; j2 < 128; ++j2) {
							int k2 = l1 - k1 - j2 + 160 - 16;
							if(k2 < this.field_1183_f[i2] || k2 < this.field_1183_f[i2 + 1]) {
								Block block = Block.blocksList[world.getBlockId(j1, j2, i1)];
								if(block == null) {
									flag = false;
								} else if(block.blockMaterial == Material.water) {
									int var24 = world.getBlockId(j1, j2 + 1, i1);
									if(var24 == 0 || Block.blocksList[var24].blockMaterial != Material.water) {
										float var25 = (float)j2 / 127.0F * 0.6F + 0.4F;
										float var26 = world.getLightBrightness(j1, j2 + 1, i1) * var25;
										if(k2 >= 0 && k2 < 160) {
											int var27 = i2 + k2 * 32;
											if(i2 >= 0 && i2 <= 32 && this.field_1185_d[var27] <= j2) {
												this.field_1185_d[var27] = j2;
												this.field_1184_e[var27] = (int)(var26 * 127.0F);
											}

											if(i2 >= -1 && i2 <= 31 && this.field_1185_d[var27 + 1] <= j2) {
												this.field_1185_d[var27 + 1] = j2;
												this.field_1184_e[var27 + 1] = (int)(var26 * 127.0F);
											}

											flag = false;
										}
									}
								} else {
									if(flag) {
										if(k2 < this.field_1183_f[i2]) {
											this.field_1183_f[i2] = k2;
										}

										if(k2 < this.field_1183_f[i2 + 1]) {
											this.field_1183_f[i2 + 1] = k2;
										}
									}

									float f = (float)j2 / 127.0F * 0.6F + 0.4F;
									int j3;
									int l3;
									float f4;
									float f7;
									if(k2 >= 0 && k2 < 160) {
										j3 = i2 + k2 * 32;
										l3 = this.field_1182_g[block.blockID * 3 + 0];
										f4 = (world.getLightBrightness(j1, j2 + 1, i1) * 0.8F + 0.2F) * f;
										if(i2 >= 0 && this.field_1186_c[j3] <= j2) {
											this.field_1186_c[j3] = j2;
											this.field_1180_b[j3] = -16777216 | (int)(this.field_1181_a[l3 * 3 + 0] * f4) << 16 | (int)(this.field_1181_a[l3 * 3 + 1] * f4) << 8 | (int)(this.field_1181_a[l3 * 3 + 2] * f4);
										}

										if(i2 < 31) {
											f7 = f4 * 0.9F;
											if(this.field_1186_c[j3 + 1] <= j2) {
												this.field_1186_c[j3 + 1] = j2;
												this.field_1180_b[j3 + 1] = -16777216 | (int)(this.field_1181_a[l3 * 3 + 0] * f7) << 16 | (int)(this.field_1181_a[l3 * 3 + 1] * f7) << 8 | (int)(this.field_1181_a[l3 * 3 + 2] * f7);
											}
										}
									}

									if(k2 >= -1 && k2 < 159) {
										j3 = i2 + (k2 + 1) * 32;
										l3 = this.field_1182_g[block.blockID * 3 + 1];
										f4 = world.getLightBrightness(j1 - 1, j2, i1) * 0.8F + 0.2F;
										int k4 = this.field_1182_g[block.blockID * 3 + 2];
										f7 = world.getLightBrightness(j1, j2, i1 + 1) * 0.8F + 0.2F;
										float f9;
										if(i2 >= 0) {
											f9 = f4 * f * 0.6F;
											if(this.field_1186_c[j3] <= j2 - 1) {
												this.field_1186_c[j3] = j2 - 1;
												this.field_1180_b[j3] = -16777216 | (int)(this.field_1181_a[l3 * 3 + 0] * f9) << 16 | (int)(this.field_1181_a[l3 * 3 + 1] * f9) << 8 | (int)(this.field_1181_a[l3 * 3 + 2] * f9);
											}
										}

										if(i2 < 31) {
											f9 = f7 * 0.9F * f * 0.4F;
											if(this.field_1186_c[j3 + 1] <= j2 - 1) {
												this.field_1186_c[j3 + 1] = j2 - 1;
												this.field_1180_b[j3 + 1] = -16777216 | (int)(this.field_1181_a[k4 * 3 + 0] * f9) << 16 | (int)(this.field_1181_a[k4 * 3 + 1] * f9) << 8 | (int)(this.field_1181_a[k4 * 3 + 2] * f9);
											}
										}
									}
								}
							}
						}
					}
				}

				this.func_800_a();
				if(isoimagebuffer.field_1348_a == null) {
					isoimagebuffer.field_1348_a = new BufferedImage(32, 160, 2);
				}

				isoimagebuffer.field_1348_a.setRGB(0, 0, 32, 160, this.field_1180_b, 0, 32);
				isoimagebuffer.field_1352_e = true;
			}
		}
	}

	private void func_800_a() {
		for(int i = 0; i < 32; ++i) {
			for(int j = 0; j < 160; ++j) {
				int k = i + j * 32;
				if(this.field_1186_c[k] == 0) {
					this.field_1180_b[k] = 0;
				}

				if(this.field_1185_d[k] > this.field_1186_c[k]) {
					int l = this.field_1180_b[k] >> 24 & 255;
					this.field_1180_b[k] = ((this.field_1180_b[k] & 16711422) >> 1) + this.field_1184_e[k];
					if(l < 128) {
						this.field_1180_b[k] = Integer.MIN_VALUE + this.field_1184_e[k] * 2;
					} else {
						this.field_1180_b[k] |= -16777216;
					}
				}
			}
		}

	}
}
