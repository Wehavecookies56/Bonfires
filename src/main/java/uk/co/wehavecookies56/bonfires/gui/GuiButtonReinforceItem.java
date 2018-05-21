package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import uk.co.wehavecookies56.bonfires.ReinforceHandler;

import java.awt.Color;
import java.util.List;

public class GuiButtonReinforceItem extends GuiButton {

    GuiReinforce parent;

    public GuiButtonReinforceItem(GuiReinforce parent, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.parent = parent;
    }

    public void drawButtons(Minecraft mc, int mouseX, int mouseY, float partialTicks, float scrollOffset) {
        if (visible) {
            GL11.glPushMatrix();
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int scale = scaledResolution.getScaleFactor(), scissorX = x, scissorY = y, scissorWidth = 239, scissorHeight = 171;
            GL11.glScissor(0, mc.displayHeight - (scissorY + scissorHeight) * scale, (scissorWidth + scissorX) * scale, scissorHeight * scale);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            int insideWidth = x + width;
            if (parent.scrollBar.visible) {
                insideWidth -= 8;
            }
            int elementHeight = 36;
            if (parent.itemSelected != -1 ) {
                drawRect(x, y - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth, (int)(y - scrollOffset + elementHeight + (elementHeight * parent.itemSelected)), new Color(160, 160, 160).hashCode());
                drawRect(x + 1, y + 1 - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth - 1, (int)(y - scrollOffset + elementHeight + (elementHeight * parent.itemSelected) - 1), new Color(0, 0, 0).hashCode());
            }
            for (int i = 0; i < parent.reinforceableItems.size(); i++) {
                GlStateManager.pushMatrix();
                float yPos = y+2 + (((32 + 4) * i) - scrollOffset);
                GlStateManager.translate(x+2, (int)yPos, 0);
                GlStateManager.scale(2, 2, 0);
                mc.getRenderItem().renderItemAndEffectIntoGUI(parent.reinforceableItems.get(i), 0, 0);
                GlStateManager.popMatrix();
                ItemStack item = parent.reinforceableItems.get(i);
                ReinforceHandler.IReinforceHandler handler = ReinforceHandler.getHandler(item);
                int nextLevel = handler.level()+1;
                String nextLevelText = Integer.toString(nextLevel);
                if (nextLevel == handler.maxLevel()) {
                    nextLevelText = "MAX";
                } else if (nextLevel > handler.maxLevel()) {

                }
                drawString(mc.fontRenderer, parent.reinforceableItems.get(i).getDisplayName(), x+2 + 32, ((int)yPos + 16) - (mc.fontRenderer.FONT_HEIGHT / 2), new Color(255, 255, 255).hashCode());
                //ItemStack required = ReinforceHandler.getRequiredResources(parent.reinforceableItems.get(i));
                //int textWidth = mc.fontRenderer.getStringWidth(required.getDisplayName());
                //drawString(mc.fontRenderer, required.getDisplayName(), (x+2 + 220) - textWidth, ((int)yPos + 10) - (mc.fontRenderer.FONT_HEIGHT / 2), new Color(255, 255, 255).hashCode());
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY, float scrollOffset) {
        int minusWidth = 0;
        if (parent.scrollBar.visible) {
            minusWidth -= 8;
        }
        if (visible && enabled) {
            if (mouseX >= x && mouseX <= x + width + minusWidth) {
                if (mouseY >= y && mouseY <= y + height) {
                    float truePos = (mouseY - y) + scrollOffset;
                    int index = (int)(truePos) / 36;
                    if (parent.reinforceableItems.size()-1 >= index) {
                        parent.itemSelected = (int) (truePos) / 36;
                        playPressSound(mc.getSoundHandler());
                    }
                }
            }
        }
        return true;
    }
}
