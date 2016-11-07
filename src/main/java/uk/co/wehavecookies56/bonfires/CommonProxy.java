package uk.co.wehavecookies56.bonfires;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Toby on 05/11/2016.
 */
public class CommonProxy {

    public void preInit() {

    }

    public void init() {

    }

    public void postInit() {

    }

    public EntityPlayer getPlayerEntity (MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public IThreadListener getThreadFromContext (MessageContext ctx) {
        return ctx.getServerHandler().playerEntity.getServer();
    }
}
