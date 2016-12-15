package uk.co.wehavecookies56.bonfires;

import com.mojang.authlib.GameProfile;
import net.ilexiconn.llibrary.server.command.Command;
import net.ilexiconn.llibrary.server.command.CommandHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
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
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */
@Mod(modid = Bonfires.modid, name = Bonfires.name, version = Bonfires.version, dependencies = "required-after:llibrary@[1.7.2]")
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
                event.getDrops().add(new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(ashPile)));
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
        BonfireRegistry.INSTANCE.clearBonfires();
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


    public static void listQueriedBonfires(List<Bonfire> query, ICommandSender sender) {
        query.forEach((bonfires -> {
            GameProfile owner = sender.getServer().getPlayerProfileCache().getProfileByUUID(bonfires.getOwner());
            String name = I18n.format(LocalStrings.COMMAND_NA);
            if(owner != null) {
                name = owner.getName();
            }
            TextComponentTranslation messageName = new TextComponentTranslation(LocalStrings.COMMAND_NAME, bonfires.getName());
            TextComponentTranslation messageID = new TextComponentTranslation(LocalStrings.COMMAND_ID, bonfires.getId());
            TextComponentTranslation messageOwner = new TextComponentTranslation(LocalStrings.COMMAND_OWNER, bonfires.getOwner().getMostSignificantBits());
            TextComponentTranslation messagePos = new TextComponentTranslation(LocalStrings.COMMAND_POS, bonfires.getPos().getX(), bonfires.getPos().getY(), bonfires.getPos().getZ());

            sender.addChatMessage(new TextComponentString(messageName.getUnformattedText() + " " + messageID.getUnformattedText() + " " + messageOwner.getUnformattedText() + " " + messagePos.getUnformattedText()));
        }));
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

                // Filter by dimension ID
                if (sort.equals("dim")) {
                    if (arguments.hasArgument("parameter")) {
                        if (DimensionManager.isDimensionRegistered(Integer.parseInt(arguments.getArgument("parameter")))) {
                            List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByDimension(Integer.parseInt(arguments.getArgument("parameter")));
                            if (query.isEmpty()) {
                                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_DIM_NOMATCH, Integer.parseInt(arguments.getArgument("parameter")));
                                message.getStyle().setColor(TextFormatting.RED);
                                sender.addChatMessage(message);
                            } else {
                                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_DIM_MATCH, query.size(), sender.getServer().worldServerForDimension(Integer.parseInt(arguments.getArgument("parameter"))).provider.getDimensionType().getName() + "(" + arguments.getArgument("parameter") + ")");
                                sender.addChatMessage(message);
                                listQueriedBonfires(query, sender);
                            }
                        } else {
                            TextComponentTranslation error = new TextComponentTranslation(LocalStrings.COMMAND_DIM_NODIM, Integer.parseInt(arguments.getArgument("parameter")));
                            error.getStyle().setColor(TextFormatting.DARK_RED);
                            sender.addChatMessage(error);
                        }
                    }
                }
                if (sort.equals("owner")) {
                    if (arguments.hasArgument("parameter")) {
                        UUID ownerID = sender.getServer().getPlayerProfileCache().getGameProfileForUsername(arguments.getArgument("parameter")).getId();
                        if (ownerID != null) {
                            List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByOwner(ownerID);
                            if (query.isEmpty()) {
                                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOMATCH, arguments.getArgument("parameter").toString());
                                message.getStyle().setColor(TextFormatting.RED);
                                sender.addChatMessage(message);
                            } else {
                                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_MATCH, query.size(), arguments.getArgument("parameter").toString());
                                sender.addChatMessage(message);
                                listQueriedBonfires(query, sender);
                            }
                        } else {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOUSER, arguments.getArgument("parameter").toString());
                            message.getStyle().setColor(TextFormatting.DARK_RED);
                            sender.addChatMessage(message);
                        }
                    }
                }
                if (sort.equals("name")) {
                    if (arguments.hasArgument("parameter")) {
                        List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByName(arguments.getArgument("parameter"));
                        if (query.isEmpty()) {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOMATCH, arguments.getArgument("parameter").toString());
                            message.getStyle().setColor(TextFormatting.RED);
                            sender.addChatMessage(message);
                        } else {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_MATCH, query.size(), arguments.getArgument("parameter").toString());
                            sender.addChatMessage(message);
                            listQueriedBonfires(query, sender);
                        }
                    }
                }
                if (sort.equals("radius")) {
                    if (arguments.hasArgument("parameter")) {
                        List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresInRadius(sender.getPosition(), Integer.parseInt(arguments.getArgument("parameter")), sender.getEntityWorld().provider.getDimension());
                        if (query.isEmpty()) {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_RADIUS_NOMATCH, arguments.getArgument("parameter").toString());
                            message.getStyle().setColor(TextFormatting.RED);
                            sender.addChatMessage(message);
                        } else {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_RADIUS_MATCH, query.size(), arguments.getArgument("parameter").toString());
                            sender.addChatMessage(message);
                            listQueriedBonfires(query, sender);
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
