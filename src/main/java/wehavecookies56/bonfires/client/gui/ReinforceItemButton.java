package wehavecookies56.bonfires.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.awt.Color;

public class ReinforceItemButton extends Button {

    ReinforceScreen parent;

    public ReinforceItemButton(ReinforceScreen parent, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(x, y, widthIn, heightIn, new TranslationTextComponent(""), button -> parent.action(buttonId));
        this.parent = parent;
    }

    public void drawButtons(MatrixStack stack, int mouseX, int mouseY, float partialTicks, float scrollOffset) {
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.pushMatrix();

            double scale = mc.getWindow().getGuiScale();
            int scissorX = x, scissorY = y, scissorWidth = 239, scissorHeight = 171;
            //RenderSystem.enableScissor(0, mc.getWindow().getGuiScaledHeight() - (scissorY + scissorHeight) * scale, (scissorWidth + scissorX) * scale, scissorHeight * scale);
            RenderSystem.enableScissor(0, mc.getWindow().getHeight() - (int)((scissorY + scissorHeight) * scale), mc.getWindow().getWidth(), (int) (scissorHeight * scale));
            int insideWidth = x + width;
            if (parent.scrollBar.visible) {
                insideWidth -= 8;
            }
            int elementHeight = 36;
            if (parent.itemSelected != -1 ) {
                fill(stack, x, y - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth, (int)(y - scrollOffset + elementHeight + (elementHeight * parent.itemSelected)), new Color(160, 160, 160).hashCode());
                fill(stack, x + 1, y + 1 - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth - 1, (int)(y - scrollOffset + elementHeight + (elementHeight * parent.itemSelected) - 1), new Color(0, 0, 0).hashCode());
            }
            for (int i = 0; i < parent.reinforceableItems.size(); i++) {
                stack.pushPose();
                float yPos = y+2 + (((32 + 4) * i) - scrollOffset);
                stack.translate(x+2, (int)yPos, 0);
                stack.scale(2, 2, 0);
                RenderSystem.pushMatrix();
                RenderSystem.translatef(x+2, (int)yPos, 0);
                RenderSystem.scalef(2, 2, 2);
                mc.getItemRenderer().renderGuiItem(parent.reinforceableItems.get(i), 0, 0);
                RenderSystem.popMatrix();
                stack.popPose();
                ItemStack item = parent.reinforceableItems.get(i);
                int nextLevel = ReinforceHandler.getReinforceLevel(item).level()+1;
                String nextLevelText = Integer.toString(nextLevel);
                if (nextLevel == ReinforceHandler.getReinforceLevel(item).maxLevel()) {
                    nextLevelText = "MAX";
                } else if (nextLevel > ReinforceHandler.getReinforceLevel(item).maxLevel()) {

                }
                String itemName = parent.reinforceableItems.get(i).getHoverName().getString();
                if (ReinforceHandler.getReinforceLevel(item).level() > 0) {
                     itemName += " +" + ReinforceHandler.getReinforceLevel(item).level();
                }
                drawString(stack, mc.font, itemName, x+2 + 32, ((int)yPos + 16) - (mc.font.lineHeight / 2), new Color(255, 255, 255).hashCode());
                //ItemStack required = ReinforceHandler.getRequiredResources(parent.reinforceableItems.get(i));
                //int textWidth = mc.fontRenderer.getStringWidth(required.getDisplayName());
                //drawString(mc.fontRenderer, required.getDisplayName(), (x+2 + 220) - textWidth, ((int)yPos + 10) - (mc.fontRenderer.FONT_HEIGHT / 2), new Color(255, 255, 255).hashCode());
            }
            RenderSystem.disableScissor();
            RenderSystem.popMatrix();
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {

    }

    public boolean mousePressed(Minecraft mc, double mouseX, double mouseY, float scrollOffset) {
        int minusWidth = 0;
        if (parent.scrollBar.visible) {
            minusWidth -= 8;
        }
        if (visible && active) {
            if (mouseX >= x && mouseX <= x + width + minusWidth) {
                if (mouseY >= y && mouseY <= y + height) {
                    double truePos = (mouseY - y) + scrollOffset;
                    int index = (int)(truePos) / 36;
                    if (parent.reinforceableItems.size()-1 >= index) {
                        parent.itemSelected = (int) (truePos) / 36;
                        playDownSound(mc.getSoundManager());
                    }
                }
            }
        }
        return true;
    }
}
