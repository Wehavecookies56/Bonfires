package uk.co.wehavecookies56.bonfires;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
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
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBlock;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBonePile;
import uk.co.wehavecookies56.bonfires.gui.GuiHandler;
import uk.co.wehavecookies56.bonfires.items.*;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.SyncSaveData;
import uk.co.wehavecookies56.bonfires.proxies.CommonProxy;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */
@Mod(modid = Bonfires.modid, name = Bonfires.name, version = Bonfires.version, updateJSON = "https://raw.githubusercontent.com/Wehavecookies56/Bonfires/master/update.json")
public class Bonfires {

    @SidedProxy(clientSide = "uk.co.wehavecookies56.bonfires.proxies.ClientProxy", serverSide = "uk.co.wehavecookies56.bonfires.proxies.CommonProxy")
    public static CommonProxy proxy;

    public static final String modid = "bonfires", name = "Bonfires", version = "1.0";

    @Mod.Instance (modid)
    public static Bonfires instance;

    public static Block ashBonePile, ashBlock;
    public static Block[] blocks;

    public static Item ashPile, coiledSword, estusFlask, homewardBone, coiledSwordFragment, estusShard;
    public static Item[] items;

    public static CreativeTabs tabBonfires;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        PacketDispatcher.registerPackets();
        BonfiresConfig.init(event.getSuggestedConfigurationFile());
        tabBonfires = new TabBonfires("tabBonfires");
        blocks = new Block[] {
                ashBlock = new BlockAshBlock(Material.SAND).setRegistryName(modid, "ash_block").setUnlocalizedName("ash_block"),
                ashBonePile = new BlockAshBonePile(Material.SNOW).setRegistryName(modid, "ash_bone_pile").setUnlocalizedName("ash_bone_pile")
        };
        items = new Item[] {
                ashPile = new ItemAshPile().setRegistryName(modid, "ash_pile").setUnlocalizedName("ash_pile"),
                coiledSword = new ItemCoiledSword(EnumHelper.addToolMaterial("COILED_SWORD", 3, 105, 3, 10, 0)).setRegistryName(modid, "coiled_sword").setUnlocalizedName("coiled_sword"),
                estusFlask = new ItemEstusFlask(0, 0, false).setRegistryName(modid, "estus_flask").setUnlocalizedName("estus_flask"),
                homewardBone = new ItemHomewardBone().setRegistryName(modid, "homeward_bone").setUnlocalizedName("homeward_bone"),
                coiledSwordFragment = new ItemCoiledSwordFragment().setRegistryName(modid, "coiled_sword_fragment").setUnlocalizedName("coiled_sword_fragment"),
                estusShard = new ItemEstusShard().setRegistryName(modid, "estus_shard").setUnlocalizedName("estus_shard")
        };
        for (Block b : blocks) {
            b.setCreativeTab(tabBonfires);
            registerBlock(b);
        }
        for (Item i : items) {
            i.setCreativeTab(tabBonfires);
            registerItem(i);
        }
        EstusHandler.init();
        proxy.preInit();
    }

    public static void registerItem(Item item) {
        GameRegistry.register(item);
    }

    public static void registerBlock(Block block) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void registerRender(Item item, int meta, String name) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(modid + ":" + name, "inventory"));
    }

    @SubscribeEvent
    public void entityDeath(LivingDropsEvent event) {
        if (event.getSource().isFireDamage()) {
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

        GameRegistry.addShapelessRecipe(new ItemStack(homewardBone), Items.BONE, Items.ENDER_PEARL, Items.BLAZE_ROD);
        GameRegistry.addShapelessRecipe(new ItemStack(ashBlock), ashPile, ashPile, ashPile, ashPile, ashPile, ashPile, ashPile, ashPile, ashPile);
        GameRegistry.addShapelessRecipe(new ItemStack(ashPile, 9), ashBlock);
        ItemStack eF = new ItemStack(estusFlask);
        eF.setTagCompound(new NBTTagCompound());
        eF.getTagCompound().setInteger("estus", 0);
        eF.getTagCompound().setInteger("uses", 3);
        GameRegistry.addShapelessRecipe(eF, Items.GLASS_BOTTLE, estusShard, estusShard, estusShard);
        GameRegistry.addShapedRecipe(new ItemStack(coiledSword), "OLO", "FSF", "OAO", 'O', Blocks.OBSIDIAN, 'L', Items.LAVA_BUCKET, 'F', Items.FIRE_CHARGE, 'S', Items.DIAMOND_SWORD, 'A', ashPile);
        GameRegistry.addShapedRecipe(new ItemStack(ashBonePile), "BBB", "AAA", 'B', homewardBone, 'A', ashPile);
        GameRegistry.addShapelessRecipe(new ItemStack(estusShard), Items.GOLD_NUGGET, Items.DIAMOND, Items.BLAZE_POWDER, Items.GOLDEN_APPLE);
        GameRegistry.addShapelessRecipe(new ItemStack(coiledSword), Items.IRON_SWORD, coiledSwordFragment, Items.LAVA_BUCKET);
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

}
