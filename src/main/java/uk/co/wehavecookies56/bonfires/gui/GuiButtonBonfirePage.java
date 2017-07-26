package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

/**
 * Created by Toby on 17/12/2016.
 */
class GuiButtonBonfirePage extends GuiButton {

    private GuiBonfire parent;
    private boolean isNext;

    GuiButtonBonfirePage(GuiBonfire parent, int buttonId, int x, int y, boolean isNext) {
        super(buttonId, x, y, 8, 14, "");
        this.parent = parent;
        this.isNext = isNext;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.renderEngine.bindTexture(parent.TRAVEL_TEX);
            int texWidth = 8;
            int texHeight = 14;
            int u = 256 - texWidth * 2;
            if (isNext) {
                u = (256 - texWidth * 2) + texWidth;
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
