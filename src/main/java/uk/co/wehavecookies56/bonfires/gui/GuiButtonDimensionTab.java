package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Toby on 14/11/2016.
 */
public class GuiButtonDimensionTab extends GuiButton {

    GuiBonfire parent;
    int dimension;
    Item icon = Items.FILLED_MAP;

    public GuiButtonDimensionTab(GuiBonfire parent, int buttonId, int x, int y) {
        super(buttonId, x, y, 28, 30, "");
        this.parent = parent;
        this.dimension = dimension;
    }

    public Item getIcon() {
        return icon;
    }

    public void setIcon(Item icon) {
        this.icon = icon;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            int tab_width = 28;
            int tab_height = 30;
            int tab_u = 28;
            int tab_v = parent.travel_height;
            Minecraft.getMinecraft().renderEngine.bindTexture(parent.TRAVEL_TEX);
            if (parent.dimTabSelected == id) {
                tab_v = parent.travel_height + 30;
                tab_height = 32;
                drawTexturedModalRect(xPosition, yPosition, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(icon), xPosition + (tab_width / 2) - 8, yPosition + (tab_height / 2) - 8);
            } else {
                drawTexturedModalRect(xPosition, yPosition-1, tab_u, tab_v, tab_width, tab_height);
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(icon), xPosition + (tab_width / 2) - 8, yPosition + (tab_height / 2) - 8 -1);
            }
        }
    }
}
