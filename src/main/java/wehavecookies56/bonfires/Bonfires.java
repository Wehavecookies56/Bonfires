package wehavecookies56.bonfires;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.CreativeTabSetup;
import wehavecookies56.bonfires.setup.EntitySetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */
public class Bonfires implements ModInitializer {
    public static final String modid = "bonfires";
    public static Logger LOGGER = LoggerFactory.getLogger(modid);

    public static final Identifier BONFIRES_LIT = new Identifier(modid, "bonfires_lit");
    public static final Identifier TIMES_TRAVELLED = new Identifier(modid, "times_travelled");

    public static final UUID reinforceDamageModifier = UUID.fromString("117e876c-c9bd-4898-985a-2ecb24198350");

    public static final wehavecookies56.bonfires.BonfiresMainConfig CONFIG = wehavecookies56.bonfires.BonfiresMainConfig.createAndLoad();

    @Override
    public void onInitialize() {
        BlockSetup.init();
        ItemSetup.init();
        EntitySetup.init();
        CreativeTabSetup.init();
        Registry.register(Registries.CUSTOM_STAT, BONFIRES_LIT.getPath(), BONFIRES_LIT);
        Registry.register(Registries.CUSTOM_STAT, TIMES_TRAVELLED.getPath(), TIMES_TRAVELLED);
        Stats.CUSTOM.getOrCreateStat(BONFIRES_LIT, StatFormatter.DEFAULT);
        Stats.CUSTOM.getOrCreateStat(TIMES_TRAVELLED, StatFormatter.DEFAULT);
        BonfireLitTrigger.INSTANCE = Criteria.register(BonfireLitTrigger.ID.toString(), new BonfireLitTrigger());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BonfiresCommand.register(dispatcher);
            TravelCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            BonfireHandler.getServerHandler(server).loadOldBonfireData(server);
        });
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (damageSource.isOf(DamageTypes.IN_FIRE) || entity.isOnFire() || damageSource.getSource() instanceof PlayerEntity && ((PlayerEntity) damageSource.getSource()).getMainHandStack().getItem() == ItemSetup.coiled_sword) {
                Random r = new Random();
                double percent = r.nextDouble() * 100;
                if (percent > 65) {
                    entity.dropStack(new ItemStack(ItemSetup.ash_pile));
                }
            }
        });
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (!handler.getPlayer().getServerWorld().isClient) {
                //PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(event.getLevel().getServer()).getRegistry().getBonfires()), player);
                //PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), player);
            }
        }));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            //PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(event.getEntity().getServer()).getRegistry().getBonfires()), (ServerPlayer) event.getEntity());
        });
        ModifyItemAttributeModifiersCallback.EVENT.register((stack, slot, attributeModifiers) -> {
            if (slot == EquipmentSlot.MAINHAND && stack.getItem() != ItemSetup.estus_flask) {
                if (ReinforceHandler.canReinforce(stack)) {
                    ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(stack);
                    if (rlevel != null && rlevel.level() != 0) {
                        attributeModifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(reinforceDamageModifier, "reinforce_damagebonus", Bonfires.CONFIG.common.reinforceDamagePerLevel() * rlevel.level(), EntityAttributeModifier.Operation.ADDITION));
                    }
                }
            }
        });
        PacketHandler.init();
    }

    /*
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        BonfiresCommand.register(dispatcher);
        TravelCommand.register(dispatcher);
    }

     */
}
