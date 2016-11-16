package uk.co.wehavecookies56.bonfires.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBonePile;

/**
 * Created by Toby on 06/11/2016.
 */
public class TESRBonfire extends TileEntitySpecialRenderer<TileEntityBonfire> {

    private RenderItem renderItem;

    @Override
    public void renderTileEntityAt(TileEntityBonfire te, double x, double y, double z, float partialTicks, int destroyStage) {
        if(te != null && te instanceof TileEntityBonfire) {
            if (te.isBonfire()) {

                GlStateManager.pushAttrib();
                GlStateManager.pushMatrix();

                GlStateManager.translate(x, y, z);
                GlStateManager.disableRescaleNormal();
                this.renderItem = Minecraft.getMinecraft().getRenderItem();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableLighting();
                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(0.5, 0.75, 0.5);
                    if (Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()).getBlock() == Bonfires.ashBonePile) {
                        if (Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()).getValue(BlockAshBonePile.FACING) == EnumFacing.NORTH) {
                            GlStateManager.rotate(0, 0, 1, 0);
                        }
                        else if (Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()).getValue(BlockAshBonePile.FACING) == EnumFacing.EAST) {
                            GlStateManager.rotate(90, 0, 1, 0);
                        }
                        else if (Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()).getValue(BlockAshBonePile.FACING) == EnumFacing.SOUTH) {
                            GlStateManager.rotate(180, 0, 1, 0);
                        }
                        else if (Minecraft.getMinecraft().theWorld.getBlockState(te.getPos()).getValue(BlockAshBonePile.FACING) == EnumFacing.WEST) {
                            GlStateManager.rotate(270, 0, 1, 0);
                        }
                    }
                    GlStateManager.rotate(-130, 0, 0, 1);
                    GlStateManager.scale(1, 1, 1);
                    renderItem.renderItem(new ItemStack(Bonfires.coiledSword), ItemCameraTransforms.TransformType.NONE);
                }
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
                GlStateManager.popAttrib();
            }
            if (te.isLit()) {

            }
        }

        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    }
}
