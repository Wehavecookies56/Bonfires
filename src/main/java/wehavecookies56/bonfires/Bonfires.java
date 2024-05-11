package wehavecookies56.bonfires;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SyncEstusData;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.CreativeTabSetup;
import wehavecookies56.bonfires.setup.EntitySetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */
@Mod("bonfires")
public class Bonfires {
    public static Logger LOGGER = LogManager.getLogger();
    public static final String modid = "bonfires";

    public static final UUID reinforceDamageModifier = UUID.fromString("117e876c-c9bd-4898-985a-2ecb24198350");

    public Bonfires(IEventBus modEventBus) {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();

        BlockSetup.BLOCKS.register(modEventBus);
        ItemSetup.ITEMS.register(modEventBus);
        EntitySetup.TILE_ENTITIES.register(modEventBus);
        CreativeTabSetup.TABS.register(modEventBus);
        EstusHandler.ATTACHMENT_TYPES.register(modEventBus);
        BonfireLitTrigger.CRITERION_TRIGGERS.register(modEventBus);

        modEventBus.addListener(PacketHandler::register);

        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, BonfiresConfig.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, BonfiresConfig.COMMON_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, BonfiresConfig.SERVER_SPEC);

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void entityDeath(LivingDropsEvent event) {
        if (event.getSource().is(DamageTypes.IN_FIRE) || event.getEntity().isOnFire() || (event.getSource().getEntity() instanceof Player && ((Player) event.getSource().getEntity()).getMainHandItem().getItem() == ItemSetup.coiled_sword.get())) {
            Random r = new Random();
            double percent = r.nextDouble() * 100;
            if (percent > 65) {
                event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ItemSetup.ash_pile.get())));
            }
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide) {
            if (event.getEntity() instanceof ServerPlayer player) {
                PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), player);
            }
        }
    }

    @SubscribeEvent
    public void modifyAttributes(ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND && event.getItemStack().getItem() != ItemSetup.estus_flask.get()) {
            if (ReinforceHandler.canReinforce(event.getItemStack())) {
                ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(event.getItemStack());
                if (rlevel != null && rlevel.level() != 0) {
                    event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(reinforceDamageModifier, "reinforce_damagebonus", BonfiresConfig.Server.reinforceDamagePerLevel * rlevel.level(), AttributeModifier.Operation.ADDITION));
                }
            }
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
}
