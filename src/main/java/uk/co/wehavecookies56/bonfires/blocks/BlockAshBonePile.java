package uk.co.wehavecookies56.bonfires.blocks;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import uk.co.wehavecookies56.bonfires.*;
import uk.co.wehavecookies56.bonfires.gui.GuiHandler;
import uk.co.wehavecookies56.bonfires.packets.*;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */
@SuppressWarnings("ALL")
public class BlockAshBonePile extends Block implements ITileEntityProvider {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool LIT = PropertyBool.create("lit");
    boolean dropFragment = false;

    public BlockAshBonePile(String name, Material blockMaterialIn) {
        super(blockMaterialIn);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false));
        setRegistryName(Bonfires.modid, name);
        setUnlocalizedName(getRegistryName().toString().replace(Bonfires.modid + ":", ""));
        setCreativeTab(Bonfires.tabBonfires);
        setHardness(0.8F);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(LIT) ? 4 : 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta - (meta > 3 ? 4 : 0))).withProperty(LIT, meta > 3);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(new ItemStack(getItemDropped(state, new Random(), fortune)));
        if (dropFragment) {
            stacks.add(new ItemStack(Bonfires.coiledSwordFragment));
            dropFragment = false;
        }
        return stacks;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        BonfireWorldSavedData.get(world).markDirty();
        if (world.getTileEntity(pos) instanceof TileEntityBonfire) {
            TileEntityBonfire te = (TileEntityBonfire) world.getTileEntity(pos);
            if (te.isBonfire()) {
                if (!te.isLit()) {
                    if (!world.isRemote) {
                    } else {
                        if(BonfireWorldSavedData.get(world).bonfires.getBonfire(te.getID()) != null) {

                        }
                        player.openGui(Bonfires.instance, GuiHandler.GUI_BONFIRECREATION, world, pos.getX(), pos.getY(), pos.getZ());
                    }
                } else {
                    if (!world.isRemote) {
                        if (BonfireWorldSavedData.get(world).bonfires.getBonfire(te.getID()) != null) {
                            GameProfile profile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getProfileByUUID(BonfireWorldSavedData.get(world).bonfires.getBonfire(te.getID()).getOwner());
                            PacketDispatcher.sendTo(new OpenBonfireGUI(profile.getName()), (EntityPlayerMP) player);
                            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                                if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                                    if (player.inventory.getStackInSlot(i).getItem() == Bonfires.estusFlask) {
                                        if (player.inventory.getStackInSlot(i).hasTagCompound()) {
                                            player.inventory.getStackInSlot(i).getTagCompound().setInteger("estus", player.inventory.getStackInSlot(i).getTagCompound().getInteger("uses"));
                                        }
                                    }
                                }
                                if (ReinforceHandler.hasHandler(player.inventory.getStackInSlot(i))) {
                                    PacketDispatcher.sendTo(new SyncReinforceData(ReinforceHandler.getHandler(player.inventory.getStackInSlot(i)), player.inventory.getStackInSlot(i), i), (EntityPlayerMP) player);
                                }
                            }
                            player.heal(player.getMaxHealth());
                            player.setSpawnPoint(pos, true);
                            player.getCapability(EstusHandler.CAPABILITY_ESTUS, null).setLastRested(te.getID());
                        }
                    } else {
                        player.openGui(Bonfires.instance, GuiHandler.GUI_BONFIRE, world, pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            } else {
                if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
                    if (player.getHeldItemMainhand().getItem() == Bonfires.coiledSword) {
                        placeItem(world, te, pos, player, TileEntityBonfire.BonfireType.BONFIRE);
                    }/*else if (player.getHeldItemMainhand().getItem() == Bonfires.coiledSwordFragment) {
                        placeItem(world, te, pos, playerIn, TileEntityBonfire.BonfireType.PRIMAL);
                    }*/
                }
            }
        }
        world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1);
        return true;
    }

    public void placeItem(World world, TileEntityBonfire te, BlockPos pos, EntityPlayer player, TileEntityBonfire.BonfireType type) {
        if (!world.isRemote) {
            if (!player.capabilities.isCreativeMode)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            te.setBonfire(true);
            te.setLit(false);
            te.setBonfireType(type);
            PacketDispatcher.sendToAllAround(new SyncBonfire(te.isBonfire(), type, te.isLit(), null, te), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(LIT, false), 2);
    }

    @Override
    public int getLightValue(IBlockState state) {
        if (state.getValue(LIT)) {
            return 8;
        } else {
            return 0;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityBonfire te = (TileEntityBonfire) worldIn.getTileEntity(pos);
        if (te != null) {
            if (te.isBonfire()) {
                dropFragment = true;
            }
            if (te.isLit()) {
                te.destroyBonfire(te.getID());
                BonfireWorldSavedData.get(worldIn).bonfires.removeBonfire(te.getID());
                BonfireWorldSavedData.get(worldIn).markDirty();
                PacketDispatcher.sendToAll(new SyncSaveData(BonfireWorldSavedData.get(worldIn).bonfires.getBonfires()));
            }
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (BonfiresConfig.enableUBSBonfire) {
            EntityItem shard = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Bonfires.undeadBoneShard));
            if (!worldIn.isRemote) {
                if (dropFragment) {
                    worldIn.spawnEntity(shard);
                }
            }
        }
        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (source.getTileEntity(pos) != null) {
            if (source.getTileEntity(pos) instanceof TileEntityBonfire) {
                if (((TileEntityBonfire)source.getTileEntity(pos)).isBonfire()) {
                    return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 1.0, 0.8);
                } else {
                    return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.2, 0.8);
                }
            }
        }
        return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.2, 0.8);
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

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.getTileEntity(pos) != null) {
            TileEntityBonfire bonfire = (TileEntityBonfire) worldIn.getTileEntity(pos);
            if (bonfire.isLit()) {
                double d0 = (double)pos.getX() + 0.5D + rand.nextDouble() * 3.0D / 16.0D;
                double d1 = (double)pos.getY() + 0.2D;
                double d2 = (double)pos.getZ() + 0.5D + rand.nextDouble() * 1.0D / 16.0D;
                double d3 = 0.52D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                double d5 = rand.nextDouble() * 0.6D - 0.3D;

                if (rand.nextDouble() < 0.1D) {
                    worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
                }
                //worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            }
        }
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
}
