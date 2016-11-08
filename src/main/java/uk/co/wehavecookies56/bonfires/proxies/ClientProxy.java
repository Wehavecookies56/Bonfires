package uk.co.wehavecookies56.bonfires.proxies;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.tiles.TESRBonfire;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

/**
 * Created by Toby on 05/11/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ModelLoader.setCustomModelResourceLocation(Bonfires.estusFlask, 0, new ModelResourceLocation(Bonfires.estusFlask.getRegistryName() + "_empty"));
        ModelLoader.setCustomModelResourceLocation(Bonfires.estusFlask, 1, new ModelResourceLocation(Bonfires.estusFlask.getRegistryName() + "_quarter"));
        ModelLoader.setCustomModelResourceLocation(Bonfires.estusFlask, 2, new ModelResourceLocation(Bonfires.estusFlask.getRegistryName() + "_half"));
        ModelLoader.setCustomModelResourceLocation(Bonfires.estusFlask, 3, new ModelResourceLocation(Bonfires.estusFlask.getRegistryName() + ""));
    }

    @Override
    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBonfire.class, new TESRBonfire());
        for (Item i : Bonfires.items) {
            if (i != Bonfires.estusFlask)
                Bonfires.registerRender(i);
        }
        for (Block b : Bonfires.blocks) {
            Bonfires.registerRender(Item.getItemFromBlock(b));
        }
    }

    @Override
    public void postInit() {

    }

    @Override
    public EntityPlayer getPlayerEntity (MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }

    @Override
    public IThreadListener getThreadFromContext (MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
    }

}
