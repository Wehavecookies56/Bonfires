package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import wehavecookies56.bonfires.client.gui.ReinforceScreen;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.awt.*;

public class ReinforceItemButton extends Button {

    ReinforceScreen parent;

    public ReinforceItemButton(ReinforceScreen parent, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(new Builder(Component.empty(), button -> parent.action(buttonId)).pos(x, y).size(widthIn, heightIn));
        this.parent = parent;
    }

    public void drawItem(ItemStack istack, GuiGraphics guiGraphics, int x, int y, int scale) {
        if (!istack.isEmpty()) {
            BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(istack, Minecraft.getInstance().level, Minecraft.getInstance().player, 0);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)(x + 16), (float)(y + 16), (float)(150));

            try {
                guiGraphics.pose().mulPose((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
                guiGraphics.pose().scale(16.0F * scale, 16.0F * scale, 16.0F * scale);
                boolean flag = !bakedmodel.usesBlockLight();
                if (flag) {
                    Lighting.setupForFlatItems();
                }

                Minecraft.getInstance().getItemRenderer().render(istack, ItemDisplayContext.GUI, false, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
                guiGraphics.flush();
                if (flag) {
                    Lighting.setupFor3DItems();
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> String.valueOf(istack.getItem()));
                crashreportcategory.setDetail("Item Components", () -> String.valueOf(istack.getComponents()));
                crashreportcategory.setDetail("Item Foil", () -> String.valueOf(istack.hasFoil()));
                throw new ReportedException(crashreport);
            }

            guiGraphics.pose().popPose();
        }
    }

    public void drawButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, float scrollOffset) {
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            double scale = mc.getWindow().getGuiScale();
            int scissorX = getX(), scissorY = getY(), scissorWidth = 239, scissorHeight = 171;
            //RenderSystem.enableScissor(0, mc.getWindow().getGuiScaledHeight() - (scissorY + scissorHeight) * scale, (scissorWidth + scissorX) * scale, scissorHeight * scale);
            RenderSystem.enableScissor(0, mc.getWindow().getHeight() - (int)((scissorY + scissorHeight) * scale), mc.getWindow().getWidth(), (int) (scissorHeight * scale));
            //guiGraphics.enableScissor(0, mc.getWindow().getHeight() - (int)((scissorY + scissorHeight) * scale), mc.getWindow().getWidth(), (int) (scissorHeight * scale));
            int insideWidth = getX() + width;
            if (parent.scrollBar.visible) {
                insideWidth -= 8;
            }
            int elementHeight = 36;
            if (parent.itemSelected != -1 ) {
                guiGraphics.fill(getX(), getY() - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth, (int)(getY() - scrollOffset + elementHeight + (elementHeight * parent.itemSelected)), new Color(160, 160, 160).hashCode());
                guiGraphics.fill(getX() + 1, getY() + 1 - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth - 1, (int)(getY() - scrollOffset + elementHeight + (elementHeight * parent.itemSelected) - 1), new Color(0, 0, 0).hashCode());
            }
            for (int i = 0; i < parent.reinforceableItems.size(); i++) {
                float yPos = getY()+2 + (((32 + 4) * i) - scrollOffset);
                drawItem(parent.reinforceableItems.get(i), guiGraphics, getX()+2, (int)yPos, 2);
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
                guiGraphics.drawString(mc.font, itemName, getX()+2 + 32, ((int)yPos + 16) - (mc.font.lineHeight / 2), new Color(255, 255, 255).hashCode());
                //ItemStack required = ReinforceHandler.getRequiredResources(parent.reinforceableItems.get(i));
                //int textWidth = mc.font.width(required.getDisplayName());
                //drawString(stack, mc.font, required.getDisplayName(), (x+2 + 220) - textWidth, ((int)yPos + 10) - (mc.font.lineHeight / 2), new Color(255, 255, 255).hashCode());
            }
            RenderSystem.disableScissor();
            //guiGraphics.disableScissor();
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_230430_2_, int p_230430_3_, float p_230430_4_) {

    }

    public boolean mousePressed(Minecraft mc, double mouseX, double mouseY, float scrollOffset) {
        int minusWidth = 0;
        if (parent.scrollBar.visible) {
            minusWidth -= 8;
        }
        if (visible && active) {
            if (mouseX >= getX() && mouseX <= getX() + width + minusWidth) {
                if (mouseY >= getY() && mouseY <= getY() + height) {
                    double truePos = (mouseY - getY()) + scrollOffset;
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
