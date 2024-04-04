package wehavecookies56.bonfires.blocks;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DeleteScreenshot;
import wehavecookies56.bonfires.packets.client.OpenBonfireGUI;
import wehavecookies56.bonfires.packets.client.OpenCreateScreen;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.setup.ItemSetup;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Created by Toby on 05/11/2016.
 */
public class AshBonePileBlock extends Block implements BlockEntityProvider {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.of("lit");

    public static final BooleanProperty EXPLODED = BooleanProperty.of("exploded");

    public AshBonePileBlock() {
        super(AbstractBlock.Settings.create().sounds(BlockSoundGroup.SAND).nonOpaque().strength(0.8F).luminance(AshBonePileBlock::getLightValue));
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(LIT, false).with(EXPLODED, false));
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(LIT, false).with(EXPLODED, false);
    }

    public Optional<Vec3d> getRespawnPosition(BlockPos pos, World world) {
        return Optional.of(BonfireTeleporter.attemptToPlaceNextToBonfire(pos, world));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, LIT, EXPLODED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof BonfireTileEntity te) {
            if (te.isBonfire()) {
                if (!te.isLit()) {
                    if (!world.isClient) {
                        if (BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(te.getID()) == null) {
                            if (!te.hasUnlitName()) {
                                PacketHandler.sendTo(new OpenCreateScreen(te), (ServerPlayerEntity) player);
                                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                            }
                            return ActionResult.SUCCESS;
                        }
                    } else {
                        if (te.hasUnlitName()) {
                            PacketHandler.sendToServer(new LightBonfire(te.getUnlitName(), te, !te.isUnlitPrivate(), Bonfires.CONFIG.client.enableAutomaticScreenshotOnCreation()));
                        }
                        return ActionResult.SUCCESS;
                    }
                } else {
                    if (!world.isClient) {
                        if (te.hasUnlitName()) {
                            te.setUnlitName("");
                            return ActionResult.SUCCESS;
                        }
                        BonfireRegistry registry = BonfireHandler.getServerHandler(world.getServer()).getRegistry();
                        if (registry.getBonfire(te.getID()) != null) {
                            GameProfile profile;
                            Optional<GameProfile> cachedProfile = world.getServer().getUserCache().getByUuid(registry.getBonfire(te.getID()).getOwner());
                            profile = cachedProfile.orElseGet(() -> new GameProfile(registry.getBonfire(te.getID()).getOwner(), "Unknown"));
                            for (int i = 0; i < player.getInventory().main.size(); i++) {
                                if (!ItemStack.areItemsEqual(player.getInventory().getStack(i), ItemStack.EMPTY)) {
                                    if (player.getInventory().getStack(i).getItem() == ItemSetup.estus_flask) {
                                        if (player.getInventory().getStack(i).hasNbt()) {
                                            player.getInventory().getStack(i).getNbt().putInt("estus", player.getInventory().getStack(i).getNbt().getInt("uses"));
                                        }
                                    }
                                }
                            }
                            PacketHandler.sendTo(new OpenBonfireGUI(te, profile.getName(), registry, Bonfires.CONFIG.common.enableReinforcing(), world.getServer()), (ServerPlayerEntity) player);
                            player.heal(player.getMaxHealth());
                            ((ServerPlayerEntity) player).setSpawnPoint(te.getWorld().getRegistryKey(), te.getPos(), player.getYaw(), false, true);
                            EstusHandler.getHandler(player).setLastRested(te.getID());
                            //PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), (ServerPlayerEntity) player);
                        } else {
                            //Bonfire lit but not in data, so should not be lit
                            te.setLit(false);
                        }
                        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                    }
                    return ActionResult.SUCCESS;
                }
            } else {
                if (player.getStackInHand(hand) != ItemStack.EMPTY) {
                    if (player.getStackInHand(hand).getItem() == ItemSetup.coiled_sword) {
                        placeItem(world, te, pos, player, hand, BonfireTileEntity.BonfireType.BONFIRE);
                    }/*else if (player.getHeldItemMainhand().getItem() == Bonfires.coiledSwordFragment) {
                        placeItem(world, te, pos, playerIn, TileEntityBonfire.BonfireType.PRIMAL);
                    }*/
                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                    return ActionResult.PASS;
                }
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onExploded(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        if (state.getBlock() instanceof AshBonePileBlock block) {
            block.wasDestroyedByExplosion(world, pos);
        }
        super.onExploded(state, world, pos, explosion, stackMerger);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        NbtCompound compoundtag = itemStack.getNbt();
        if (compoundtag != null && compoundtag.contains("bonfire_private")) {
            world.setBlockState(pos, state, 2);
            BonfireTileEntity te = new BonfireTileEntity(pos, state);
            te.setBonfire(true);
            te.setLit(false);
            te.setBonfireType(BonfireTileEntity.BonfireType.BONFIRE);
            if (compoundtag.contains("bonfire_name")) {
                te.setUnlitName(compoundtag.getString("bonfire_name"));
                te.setUnlitPrivate(compoundtag.getBoolean("bonfire_private"));
            }
            world.addBlockEntity(te);
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    public void placeItem(World world, BonfireTileEntity te, BlockPos pos, PlayerEntity player, Hand hand, BonfireTileEntity.BonfireType type) {
        if (!world.isClient) {
            if (!player.isCreative())
                player.setStackInHand(hand, ItemStack.EMPTY);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1, 1);
            te.setBonfire(true);
            te.setLit(false);
            te.setBonfireType(type);
            //PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), type, te.isLit(), null, te));
        } else {
            world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1, 1);
        }
    }

    public static int getLightValue(BlockState state) {
        if (state.get(LIT)) {
            return 8;
        } else {
            return 0;
        }
    }

    @Override
    public boolean canMobSpawnInside(BlockState state) {
        return true;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (newState.getBlock() != this) {
            if (!world.isClient()) {
                BonfireTileEntity te = (BonfireTileEntity) world.getBlockEntity(pos);
                if (te != null) {
                    if (te.isBonfire()) {
                        if (!state.get(EXPLODED)) {
                            ItemEntity fragment = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemSetup.coiled_sword_fragment));
                            world.spawnEntity(fragment);
                        }
                    }
                    if (te.isLit()) {
                        MinecraftServer server = world.getServer();
                        Bonfire destroyed = BonfireHandler.getServerHandler(server).getRegistry().getBonfire(te.getID());
                        te.destroyBonfire(te.getID());
                        BonfireHandler.getServerHandler(server).removeBonfire(te.getID());
                        //PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getServerHandler(server).getRegistry().getBonfires()));
                        PacketHandler.sendToAll(new SendBonfiresToClient(server), server);
                        PacketHandler.sendToAll(new DeleteScreenshot(te.getID(), destroyed.getName()), server);
                    }
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    public void wasDestroyedByExplosion(World world, BlockPos pos) {
        if (Bonfires.CONFIG.common.enableUBSBonfire()) {
            if (!world.isClient) {
                ItemEntity shard = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemSetup.undead_bone_shard));
                world.spawnEntity(shard);
                BlockState state = world.getBlockState(pos).with(EXPLODED, true);
                world.setBlockState(pos, state, 3);
            }
        }
    }



    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape base = VoxelShapes.combine(Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0, 13.0D), Block.createCuboidShape(4.0D, 1.0D, 4.0D, 12.0D, 2.0, 12.0D), BooleanBiFunction.OR);
        VoxelShape sword = Block.createCuboidShape(5, 2, 5, 11, 20, 11);
        VoxelShape combined = VoxelShapes.combine(base, sword, BooleanBiFunction.OR);
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
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state, world, pos, context);
    }

    @Override
    public boolean hasDynamicBounds() {
        return true;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
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
                    world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
                }
                //worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.05D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
            }
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        if(stack.getNbt() != null) {
            NbtCompound tag = stack.getNbt();
            if (tag.contains("bonfire_private")) {
                MutableText text = Text.translatable(LocalStrings.TOOLTIP_UNLIT);
                if (tag.contains("bonfire_name")) {
                    text.append(" ");
                    text.append(Text.translatable(tag.getString("bonfire_name")));
                }
                if (tag.getBoolean("bonfire_private")) {
                    text.append(" ");
                    text.append(Text.translatable(LocalStrings.TEXT_PRIVATE));
                }
                tooltip.add(text);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BonfireTileEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> BonfireTileEntity.tick(world1, pos, state1, (BonfireTileEntity) blockEntity);
    }
}
