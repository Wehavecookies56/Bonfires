package wehavecookies56.bonfires.client.tiles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
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

    BlockEntityRendererFactory.Context context;

    public BonfireRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(BonfireTileEntity te, float pPartialTicks, MatrixStack stack, VertexConsumerProvider pBuffer, int pCombinedLight, int pCombinedOverlay) {
        if (te.isBonfire()) {
            stack.push();
            stack.translate(0.5, 0.65, 0.5);
            if (MinecraftClient.getInstance().world.getBlockState(te.getPos()).getBlock() == BlockSetup.ash_bone_pile) {
                if (MinecraftClient.getInstance().world.getBlockState(te.getPos()).get(AshBonePileBlock.FACING) == Direction.NORTH) {
                    stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0));
                }
                else if (MinecraftClient.getInstance().world.getBlockState(te.getPos()).get(AshBonePileBlock.FACING) == Direction.EAST) {
                    stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                }
                else if (MinecraftClient.getInstance().world.getBlockState(te.getPos()).get(AshBonePileBlock.FACING) == Direction.SOUTH) {
                    stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                }
                else if (MinecraftClient.getInstance().world.getBlockState(te.getPos()).get(AshBonePileBlock.FACING) == Direction.WEST) {
                    stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270));
                }
            }
            stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-130));
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ItemSetup.coiled_sword), ModelTransformationMode.NONE, pCombinedLight, pCombinedOverlay, stack, pBuffer, MinecraftClient.getInstance().world, 0);
            stack.pop();
            if (te.isLit() && !ScreenshotUtils.isTimerStarted()) {
                renderNameTag(te, te.getDisplayName(), stack, pBuffer, pCombinedLight, pPartialTicks);
            }
        }
    }

    protected void renderNameTag(BonfireTileEntity te, Text pDisplayName, MatrixStack pMatrixStack, VertexConsumerProvider pBuffer, int pPackedLight, float partialTicks) {
        if (!pDisplayName.getString().isEmpty() && lookingAt(partialTicks, te)) {
            float f = (float) (te.getCachedState().getCollisionShape(MinecraftClient.getInstance().world, te.getPos()).getMax(Direction.Axis.Y) + 0.5F);
            pMatrixStack.push();
            pMatrixStack.translate(0.5D, (double)f, 0.5D);
            pMatrixStack.multiply(context.getEntityRenderDispatcher().camera.getRotation());
            pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pMatrixStack.peek().getPositionMatrix();
            float f1 = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            TextRenderer fontrenderer = context.getTextRenderer();
            float f2 = (float)(-fontrenderer.getWidth(pDisplayName) / 2);
            fontrenderer.draw(pDisplayName, f2, 0, 553648127, false, matrix4f, pBuffer, TextRenderer.TextLayerType.NORMAL, j, pPackedLight);
            fontrenderer.draw(pDisplayName, f2, 0, -1, false, matrix4f, pBuffer, TextRenderer.TextLayerType.NORMAL, 0, pPackedLight);
            pMatrixStack.pop();
        }
    }

    boolean lookingAt(float partialTicks, BonfireTileEntity te) {
        HitResult rayTraceResult = MinecraftClient.getInstance().player.raycast(20, partialTicks, false);
        if (rayTraceResult.getPos().distanceTo(te.getPos().toCenterPos()) < 0.8) {
            return true;
        } else {
            return false;
        }
    }
}
