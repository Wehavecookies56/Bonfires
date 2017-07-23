package uk.co.wehavecookies56.bonfires.proxies;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.tiles.TESRBonfire;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

/**
 * Created by Toby on 05/11/2016.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerRender(Bonfires.estusFlask, 0, Bonfires.estusFlask.getRegistryName() + "_empty");
        registerRender(Bonfires.estusFlask, 1, Bonfires.estusFlask.getRegistryName() + "_quarter");
        registerRender(Bonfires.estusFlask, 2, Bonfires.estusFlask.getRegistryName() + "_half");
        registerRender(Bonfires.estusFlask, 3, Bonfires.estusFlask.getRegistryName() + "");
        registerRender(Bonfires.ashPile);
        registerRender(Bonfires.coiledSword);
        registerRender(Bonfires.coiledSwordFragment);
        registerRender(Bonfires.estusShard);
        registerRender(Bonfires.homewardBone);
        registerRender(Item.getItemFromBlock(Bonfires.ashBonePile));
        registerRender(Item.getItemFromBlock(Bonfires.ashBlock));
    }

    @Override
    public void preInit() {


    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName() + "", "inventory"));
    }

    public static void registerRender(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
    }

    @Override
    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBonfire.class, new TESRBonfire());
    }

    @Override
    public void postInit() {

    }

    @Override
    public EntityPlayer getPlayerEntity (MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext (MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
    }

}
