package wehavecookies56.bonfires.client.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

/**
 * Created by Toby on 06/11/2016.
 */
public class BonfireRenderer extends TileEntityRenderer<BonfireTileEntity> {

    public BonfireRenderer(TileEntityRendererDispatcher context) {
        super(context);
    }

    @Override
    public void render(BonfireTileEntity te, float pPartialTicks, MatrixStack stack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        if (te.isBonfire()) {
            if (te.isLit()) {
                renderNameTag(te, te.getDisplayName(), stack, pBuffer, pCombinedLight, pPartialTicks);
            }
            stack.pushPose();
            stack.translate(0.5, 0.65, 0.5);
            if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getBlock() == BlockSetup.ash_bone_pile.get()) {
                if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.NORTH) {
                    stack.mulPose(Vector3f.YP.rotationDegrees(0));
                }
                else if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.EAST) {
                    stack.mulPose(Vector3f.YP.rotationDegrees(90));
                }
                else if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.SOUTH) {
                    stack.mulPose(Vector3f.YP.rotationDegrees(180));
                }
                else if (Minecraft.getInstance().level.getBlockState(te.getBlockPos()).getValue(AshBonePileBlock.FACING) == Direction.WEST) {
                    stack.mulPose(Vector3f.YP.rotationDegrees(270));
                }
            }
            stack.mulPose(Vector3f.ZP.rotationDegrees(-130));
            Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(ItemSetup.coiled_sword.get()), ItemCameraTransforms.TransformType.NONE, pCombinedLight, pCombinedOverlay, stack, pBuffer);
            stack.popPose();
        }
    }

    protected void renderNameTag(BonfireTileEntity te, ITextComponent pDisplayName, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, float partialTicks) {
        if (!pDisplayName.getString().isEmpty() && lookingAt(partialTicks, te)) {
            float f = (float) (te.getBlockState().getCollisionShape(Minecraft.getInstance().level, te.getBlockPos()).max(Direction.Axis.Y) + 0.5F);
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5D, (double)f, 0.5D);
            pMatrixStack.mulPose(this.renderer.camera.rotation());
            pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pMatrixStack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            FontRenderer fontrenderer = this.renderer.getFont();
            float f2 = (float)(-fontrenderer.width(pDisplayName) / 2);
            fontrenderer.drawInBatch(pDisplayName, f2, 0, 0xFFFFFF, true, matrix4f, pBuffer, false, 0, pPackedLight);
            pMatrixStack.popPose();
        }
    }

    boolean lookingAt(float partialTicks, BonfireTileEntity te) {
        RayTraceResult rayTraceResult = Minecraft.getInstance().player.pick(20, partialTicks, false);
        if (((BlockRayTraceResult)rayTraceResult).getBlockPos().equals(te.getBlockPos())) {
            return true;
        } else {
            return false;
        }
    }
}
