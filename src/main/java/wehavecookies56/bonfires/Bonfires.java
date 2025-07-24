package wehavecookies56.bonfires;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SyncDiscoveryData;
import wehavecookies56.bonfires.packets.client.SyncEstusData;
import wehavecookies56.bonfires.setup.*;

import java.util.*;
import java.util.function.Supplier;

@Mod("bonfires")
public class Bonfires {
    public static Logger LOGGER = LogManager.getLogger();
    public static final String modid = "bonfires";

    public static final ResourceLocation reinforceDamageModifier = ResourceLocation.fromNamespaceAndPath(modid, "reinforce_damagebonus");

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Bonfires.modid);
    public static final Supplier<AttachmentType<EstusHandler.EstusHandlerInstance>> ESTUS = Bonfires.ATTACHMENT_TYPES.register("estus", () -> AttachmentType.serializable(() -> new EstusHandler.EstusHandlerInstance(null)).copyOnDeath().build());
    public static final Supplier<AttachmentType<DiscoveryHandler.DiscoveryHandlerInstance>> DISCOVERY = Bonfires.ATTACHMENT_TYPES.register("discovery", () -> AttachmentType.serializable(DiscoveryHandler.DiscoveryHandlerInstance::new).copyOnDeath().build());

    public Bonfires(IEventBus modEventBus) {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();

        BlockSetup.BLOCKS.register(modEventBus);
        ItemSetup.ITEMS.register(modEventBus);
        EntitySetup.TILE_ENTITIES.register(modEventBus);
        CreativeTabSetup.TABS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        BonfireLitTrigger.CRITERION_TRIGGERS.register(modEventBus);
        ComponentSetup.COMPONENTS.register(modEventBus);

        modEventBus.addListener(PacketHandler::register);

        modLoadingContext.getActiveContainer().registerConfig(ModConfig.Type.CLIENT, BonfiresConfig.CLIENT_SPEC);
        modLoadingContext.getActiveContainer().registerConfig(ModConfig.Type.COMMON, BonfiresConfig.COMMON_SPEC);
        modLoadingContext.getActiveContainer().registerConfig(ModConfig.Type.SERVER, BonfiresConfig.SERVER_SPEC);

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void entityDeath(LivingDropsEvent event) {
        if (!BonfiresConfig.Common.disableAshDrops) {
            if (event.getSource().is(DamageTypes.IN_FIRE) || event.getEntity().isOnFire() || (event.getSource().getEntity() instanceof Player && ((Player) event.getSource().getEntity()).getMainHandItem().getItem() == ItemSetup.coiled_sword.get())) {
                Random r = new Random();
                double percent = r.nextDouble() * 100;
                if (percent > 65) {
                    event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ItemSetup.ash_pile.get())));
                }
            }
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide) {
            if (event.getEntity() instanceof ServerPlayer player) {
                PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), player);
                PacketHandler.sendTo(new SyncDiscoveryData(DiscoveryHandler.getHandler(player), player), player);
                if (DiscoveryHandler.getHandler(player).getDiscovered().isEmpty()) {
                    //No discovered bonfires, could potentially be an old world so add all (if any) Bonfires created by the player to discovered
                    BonfireRegistry registry = BonfireHandler.getServerHandler(event.getLevel().getServer()).getRegistry();
                    DiscoveryHandler.IDiscoveryHandler discoveryHandler = DiscoveryHandler.getHandler(player);
                    List<Bonfire> bonfires = registry.getBonfiresByOwner(player.getUUID());
                    bonfires.forEach(bonfire -> discoveryHandler.setDiscovered(bonfire.getId(), bonfire.getTimeCreated()));
                }
            }
        }
    }

    @SubscribeEvent
    public void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.isEndConquered()) {
            event.getEntity().getInventory().items.forEach(stack -> {
                if (stack.is(ItemSetup.estus_flask.get())) {
                    if (stack.has(ComponentSetup.ESTUS)) {
                        EstusFlaskItem.Estus estus = stack.get(ComponentSetup.ESTUS);
                        stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.maxUses(), estus.maxUses()));
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void serverStart(ServerStartingEvent event) {
        BonfireHandler handler = BonfireHandler.getServerHandler(event.getServer());
        handler.loadOldBonfireData(event.getServer());
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        BonfiresCommand.register(dispatcher);
        TravelCommand.register(dispatcher);
    }

    @SubscribeEvent
    public void modifyAttributes(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() != ItemSetup.estus_flask) {
            if (ReinforceHandler.canReinforce(event.getItemStack())) {
                ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(event.getItemStack());
                if (rlevel != null && rlevel.level() != 0) {
                    event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(reinforceDamageModifier, BonfiresConfig.Server.reinforceDamagePerLevel * rlevel.level(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                }
            }
        }
    }

    public static final StreamCodec<FriendlyByteBuf, UUID> NULLABLE_UUID = new StreamCodec<>() {
        @Override
        public UUID decode(FriendlyByteBuf byteBuf) {
            if (byteBuf.readBoolean()) {
                return UUIDUtil.STREAM_CODEC.decode(byteBuf);
            }
            return null;
        }

        @Override
        public void encode(FriendlyByteBuf byteBuf, UUID uuid) {
            byteBuf.writeBoolean(uuid != null);
            if (uuid != null) {
                UUIDUtil.STREAM_CODEC.encode(byteBuf, uuid);
            }
        }
    };

    public static final StreamCodec<FriendlyByteBuf, Map<UUID, String>> OWNER_NAMES = new StreamCodec<>() {
        @Override
        public Map<UUID, String> decode(FriendlyByteBuf buf) {
            CompoundTag owners = buf.readNbt();
            Map<UUID, String> ownerNames = new HashMap<>();
            owners.getAllKeys().forEach(s -> {
                ownerNames.put(UUID.fromString(s), owners.getString(s));
            });
            return ownerNames;
        }

        @Override
        public void encode(FriendlyByteBuf buf, Map<UUID, String> ownerNames) {
            CompoundTag owners = new CompoundTag();
            ownerNames.forEach((uuid, s) -> owners.putString(uuid.toString(), s));
            buf.writeNbt(owners);
        }
    };
}
