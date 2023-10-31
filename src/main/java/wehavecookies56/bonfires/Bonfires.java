package wehavecookies56.bonfires;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SyncEstusData;
import wehavecookies56.bonfires.packets.client.SyncSaveData;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.CommonSetup;
import wehavecookies56.bonfires.setup.EntitySetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */
@Mod("bonfires")
public class Bonfires {
    public static Logger LOGGER = LogManager.getLogger();
    public static final String modid = "bonfires";

    public Bonfires() {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockSetup.BLOCKS.register(modEventBus);
        ItemSetup.ITEMS.register(modEventBus);
        EntitySetup.TILE_ENTITIES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BonfiresConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BonfiresConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BonfiresConfig.SERVER_SPEC);

        MinecraftForge.EVENT_BUS.register(new CommonSetup());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void entityDeath(LivingDropsEvent event) {
        if (event.getSource().isFire() || event.getEntity().isOnFire() || (event.getSource().getEntity() instanceof Player && ((Player) event.getSource().getEntity()).getMainHandItem().getItem() == ItemSetup.coiled_sword.get())) {
            Random r = new Random();
            double percent = r.nextDouble() * 100;
            if (percent > 65) {
                event.getDrops().add(new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ItemSetup.ash_pile.get())));
            }
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide) {
            if (event.getEntity() instanceof ServerPlayer player) {
                PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(event.getWorld().getServer()).getRegistry().getBonfires()), player);
                PacketHandler.sendTo(new SyncEstusData(EstusHandler.getHandler(player)), player);
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(event.getPlayer().getServer()).getRegistry().getBonfires()), (ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public void livingHurt(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player) {
            if (ReinforceHandler.canReinforce(player.getMainHandItem())) {
                ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(player.getMainHandItem());
                if (rlevel != null) {
                    event.setAmount((float) ((event.getAmount() + (BonfiresConfig.Server.reinforceDamagePerLevel * rlevel.level())) * player.getAttackStrengthScale(0)));
                    System.out.println(event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        BonfiresCommand.register(dispatcher);
        TravelCommand.register(dispatcher);
    }
}
