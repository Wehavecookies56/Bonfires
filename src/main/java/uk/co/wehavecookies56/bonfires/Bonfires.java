package uk.co.wehavecookies56.bonfires;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import uk.co.wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBlock;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBonePile;
import uk.co.wehavecookies56.bonfires.gui.GuiHandler;
import uk.co.wehavecookies56.bonfires.items.*;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.SyncSaveData;
import uk.co.wehavecookies56.bonfires.proxies.CommonProxy;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.Map;
import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */
@SuppressWarnings("unused")
@Mod(modid = Bonfires.modid, name = Bonfires.name, version = Bonfires.version, updateJSON = "https://raw.githubusercontent.com/Wehavecookies56/Bonfires/master/update.json")
public class Bonfires {

    @SidedProxy(clientSide = "uk.co.wehavecookies56.bonfires.proxies.ClientProxy", serverSide = "uk.co.wehavecookies56.bonfires.proxies.CommonProxy")
    public static CommonProxy proxy;

    public static final String modid = "bonfires";
    @SuppressWarnings("WeakerAccess")
    public static final String name = "Bonfires";
    @SuppressWarnings("WeakerAccess")
    public static final String version = "1.1.1";

    @Mod.Instance (modid)
    public static Bonfires instance;

    @GameRegistry.ObjectHolder("bonfires:ash_bone_pile")
    public static Block ashBonePile;

    @GameRegistry.ObjectHolder("bonfires:ash_block")
    public static Block ashBlock;

    @GameRegistry.ObjectHolder("bonfires:ash_pile")
    public static Item ashPile;

    @GameRegistry.ObjectHolder("bonfires:coiled_sword")
    public static Item coiledSword;

    @GameRegistry.ObjectHolder("bonfires:estus_flask")
    public static Item estusFlask;

    @GameRegistry.ObjectHolder("bonfires:homeward_bone")
    public static Item homewardBone;

    @GameRegistry.ObjectHolder("bonfires:coiled_sword_fragment")
    public static Item coiledSwordFragment;

    @GameRegistry.ObjectHolder("bonfires:estus_shard")
    public static Item estusShard;

    public static CreativeTabs tabBonfires;

    public static BonfireLitTrigger TRIGGER_BONFIRE_LIT = new BonfireLitTrigger();

    public static Logger logger;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        PacketDispatcher.registerPackets();
        tabBonfires = new TabBonfires("tabBonfires");
        EstusHandler.init();
        TRIGGER_BONFIRE_LIT = CriteriaTriggers.register(TRIGGER_BONFIRE_LIT);
        proxy.preInit();
    }

    @SubscribeEvent
    public void entityDeath(LivingDropsEvent event) {
        if (event.getSource().isFireDamage() || event.getEntity().isBurning() || (event.getSource().getTrueSource() instanceof EntityPlayer && ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().getItem() == coiledSword)) {
            Random r = new Random();
            double percent = r.nextDouble() * 100;
            if (percent > 65) {
                event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(ashPile)));
            }
        }
    }

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote) {
            if (event.getEntity() instanceof EntityPlayer)
                PacketDispatcher.sendTo(new SyncSaveData(BonfireRegistry.INSTANCE.getBonfires()), (EntityPlayerMP) event.getEntity());
        }
    }

    @SubscribeEvent
    public void quit(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player.world.getMinecraftServer() != null)
            if (!event.player.world.getMinecraftServer().isDedicatedServer()) BonfireRegistry.INSTANCE.clearBonfires();
    }

    @SubscribeEvent
    public void loadWorld(WorldEvent.Load event) {
        BonfireWorldSavedData.get(event.getWorld());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(Bonfires.instance);
        GameRegistry.registerTileEntity(TileEntityBonfire.class, "bonfire");
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        proxy.init();
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public static void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBonfires());
        event.registerServerCommand(new CommandTravel());
    }

    @Mod.EventBusSubscriber(modid = modid)
    private static class Events {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new ItemAshPile("ash_pile"));
            event.getRegistry().register(new ItemCoiledSword("coiled_sword", EnumHelper.addToolMaterial("COILED_SWORD", 3, 105, 3, 10, 0)));
            event.getRegistry().register(new ItemEstusFlask("estus_flask", 0, 0, false));
            event.getRegistry().register(new ItemHomewardBone("homeward_bone"));
            event.getRegistry().register(new ItemCoiledSwordFragment("coiled_sword_fragment"));
            event.getRegistry().register(new ItemEstusShard("estus_shard"));

            if (ashBlock.getRegistryName() != null)
                event.getRegistry().register(new ItemBlock(ashBlock).setRegistryName(ashBlock.getRegistryName()));
            if (ashBonePile.getRegistryName() != null)
                event.getRegistry().register(new ItemBlock(ashBonePile).setRegistryName(ashBonePile.getRegistryName()));
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new BlockAshBlock("ash_block", Material.SAND));
            event.getRegistry().register(new BlockAshBonePile("ash_bone_pile", Material.CIRCUITS));
        }

    }

}
