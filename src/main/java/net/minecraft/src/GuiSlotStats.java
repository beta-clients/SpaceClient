package net.minecraft.src;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lwjgl.input.Mouse;

abstract class GuiSlotStats extends GuiSlot {
	protected int field_27268_b;
	protected List field_27273_c;
	protected Comparator field_27272_d;
	protected int field_27271_e;
	protected int field_27270_f;
	final GuiStats field_27269_g;

	protected GuiSlotStats(GuiStats guistats) {
		super(GuiStats.func_27143_f(guistats), guistats.width, guistats.height, 32, guistats.height - 64, 20);
		this.field_27269_g = guistats;
		this.field_27268_b = -1;
		this.field_27271_e = -1;
		this.field_27270_f = 0;
		this.func_27258_a(false);
		this.func_27259_a(true, 20);
	}

	protected void elementClicked(int i, boolean flag) {
	}

	protected boolean isSelected(int i) {
		return false;
	}

	protected void drawBackground() {
		this.field_27269_g.drawDefaultBackground();
	}

	protected void func_27260_a(int i, int j, Tessellator tessellator) {
		if(!Mouse.isButtonDown(0)) {
			this.field_27268_b = -1;
		}

		if(this.field_27268_b == 0) {
			GuiStats.func_27128_a(this.field_27269_g, i + 115 - 18, j + 1, 0, 0);
		} else {
			GuiStats.func_27128_a(this.field_27269_g, i + 115 - 18, j + 1, 0, 18);
		}

		if(this.field_27268_b == 1) {
			GuiStats.func_27128_a(this.field_27269_g, i + 165 - 18, j + 1, 0, 0);
		} else {
			GuiStats.func_27128_a(this.field_27269_g, i + 165 - 18, j + 1, 0, 18);
		}

		if(this.field_27268_b == 2) {
			GuiStats.func_27128_a(this.field_27269_g, i + 215 - 18, j + 1, 0, 0);
		} else {
			GuiStats.func_27128_a(this.field_27269_g, i + 215 - 18, j + 1, 0, 18);
		}

		if(this.field_27271_e != -1) {
			short c = 79;
			byte byte0 = 18;
			if(this.field_27271_e == 1) {
				c = 129;
			} else if(this.field_27271_e == 2) {
				c = 179;
			}

			if(this.field_27270_f == 1) {
				byte0 = 36;
			}

			GuiStats.func_27128_a(this.field_27269_g, i + c, j + 1, byte0, 0);
		}

	}

	protected void func_27255_a(int i, int j) {
		this.field_27268_b = -1;
		if(i >= 79 && i < 115) {
			this.field_27268_b = 0;
		} else if(i >= 129 && i < 165) {
			this.field_27268_b = 1;
		} else if(i >= 179 && i < 215) {
			this.field_27268_b = 2;
		}

		if(this.field_27268_b >= 0) {
			this.func_27266_c(this.field_27268_b);
			GuiStats.func_27149_g(this.field_27269_g).sndManager.playSoundFX("random.click", 1.0F, 1.0F);
		}

	}

	protected final int getSize() {
		return this.field_27273_c.size();
	}

	protected final StatCrafting func_27264_b(int i) {
		return (StatCrafting)this.field_27273_c.get(i);
	}

	protected abstract String func_27263_a(int var1);

	protected void func_27265_a(StatCrafting statcrafting, int i, int j, boolean flag) {
		String s1;
		if(statcrafting != null) {
			s1 = statcrafting.func_27084_a(GuiStats.func_27142_c(this.field_27269_g).writeStat(statcrafting));
			this.field_27269_g.drawString(GuiStats.func_27133_h(this.field_27269_g), s1, i - GuiStats.func_27137_i(this.field_27269_g).getStringWidth(s1), j + 5, flag ? 16777215 : 9474192);
		} else {
			s1 = "-";
			this.field_27269_g.drawString(GuiStats.func_27132_j(this.field_27269_g), s1, i - GuiStats.func_27134_k(this.field_27269_g).getStringWidth(s1), j + 5, flag ? 16777215 : 9474192);
		}

	}

	protected void func_27257_b(int i, int j) {
		if(j >= this.top && j <= this.bottom) {
			int k = this.func_27256_c(i, j);
			int l = this.field_27269_g.width / 2 - 92 - 16;
			if(k >= 0) {
				if(i < l + 40 || i > l + 40 + 20) {
					return;
				}

				StatCrafting s1 = this.func_27264_b(k);
				this.func_27267_a(s1, i, j);
			} else {
				String s = "";
				if(i >= l + 115 - 18 && i <= l + 115) {
					s = this.func_27263_a(0);
				} else if(i >= l + 165 - 18 && i <= l + 165) {
					s = this.func_27263_a(1);
				} else {
					if(i < l + 215 - 18 || i > l + 215) {
						return;
					}

					s = this.func_27263_a(2);
				}

				s = ("" + StringTranslate.getInstance().translateKey(s)).trim();
				if(s.length() > 0) {
					int i1 = i + 12;
					int j1 = j - 12;
					int k1 = GuiStats.func_27139_l(this.field_27269_g).getStringWidth(s);
					GuiStats.func_27129_a(this.field_27269_g, i1 - 3, j1 - 3, i1 + k1 + 3, j1 + 8 + 3, -1073741824, -1073741824);
					GuiStats.func_27144_m(this.field_27269_g).drawStringWithShadow(s, i1, j1, -1);
				}
			}

		}
	}

	protected void func_27267_a(StatCrafting statcrafting, int i, int j) {
		if(statcrafting != null) {
			Item item = Item.itemsList[statcrafting.func_25072_b()];
			String s = ("" + StringTranslate.getInstance().translateNamedKey(item.getItemName())).trim();
			if(s.length() > 0) {
				int k = i + 12;
				int l = j - 12;
				int i1 = GuiStats.func_27127_n(this.field_27269_g).getStringWidth(s);
				GuiStats.func_27135_b(this.field_27269_g, k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
				GuiStats.func_27131_o(this.field_27269_g).drawStringWithShadow(s, k, l, -1);
			}

		}
	}

	protected void func_27266_c(int i) {
		if(i != this.field_27271_e) {
			this.field_27271_e = i;
			this.field_27270_f = -1;
		} else if(this.field_27270_f == -1) {
			this.field_27270_f = 1;
		} else {
			this.field_27271_e = -1;
			this.field_27270_f = 0;
		}

		Collections.sort(this.field_27273_c, this.field_27272_d);
	}
}
