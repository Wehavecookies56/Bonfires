package uk.co.wehavecookies56.bonfires.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.SyncBonfire;
import uk.co.wehavecookies56.bonfires.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.gui.GuiHandler;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */
public class BlockAshBonePile extends Block implements ITileEntityProvider {



    public BlockAshBonePile(Material blockMaterialIn) {
        super(blockMaterialIn);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return super.getItemDropped(state, rand, fortune);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityBonfire) {
            TileEntityBonfire te = (TileEntityBonfire) worldIn.getTileEntity(pos);
            if (te.isBonfire()) {
                System.out.println(te.isLit());
                if (!te.isLit()) {
                    //OPEN LIGHTING GUI
                    if (!worldIn.isRemote) {
                        System.out.println("NOT LIT");
                        te.setLit(true);
                        te.setName("TEST");
                        te.setPos(te.getPos());
                        te.setID(UUID.randomUUID());
                    } else {
                        playerIn.openGui(Bonfires.instance, GuiHandler.GUI_BONFIRECREATION, worldIn, pos.getX(), pos.getY(), pos.getZ());
                    }
                } else {
                    //OPEN MAIN GUI
                }
            } else {
                if (heldItem != null) {
                    if (heldItem.getItem() == Bonfires.coiledSword) {
                        if (!worldIn.isRemote) {
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                            te.setBonfire(true);
                            te.setLit(false);
                            PacketDispatcher.sendToAllAround(new SyncBonfire(true, te), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

    }

    @Override
    public int tickRate(World worldIn) {
        return 20;
    }

    @Override
    public boolean getTickRandomly() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBonfire();
    }
}
