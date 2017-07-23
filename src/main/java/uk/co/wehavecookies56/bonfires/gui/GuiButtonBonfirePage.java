package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by Toby on 17/12/2016.
 */
public class GuiButtonBonfirePage extends GuiButton {

    GuiBonfire parent;
    boolean isNext;

    public GuiButtonBonfirePage(GuiBonfire parent, int buttonId, int x, int y, boolean isNext) {
        super(buttonId, x, y,8,14,"");
        this.parent = parent;
        this.isNext = isNext;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.renderEngine.bindTexture(parent.TRAVEL_TEX);
            int texWidth = 8;
            int texHeight = 14;
            int u = parent.travel_width;
            if (isNext) {
                u = parent.travel_width + texWidth;
            }
            int v = 0;
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                v = texHeight;
            }
            if (!enabled) {
                v = texHeight * 2;
            }
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            drawTexturedModalRect(x, y, u, v, width, height);
            GlStateManager.popMatrix();
        }
    }

}
