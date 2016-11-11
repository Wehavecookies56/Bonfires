package uk.co.wehavecookies56.bonfires;

import com.mojang.authlib.GameProfile;
import net.ilexiconn.llibrary.server.command.Command;
import net.ilexiconn.llibrary.server.command.CommandHandler;
import net.ilexiconn.llibrary.server.command.ICommandExecutor;
import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBonePile;
import uk.co.wehavecookies56.bonfires.blocks.BlockBonfire;
import uk.co.wehavecookies56.bonfires.gui.GuiHandler;
import uk.co.wehavecookies56.bonfires.items.ItemAshPile;
import uk.co.wehavecookies56.bonfires.items.ItemCoiledSword;
import uk.co.wehavecookies56.bonfires.items.ItemEstusFlask;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.packets.SyncSaveData;
import uk.co.wehavecookies56.bonfires.proxies.CommonProxy;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.List;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */
@Mod(modid = Bonfires.modid, name = Bonfires.name, version = Bonfires.version, dependencies = "required-after:llibrary@[1.7.1]")
public class Bonfires {

    @SidedProxy(clientSide = "uk.co.wehavecookies56.bonfires.proxies.ClientProxy", serverSide = "uk.co.wehavecookies56.bonfires.proxies.CommonProxy")
    public static CommonProxy proxy;

    public static final String modid = "bonfires", name = "Bonfires", version = "1.0";

    @Mod.Instance (modid)
    public static Bonfires instance;

    public static Block bonfire, ashBonePile;
    public static Block[] blocks;

    public static Item ashPile, coiledSword, estusFlask;
    public static Item[] items;

    public static CreativeTabs tabBonfires;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        PacketDispatcher.registerPackets();
        tabBonfires = new TabBonfires("tabBonfires");
        blocks = new Block[] {
                bonfire = new BlockBonfire(Material.IRON).setRegistryName(modid, "bonfire").setUnlocalizedName("bonfire"),
                ashBonePile = new BlockAshBonePile(Material.SNOW).setRegistryName(modid, "ash_bone_pile").setUnlocalizedName("ash_bone_pile")
        };
        items = new Item[] {
                ashPile = new ItemAshPile().setRegistryName(modid, "ash_pile").setUnlocalizedName("ash_pile"),
                coiledSword = new ItemCoiledSword(EnumHelper.addToolMaterial("COILED_SWORD", 3, 105, 3, 10, 0)).setRegistryName(modid, "coiled_sword").setUnlocalizedName("coiled_sword"),
                estusFlask = new ItemEstusFlask(0, 0, false).setRegistryName(modid, "estus_flask").setUnlocalizedName("estus_flask")
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
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote) {
            if (event.getEntity() instanceof EntityPlayer)
                PacketDispatcher.sendTo(new SyncSaveData(BonfireRegistry.INSTANCE.getBonfires()), (EntityPlayerMP) event.getEntity());
        }
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
        Command list = Command.create("bonfires");
        list.setPermissionLevel(2);
        list.addOptionalArgument("filter", String.class);
        list.addOptionalArgument("parameter", String.class);
        CommandHandler.INSTANCE.registerCommand(event, list, ((server, sender, arguments) -> {
            if (arguments.hasArgument("filter")) {
                String sort = arguments.getArgument("filter").toString().toLowerCase();
                //TODO Add more filters
                if (sort.equals("dim")) {
                    if (arguments.hasArgument("parameter")) {
                        List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByDimension(Integer.parseInt(arguments.getArgument("parameter")));
                        if (query.isEmpty()) {
                            sender.addChatMessage(new TextComponentString("No bonfires found with Dim ID: " + arguments.getArgument("parameter")));
                        } else {
                            sender.addChatMessage(new TextComponentString("Found " + query.size() + " bonfires in dimension " + sender.getServer().worldServerForDimension(Integer.parseInt(arguments.getArgument("parameter"))).provider.getDimensionType().getName() + "(" + arguments.getArgument("parameter") + ")"));
                            query.forEach((bonfires -> {
                                GameProfile owner = sender.getServer().getPlayerProfileCache().getProfileByUUID(bonfires.getOwner());
                                String name = "N/A";
                                if(owner != null) {
                                    name = owner.getName();
                                }
                                sender.addChatMessage(new TextComponentString("Name:" + bonfires.getName() + "; ID:" + bonfires.getId() + "; Owner:" + name + "(" + bonfires.getOwner().getMostSignificantBits() + "); Pos:" + "[X:"+bonfires.getPos().getX()+", Y:"+bonfires.getPos().getY()+", Z:"+bonfires.getPos().getZ()+"]"));
                            }));
                        }
                    }
                }
            }
        }));
        Command travel = Command.create("travel");
        travel.setPermissionLevel(2);
        CommandHandler.INSTANCE.registerArgumentParser(UUID.class, ArgumentParserUUID.UUID);
        travel.addRequiredArgument("id", UUID.class);
        CommandHandler.INSTANCE.registerCommand(event, travel, (server, sender, arguments) -> {
            UUID id = arguments.getArgument("id");
            if (sender instanceof EntityPlayer) {
                if (BonfireRegistry.INSTANCE.getBonfire(id) != null) {
                    Bonfire bonfire = BonfireRegistry.INSTANCE.getBonfire(id);
                    BonfireTeleporter tp = new BonfireTeleporter(server.worldServerForDimension(bonfire.dimension));
                    tp.teleport((EntityPlayer) sender, ((EntityPlayer) sender).worldObj, bonfire.getPos(), bonfire.getDimension());
                }
            }
        });
    }

}
