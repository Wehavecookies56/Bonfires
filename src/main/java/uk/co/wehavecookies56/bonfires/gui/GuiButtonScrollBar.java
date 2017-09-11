package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.bonfires.Bonfires;

import java.awt.Color;

public class GuiButtonScrollBar extends GuiButton {

    int clickX, clickY, startX, startY, top, bottom;

    public GuiButtonScrollBar(int buttonId, int x, int y, int widthIn, int heightIn, int top, int bottom) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.top = top;
        this.bottom = bottom+15;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1, 1, 1, 0.5F);
            drawRect(x, top-1, x + width, bottom, new Color(0, 0, 0, 0.5F).hashCode());
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            mc.renderEngine.bindTexture(new ResourceLocation(Bonfires.modid, "textures/gui/reinforce_menu.png"));
            drawTexturedModalRect(x, y, 0, 220, 8, 15);
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (clickX >= x && clickX <= x + width) {
            if (startY - (clickY - mouseY) >= top - 1 && startY - (clickY - mouseY) <= bottom - height && enabled) {
                this.y = startY - (clickY - mouseY);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {

    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        clickX = mouseX;
        clickY = mouseY;
        startX = x;
        startY = y;
        return false;
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {

    }
}
