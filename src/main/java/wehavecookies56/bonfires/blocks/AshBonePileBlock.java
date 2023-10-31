package wehavecookies56.bonfires.blocks;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.setup.EntitySetup;
import wehavecookies56.bonfires.setup.ItemSetup;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */
public class AshBonePileBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty EXPLODED = BooleanProperty.create("exploded");

    public AshBonePileBlock() {
        super(BlockBehaviour.Properties.of(Material.SAND).sound(SoundType.SAND).noOcclusion().strength(0.8F).lightLevel(AshBonePileBlock::getLightValue));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(EXPLODED, false));
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, false).setValue(EXPLODED, false);
    }


    @Override
    public Optional<Vec3> getRespawnPosition(BlockState state, EntityType<?> type, LevelReader world, BlockPos pos, float orientation, @Nullable LivingEntity entity) {
        return Optional.of(BonfireTeleporter.attemptToPlaceNextToBonfire(pos, (Level) world));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING, LIT, EXPLODED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof BonfireTileEntity te) {
            if (te.isBonfire()) {
                if (!te.isLit()) {
                    if (!world.isClientSide) {
                        if (BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(te.getID()) == null) {
                            PacketHandler.sendTo(new OpenCreateScreen(te), (ServerPlayer) player);
                            world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    if (!world.isClientSide) {
                        BonfireRegistry registry = BonfireHandler.getServerHandler(world.getServer()).getRegistry();
                        if (registry.getBonfire(te.getID()) != null) {
                            GameProfile profile = world.getServer().getProfileCache().get(registry.getBonfire(te.getID()).getOwner()).get();
                            for (int i = 0; i < player.getInventory().items.size(); i++) {
                                if (!ItemStack.isSame(player.getInventory().getItem(i), ItemStack.EMPTY)) {
                                    if (player.getInventory().getItem(i).getItem() == ItemSetup.estus_flask.get()) {
                                        if (player.getInventory().getItem(i).hasTag()) {
                                            player.getInventory().getItem(i).getTag().putInt("estus", player.getInventory().getItem(i).getTag().getInt("uses"));
                                        }
                                    }
                                }
                                if (ReinforceHandler.hasHandler(player.getInventory().getItem(i))) {
                                    PacketHandler.sendTo(new SyncReinforceData(ReinforceHandler.getHandler(player.getInventory().getItem(i)), i), (ServerPlayer) player);
                                }
                            }
                            PacketHandler.sendTo(new OpenBonfireGUI(te, profile.getName(), registry, BonfiresConfig.Common.enableReinforcing), (ServerPlayer) player);
                            player.heal(player.getMaxHealth());
                            ((ServerPlayer) player).setRespawnPosition(te.getLevel().dimension(), te.getBlockPos(), player.getYRot(), false, true);
                            EstusHandler.getHandler(player).setLastRested(te.getID());
                            PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), (ServerPlayer) player);
                            world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                            return InteractionResult.SUCCESS;
                        } else {
                            //Bonfire lit but not in data, so should not be lit
                            te.setLit(false);
                            world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (player.getItemInHand(hand) != ItemStack.EMPTY) {
                    if (player.getItemInHand(hand).getItem() == ItemSetup.coiled_sword.get()) {
                        placeItem(world, te, pos, player, hand, BonfireTileEntity.BonfireType.BONFIRE);
                    }/*else if (player.getHeldItemMainhand().getItem() == Bonfires.coiledSwordFragment) {
                        placeItem(world, te, pos, playerIn, TileEntityBonfire.BonfireType.PRIMAL);
                    }*/
                    world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                    return InteractionResult.PASS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    public void placeItem(Level world, BonfireTileEntity te, BlockPos pos, Player player, InteractionHand hand, BonfireTileEntity.BonfireType type) {
        if (!world.isClientSide) {
            if (!player.isCreative())
                player.setItemInHand(hand, ItemStack.EMPTY);
            world.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1, 1);
            te.setBonfire(true);
            te.setLit(false);
            te.setBonfireType(type);
            PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), type, te.isLit(), null, te));
        } else {
            world.playSound(player, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1, 1);
        }
    }

    public static int getLightValue(BlockState state) {
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
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
        if (newState.getBlock() != this) {
            if (!world.isClientSide()) {
                BonfireTileEntity te = (BonfireTileEntity) world.getBlockEntity(pos);
                if (te != null) {
                    if (te.isBonfire()) {
                        if (!state.getValue(EXPLODED)) {
                            ItemEntity fragment = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemSetup.coiled_sword_fragment.get()));
                            world.addFreshEntity(fragment);
                        }
                    }
                    if (te.isLit()) {
                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                        Bonfire destroyed = BonfireHandler.getServerHandler(server).getRegistry().getBonfire(te.getID());
                        te.destroyBonfire(te.getID());
                        BonfireHandler.getServerHandler(server).removeBonfire(te.getID());
                        PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getServerHandler(server).getRegistry().getBonfires()));
                        PacketHandler.sendToAll(new SendBonfiresToClient());
                        PacketHandler.sendToAll(new DeleteScreenshot(te.getID(), destroyed.getName()));
                    }
                }
            }
        }
        super.onRemove(state, world, pos, newState, moving);
    }



    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if (BonfiresConfig.Common.enableUBSBonfire) {
            if (!world.isClientSide) {
                ItemEntity shard = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemSetup.undead_bone_shard.get()));
                world.addFreshEntity(shard);
                state = state.setValue(EXPLODED, true);
                world.setBlock(pos, state, 3);
            }
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape base = Shapes.join(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0, 13.0D), Block.box(4.0D, 1.0D, 4.0D, 12.0D, 2.0, 12.0D), BooleanOp.OR);
        VoxelShape sword = Block.box(5, 2, 5, 11, 20, 11);
        VoxelShape combined = Shapes.join(base, sword, BooleanOp.OR);
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
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
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
                    world.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F, 1.0F, false);
                }
                //worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.05D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            }
        }
        super.animateTick(state, world, pos, random);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return EntitySetup.BONFIRE.get().create(pPos, pState);
    }


}
