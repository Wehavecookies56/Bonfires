package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import wehavecookies56.bonfires.client.gui.ReinforceScreen;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.awt.Color;

public class ReinforceItemButton extends Button {

    ReinforceScreen parent;

    public ReinforceItemButton(ReinforceScreen parent, int buttonId, int x, int y, int widthIn, int heightIn) {
        super(x, y, widthIn, heightIn, Component.empty(), button -> parent.action(buttonId));
        this.parent = parent;
    }

    public void drawItem(ItemStack istack, PoseStack pstack, int x, int y, int scale) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel itemBakedModel = itemRenderer.getModel(istack, null, null, 0);

        Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        pstack.pushPose();
        pstack.translate(x, y, 100.0F);
        pstack.translate(8.0D * scale, 8.0D * scale, 0.0D);
        pstack.scale(1.0F, -1.0F, 1.0F);
        pstack.scale(16.0F * scale, 16.0F * scale, 16.0F * scale);
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !itemBakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(istack, ItemTransforms.TransformType.GUI, false, pstack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, itemBakedModel);
        multibuffersource$buffersource.endBatch();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        pstack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public void drawButtons(PoseStack stack, int mouseX, int mouseY, float partialTicks, float scrollOffset) {
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
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
                float yPos = y+2 + (((32 + 4) * i) - scrollOffset);
                drawItem(parent.reinforceableItems.get(i), stack, x+2, (int)yPos, 2);
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
                //int textWidth = mc.font.width(required.getDisplayName());
                //drawString(stack, mc.font, required.getDisplayName(), (x+2 + 220) - textWidth, ((int)yPos + 10) - (mc.font.lineHeight / 2), new Color(255, 255, 255).hashCode());
            }
            RenderSystem.disableScissor();
        }
    }

    @Override
    public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {

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
