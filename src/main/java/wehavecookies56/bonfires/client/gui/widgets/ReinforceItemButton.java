package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.client.gui.ReinforceScreen;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.client.IDrawContextExtensions;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.awt.*;

public class ReinforceItemButton extends ButtonWidget {

    ReinforceScreen parent;

    public ReinforceItemButton(ReinforceScreen parent, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(x, y, widthIn, heightIn, Text.empty(), button -> parent.action(buttonId), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.parent = parent;
    }

    public void drawItem(ItemStack istack, DrawContext guiGraphics, int x, int y, int scale) {
        if (!istack.isEmpty()) {
            ItemRenderState itemRenderState = new ItemRenderState();
            MinecraftClient.getInstance().getItemModelManager().update(itemRenderState, istack, ModelTransformationMode.GUI, false, MinecraftClient.getInstance().world, MinecraftClient.getInstance().player, 0);
            guiGraphics.getMatrices().push();
            guiGraphics.getMatrices().translate((float)(x + (8 * scale)), (float)(y + (8 * scale)), (float)(150));

            try {
                guiGraphics.getMatrices().scale(16.0F * scale, 16.0F * scale, 16.0F * scale);
                boolean flag = !itemRenderState.isSideLit();
                if (flag) {
                    guiGraphics.draw();
                    DiffuseLighting.disableGuiDepthLighting();
                }

                itemRenderState.render(guiGraphics.getMatrices(), ((IDrawContextExtensions)guiGraphics).getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV);
                guiGraphics.draw();
                if (flag) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(istack.getItem()));
                crashReportSection.add("Item Components", () -> String.valueOf(istack.getComponents()));
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
            RenderSystem.enableScissor(0, mc.getWindow().getHeight() - (int)((scissorY + scissorHeight) * scale), mc.getWindow().getWidth(), (int) (scissorHeight * scale));
            int insideWidth = getX() + width;
            if (parent.scrollBar.visible) {
                insideWidth -= 8;
            }
            int elementHeight = 36;
            for (int i = 0; i < parent.reinforceableItems.size(); i++) {
                if (i % 2 != 0) {
                    guiGraphics.fill(getX(), getY() - (int) scrollOffset + (elementHeight * i), insideWidth, (int) (getY() - scrollOffset + elementHeight + (elementHeight * i)), new Color(44, 49, 43, 128).getRGB());
                }
            }
            if (parent.itemSelected != -1 ) {
                guiGraphics.fill(getX(), getY() - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth, (int)(getY() - scrollOffset + elementHeight + (elementHeight * parent.itemSelected)), new Color(160, 160, 160).getRGB());
                guiGraphics.fill(getX() + 1, getY() + 1 - (int)scrollOffset + (elementHeight * parent.itemSelected), insideWidth - 1, (int)(getY() - scrollOffset + elementHeight + (elementHeight * parent.itemSelected) - 1), new Color(0, 0, 0).getRGB());
            }
            for (int i = 0; i < parent.reinforceableItems.size(); i++) {
                float yPos = getY()+2 + (((32 + 4) * i) - scrollOffset);
                drawItem(parent.reinforceableItems.get(i), guiGraphics, getX()+2, (int)yPos, 2);
                ItemStack item = parent.reinforceableItems.get(i);
                int nextLevel = ReinforceHandler.getReinforceLevel(item).level()+1;
                String nextLevelText = Integer.toString(nextLevel);
                String itemName = parent.reinforceableItems.get(i).getName().getString();
                if (ReinforceHandler.getReinforceLevel(item).level() > 0) {
                    itemName += " +" + ReinforceHandler.getReinforceLevel(item).level();
                }
                double currentDamage = 0;
                double nextDamage = 0;
                currentDamage = Bonfires.CONFIG.common.reinforceDamagePerLevel() * ReinforceHandler.getReinforceLevel(item).level();
                nextDamage = currentDamage + Bonfires.CONFIG.common.reinforceDamagePerLevel();
                Text upgradeText = Text.translatable(LocalStrings.TEXT_REINFORCE_ATTACK);
                if (item.isOf(ItemSetup.estus_flask)) {
                    currentDamage = Bonfires.CONFIG.common.estusFlaskBaseHeal();
                    currentDamage += Bonfires.CONFIG.common.estusFlaskHealPerLevel() * ReinforceHandler.getReinforceLevel(item).level();
                    nextDamage = currentDamage;
                    nextDamage += Bonfires.CONFIG.common.estusFlaskHealPerLevel();
                    currentDamage /= 2;
                    nextDamage /= 2;
                    upgradeText = Text.translatable(LocalStrings.TEXT_REINFORCE_HEAL);
                }
                Text next = Text.literal("+" + nextLevelText);
                if (nextLevel-1 == ReinforceHandler.getReinforceLevel(item).maxLevel()) {
                    next = Text.translatable(LocalStrings.TEXT_REINFORCE_MAX);
                }
                guiGraphics.drawText(mc.textRenderer, itemName + " > " + next.getString(), getX()+2 + 34, ((int)yPos + 16) - (mc.textRenderer.fontHeight / 2), new Color(255, 255, 255).getRGB(), true);
                Text damageText = nextLevel-1 == ReinforceHandler.getReinforceLevel(item).maxLevel() ? Text.translatable(LocalStrings.TEXT_REINFORCE_MAX) : Text.literal("+" + nextDamage);
                guiGraphics.drawCenteredTextWithShadow(mc.textRenderer,  upgradeText, insideWidth - 35, ((int)yPos+8) - (mc.textRenderer.fontHeight / 2), new Color(255, 255, 255).getRGB());
                guiGraphics.drawText(mc.textRenderer,  "+" + currentDamage + " > " + damageText.getString(), insideWidth - 60, ((int)yPos+24) - (mc.textRenderer.fontHeight / 2), new Color(255, 255, 255).getRGB(), true);
            }
            RenderSystem.disableScissor();
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
