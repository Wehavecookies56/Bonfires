package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.joml.Matrix4f;
import wehavecookies56.bonfires.client.gui.ReinforceScreen;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.awt.*;

public class ReinforceItemButton extends ButtonWidget {

    ReinforceScreen parent;

    public ReinforceItemButton(ReinforceScreen parent, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(x, y, widthIn, heightIn, Text.empty(), button -> parent.action(buttonId), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.parent = parent;
    }

    public void drawItem(ItemStack istack, DrawContext guiGraphics, int x, int y, int scale) {
        if (!istack.isEmpty()) {
            BakedModel bakedmodel = MinecraftClient.getInstance().getItemRenderer().getModel(istack, MinecraftClient.getInstance().world, MinecraftClient.getInstance().player, 0);
            guiGraphics.getMatrices().push();
            guiGraphics.getMatrices().translate((float)(x + 16), (float)(y + 16), (float)(150));

            try {
                guiGraphics.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
                guiGraphics.getMatrices().scale(16.0F * scale, 16.0F * scale, 16.0F * scale);
                boolean flag = !bakedmodel.isSideLit();
                if (flag) {
                    DiffuseLighting.disableGuiDepthLighting();
                }

                MinecraftClient.getInstance().getItemRenderer().renderItem(istack, ModelTransformationMode.GUI, false, guiGraphics.getMatrices(), guiGraphics.getVertexConsumers(), 0xF000F0, OverlayTexture.DEFAULT_UV, bakedmodel);
                guiGraphics.draw();
                if (flag) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(istack.getItem()));
                crashReportSection.add("Item Damage", () -> String.valueOf(istack.getDamage()));
                crashReportSection.add("Item NBT", () -> String.valueOf(istack.getNbt()));
                crashReportSection.add("Item Foil", () -> String.valueOf(istack.hasGlint()));
                throw new CrashException(crashReport);
            }

            guiGraphics.getMatrices().pop();
        }
    }

    public void drawButtons(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks, float scrollOffset) {
        if (visible) {
            MinecraftClient mc = MinecraftClient.getInstance();
            double scale = mc.getWindow().getScaleFactor();
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
                String itemName = parent.reinforceableItems.get(i).getName().getString();
                if (ReinforceHandler.getReinforceLevel(item).level() > 0) {
                    itemName += " +" + ReinforceHandler.getReinforceLevel(item).level();
                }
                guiGraphics.drawText(mc.textRenderer, itemName, getX()+2 + 32, ((int)yPos + 16) - (mc.textRenderer.fontHeight / 2), new Color(255, 255, 255).hashCode(), true);
                //ItemStack required = ReinforceHandler.getRequiredResources(parent.reinforceableItems.get(i));
                //int textWidth = mc.font.width(required.getDisplayName());
                //drawString(stack, mc.font, required.getDisplayName(), (x+2 + 220) - textWidth, ((int)yPos + 10) - (mc.font.lineHeight / 2), new Color(255, 255, 255).hashCode());
            }
            RenderSystem.disableScissor();
            //guiGraphics.disableScissor();
        }
    }

    @Override
    public void renderWidget(DrawContext guiGraphics, int p_230430_2_, int p_230430_3_, float p_230430_4_) {

    }

    public boolean mousePressed(MinecraftClient mc, double mouseX, double mouseY, float scrollOffset) {
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
