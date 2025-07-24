package wehavecookies56.bonfires;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
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
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.CreativeTabSetup;
import wehavecookies56.bonfires.setup.EntitySetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        BonfireLitTrigger.INSTANCE = Criteria.register(new BonfireLitTrigger());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BonfiresCommand.register(dispatcher);
            TravelCommand.register(dispatcher);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            BonfireHandler.getServerHandler(server).loadOldBonfireData(server);
        });
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (!Bonfires.CONFIG.common.disableAshDrops()) {
                if (damageSource.isOf(DamageTypes.IN_FIRE) || entity.isOnFire() || damageSource.getSource() instanceof PlayerEntity && ((PlayerEntity) damageSource.getSource()).getMainHandStack().getItem() == ItemSetup.coiled_sword) {
                    Random r = new Random();
                    double percent = r.nextDouble() * 100;
                    if (percent > 65) {
                        entity.dropStack(new ItemStack(ItemSetup.ash_pile));
                    }
                }
            }
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
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (DiscoveryHandler.getHandler(handler.player).getDiscovered().isEmpty()) {
                BonfireRegistry registry = BonfireHandler.getServerHandler(server).getRegistry();
                DiscoveryHandler.IDiscoveryHandler discoveryHandler = DiscoveryHandler.getHandler(handler.player);
                List<Bonfire> bonfires = registry.getBonfiresByOwner(handler.player.getUuid());
                bonfires.forEach(bonfire -> discoveryHandler.setDiscovered(bonfire.getId(), bonfire.getTimeCreated()));
            }
        });
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (!alive) {
                newPlayer.getInventory().main.forEach(stack -> {
                    if (stack.isOf(ItemSetup.estus_flask)) {
                        if (stack.getNbt() != null) {
                            stack.getNbt().putInt("estus", stack.getNbt().getInt("uses"));
                        }
                    }
                });
            }
        });
        PacketHandler.init();
    }

}
