package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.opengl.GL11;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.BonfiresConfig;

import javax.annotation.Nonnull;

/**
 * Created by Toby on 14/11/2016.
 */
class GuiButtonDimensionTab extends GuiButton {

    private GuiBonfire parent;
    private int dimension;
    private Item icon = Items.FILLED_MAP;

    GuiButtonDimensionTab(GuiBonfire parent, int buttonId, int x, int y) {
        super(buttonId, x, y, 28, 30, "");
        this.parent = parent;
    }

    private Item getIcon() {
        if (icon == Items.FILLED_MAP) {
            String[] icons = BonfiresConfig.tabIcons;
            for (String s : icons) {
                if (s.split("=").length == 2) {
                    String dimID = s.split("=")[0];
                    String item = s.split("=")[1];
                    try {
                        if (Integer.parseInt(dimID) == dimension) {
                            if (GameRegistry.findRegistry(Item.class).containsKey(new ResourceLocation(item))) {
                                return icon = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(item));
                            } else {
                                return icon;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Bonfires.logger.error(dimID + " is not a valid dimension ID, the ID should be an integer");
                        return icon;
                    }
                } else {
                    Bonfires.logger.error(s + " is an invalid icon setting");
                }
            }
        } else {
            return icon;
        }
        return icon;
    }

    public void setIcon(Item icon) {
        this.icon = icon;
    }
    
    void resetIcon() {
        this.icon = Items.FILLED_MAP;
    }
    
    public int getDimension() {
        return dimension;
    }

    void setDimension(int dimension) {
        this.dimension = dimension;
        this.resetIcon();
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            Minecraft.getMinecraft().renderEngine.bindTexture(parent.TRAVEL_TEX);
            GL11.glColor4f(1, 1, 1, 1);
            if (parent.dimTabSelected == id) {
                tab_v = parent.travel_height + 30;
                tab_height = 32;
                drawTexturedModalRect(x, y, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(getIcon()), x + (tab_width / 2) - 8, y + (tab_height / 2) - 8);
            } else {
                drawTexturedModalRect(x, y - 1, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(getIcon()), x + (tab_width / 2) - 8, y + (tab_height / 2) - 8 -1);
            }
        }
        GL11.glPopMatrix();
    }
}
