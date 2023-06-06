package wehavecookies56.bonfires.client.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Matrix4f;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.client.ScreenshotUtils;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

/**
 * Created by Toby on 06/11/2016.
 */
public class BonfireRenderer implements BlockEntityRenderer<BonfireTileEntity> {

    BlockEntityRendererProvider.Context context;

    public BonfireRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BonfireTileEntity te, float pPartialTicks, PoseStack stack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        if (te.isBonfire()) {
            stack.pushPose();
            stack.translate(0.5, 0.65, 0.5);
            if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getBlock() == BlockSetup.ash_bone_pile.get()) {
                if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.NORTH) {
                    stack.mulPose(Axis.YP.rotationDegrees(0));
                }
                else if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.EAST) {
                    stack.mulPose(Axis.YP.rotationDegrees(90));
                }
                else if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.SOUTH) {
                    stack.mulPose(Axis.YP.rotationDegrees(180));
                }
                else if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.WEST) {
                    stack.mulPose(Axis.YP.rotationDegrees(270));
                }
            }
            stack.mulPose(Axis.ZP.rotationDegrees(-130));
            Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(ItemSetup.coiled_sword.get()), ItemDisplayContext.NONE, pCombinedLight, pCombinedOverlay, stack, pBuffer, Minecraft.getInstance().level, 0);
            stack.popPose();
            if (te.isLit() && !ScreenshotUtils.isTimerStarted()) {
                renderNameTag(te, te.getDisplayName(), stack, pBuffer, pCombinedLight, pPartialTicks);
            }
        }
    }

    protected void renderNameTag(BonfireTileEntity te, Component pDisplayName, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, float partialTicks) {
        if (!pDisplayName.getString().isEmpty() && lookingAt(partialTicks, te)) {
            float f = (float) (te.getBlockState().getCollisionShape(Minecraft.getInstance().level, te.getBlockPos()).max(Direction.Axis.Y) + 0.5F);
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5D, (double)f, 0.5D);
            pMatrixStack.mulPose(context.getBlockEntityRenderDispatcher().camera.rotation());
            pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pMatrixStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            Font fontrenderer = context.getFont();
            float f2 = (float)(-fontrenderer.width(pDisplayName) / 2);
            fontrenderer.drawInBatch(pDisplayName, f2, 0, 553648127, false, matrix4f, pBuffer, Font.DisplayMode.NORMAL, j, pPackedLight);
            fontrenderer.drawInBatch(pDisplayName, f2, 0, -1, false, matrix4f, pBuffer, Font.DisplayMode.NORMAL, 0, pPackedLight);
            pMatrixStack.popPose();
        }
    }

    boolean lookingAt(float partialTicks, BonfireTileEntity te) {
        HitResult rayTraceResult = Minecraft.getInstance().player.pick(20, partialTicks, false);
        if (((BlockHitResult)rayTraceResult).getBlockPos().equals(te.getBlockPos())) {
            return true;
        } else {
            return false;
        }
    }
}
