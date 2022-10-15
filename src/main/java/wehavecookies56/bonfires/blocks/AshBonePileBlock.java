package wehavecookies56.bonfires.blocks;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.*;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.*;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.setup.ItemSetup;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */
public class AshBonePileBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    boolean dropFragment = false;

    public AshBonePileBlock() {
        super(AbstractBlock.Properties.of(Material.SAND).sound(SoundType.SAND).noOcclusion().strength(0.8F));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, false);
    }

    @Override
    public Optional<Vector3d> getRespawnPosition(BlockState state, EntityType<?> type, IWorldReader world, BlockPos pos, float orientation, @Nullable LivingEntity entity) {
        return Optional.of(BonfireTeleporter.attemptToPlaceNextToBonfire(pos, (World) world));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING, LIT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.getBlockEntity(pos) instanceof BonfireTileEntity) {
            BonfireTileEntity te = (BonfireTileEntity) world.getBlockEntity(pos);
            if (te.isBonfire()) {
                if (!te.isLit()) {
                    if (!world.isClientSide) {
                        if (BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(te.getID()) == null) {
                            PacketHandler.sendTo(new OpenCreateScreen(te), (ServerPlayerEntity) player);
                            world.sendBlockUpdated(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
                            return ActionResultType.SUCCESS;
                        }
                    } else {
                        return ActionResultType.SUCCESS;
                    }
                } else {
                    if (!world.isClientSide) {
                        BonfireRegistry registry = BonfireHandler.getServerHandler(world.getServer()).getRegistry();
                        if (registry.getBonfire(te.getID()) != null) {
                            GameProfile profile = world.getServer().getProfileCache().get(registry.getBonfire(te.getID()).getOwner());
                            for (int i = 0; i < player.inventory.items.size(); i++) {
                                if (!ItemStack.isSame(player.inventory.getItem(i), ItemStack.EMPTY)) {
                                    if (player.inventory.getItem(i).getItem() == ItemSetup.estus_flask.get()) {
                                        if (player.inventory.getItem(i).hasTag()) {
                                            player.inventory.getItem(i).getTag().putInt("estus", player.inventory.getItem(i).getTag().getInt("uses"));
                                        }
                                    }
                                }
                                if (ReinforceHandler.hasHandler(player.inventory.getItem(i))) {
                                    PacketHandler.sendTo(new SyncReinforceData(ReinforceHandler.getHandler(player.inventory.getItem(i)), i), (ServerPlayerEntity) player);
                                }
                            }
                            PacketHandler.sendTo(new OpenBonfireGUI(te, profile.getName(), registry, BonfiresConfig.Common.enableReinforcing), (ServerPlayerEntity) player);
                            player.heal(player.getMaxHealth());
                            ((ServerPlayerEntity) player).setRespawnPosition(te.getLevel().dimension(), te.getBlockPos(), player.yRot, false, true);
                            EstusHandler.getHandler(player).setLastRested(te.getID());
                            PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), (ServerPlayerEntity) player);
                            world.sendBlockUpdated(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
                            return ActionResultType.SUCCESS;
                        }
                    } else {
                        return ActionResultType.SUCCESS;
                    }
                }
            } else {
                if (player.getItemInHand(hand) != ItemStack.EMPTY) {
                    if (player.getItemInHand(hand).getItem() == ItemSetup.coiled_sword.get()) {
                        placeItem(world, te, pos, player, hand, BonfireTileEntity.BonfireType.BONFIRE);
                    }/*else if (player.getHeldItemMainhand().getItem() == Bonfires.coiledSwordFragment) {
                        placeItem(world, te, pos, playerIn, TileEntityBonfire.BonfireType.PRIMAL);
                    }*/
                    world.sendBlockUpdated(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
                    return ActionResultType.PASS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    public void placeItem(World world, BonfireTileEntity te, BlockPos pos, PlayerEntity player, Hand hand, BonfireTileEntity.BonfireType type) {
        if (!world.isClientSide) {
            if (!player.isCreative())
                player.setItemInHand(hand, ItemStack.EMPTY);
            world.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundCategory.BLOCKS, 1, 1);
            te.setBonfire(true);
            te.setLit(false);
            te.setBonfireType(type);
            PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), type, te.isLit(), null, te));
        } else {
            world.playSound(player, pos, SoundEvents.ANVIL_PLACE, SoundCategory.BLOCKS, 1, 1);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.getValue(LIT)) {
            return 8;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return true;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
        if (newState.getBlock().is(Blocks.AIR)) {
            if (!world.isClientSide()) {
                BonfireTileEntity te = (BonfireTileEntity) world.getBlockEntity(pos);
                if (te != null) {
                    if (te.isBonfire()) {
                        dropFragment = true;
                        ItemEntity fragment = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemSetup.coiled_sword_fragment.get()));
                        world.addFreshEntity(fragment);
                    }
                    if (te.isLit()) {
                        te.destroyBonfire(te.getID());
                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                        BonfireHandler.getServerHandler(server).removeBonfire(te.getID());
                        PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getServerHandler(server).getRegistry().getBonfires()));
                    }
                }
            }
        }
        super.onRemove(state, world, pos, newState, moving);
    }



    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if (BonfiresConfig.Common.enableUBSBonfire) {
            if (!world.isClientSide) {
                if (dropFragment) {
                    ItemEntity shard = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemSetup.undead_bone_shard.get()));
                    world.addFreshEntity(shard);
                }
            }
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return getShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape base = VoxelShapes.join(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0, 13.0D), Block.box(4.0D, 1.0D, 4.0D, 12.0D, 2.0, 12.0D), IBooleanFunction.OR);
        VoxelShape sword = Block.box(5, 2, 5, 11, 20, 11);
        VoxelShape combined = VoxelShapes.join(base, sword, IBooleanFunction.OR);
        if (world.getBlockEntity(pos) != null) {
            if (world.getBlockEntity(pos) instanceof BonfireTileEntity) {
                if (((BonfireTileEntity)world.getBlockEntity(pos)).isBonfire()) {
                    return combined;
                }
            }
        }
        return base;
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_) {
        return super.getVisualShape(p_230322_1_, p_230322_2_, p_230322_3_, p_230322_4_);
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) != null) {
            BonfireTileEntity bonfire = (BonfireTileEntity) world.getBlockEntity(pos);
            if (bonfire.isLit()) {
                double d0 = (double)pos.getX() + 0.5D + random.nextDouble() * 3.0D / 16.0D;
                double d1 = (double)pos.getY() + 0.2D;
                double d2 = (double)pos.getZ() + 0.5D + random.nextDouble() * 1.0D / 16.0D;
                double d3 = 0.52D;
                double d4 = random.nextDouble() * 0.6D - 0.3D;
                double d5 = random.nextDouble() * 0.6D - 0.3D;

                if (random.nextDouble() < 0.1D) {
                    world.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
                }
                //worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.05D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            }
        }
        super.animateTick(state, world, pos, random);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BonfireTileEntity();
    }
}
