package uk.co.wehavecookies56.bonfires.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.gui.GuiBonfire;
import uk.co.wehavecookies56.bonfires.gui.GuiCreateBonfire;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.SyncBonfire;
import uk.co.wehavecookies56.bonfires.packets.SyncSaveData;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import javax.annotation.Nullable;
import java.util.Random;

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
        BonfireWorldSavedData.get(worldIn).markDirty();
        if (worldIn.getTileEntity(pos) instanceof TileEntityBonfire) {
            TileEntityBonfire te = (TileEntityBonfire) worldIn.getTileEntity(pos);
            if (te.isBonfire()) {
                if (!te.isLit()) {
                    //OPEN LIGHTING GUI
                    if (!worldIn.isRemote) {

                    } else {
                        if(BonfireRegistry.INSTANCE.getBonfire(te.getID()) != null) {
                            System.out.println("ID:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getId() + " NAME:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getName() + " DIM:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getDimension() + " OWNER:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getOwner() + " POS:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getPos() + " PUBLIC:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).isPublic());
                        }
                        //playerIn.openGui(Bonfires.instance, GuiHandler.GUI_BONFIRECREATION, worldIn, pos.getX(), pos.getY(), pos.getZ());
                        Minecraft.getMinecraft().displayGuiScreen(new GuiCreateBonfire(te));
                    }
                } else {
                    if (!worldIn.isRemote) {
                        if (BonfireRegistry.INSTANCE.getBonfire(te.getID()) != null)
                            System.out.println("ID:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getId() + " NAME:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getName() + " DIM:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getDimension() + " OWNER:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getOwner() + " POS:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).getPos() + " PUBLIC:" + BonfireRegistry.INSTANCE.getBonfire(te.getID()).isPublic());
                    } else {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiBonfire(te));
                    }
                }
            } else {
                if (heldItem != null) {
                    if (heldItem.getItem() == Bonfires.coiledSword) {
                        if (!worldIn.isRemote) {
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                            te.setBonfire(true);
                            te.setLit(false);
                            PacketDispatcher.sendToAllAround(new SyncBonfire(te.isBonfire(), te.isLit(), null, te), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {

        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityBonfire te = (TileEntityBonfire) worldIn.getTileEntity(pos);
        if (te != null) {
            if (te.isLit()) {
                te.destroyBonfire(te.getID());
                BonfireRegistry.INSTANCE.getBonfires().remove(te.getID());
                BonfireWorldSavedData.get(worldIn).markDirty();
                System.out.println("Destroyed bonfire: " + te.getID());
                System.out.println("All registered bonfires: ");
                for (Bonfire b : BonfireRegistry.INSTANCE.getBonfires().values()) {
                    System.out.println("NAME:" + b.getName() + " ID:" + b.getId());
                }
                PacketDispatcher.sendToAll(new SyncSaveData(BonfireRegistry.INSTANCE.getBonfires()));
            }
            worldIn.removeTileEntity(pos);
        }
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
