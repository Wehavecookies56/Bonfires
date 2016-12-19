package uk.co.wehavecookies56.bonfires.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Created by Toby on 10/11/2016.
 */
public class BonfireTeleporter extends Teleporter {

    private final WorldServer worldServer;

    public BonfireTeleporter(WorldServer worldIn) {
        super(worldIn);
        this.worldServer = worldIn;
    }

    public void teleport(EntityPlayer player, World world, BlockPos pos, int dimension) {
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        playerMP.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        playerMP.motionX = playerMP.motionY = playerMP.motionZ = 0;
        playerMP.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        if (world.provider.getDimension() != dimension)
            playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, dimension, this);
        playerMP.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {

    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        return false;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        return false;
    }

    @Override
    public void removeStalePortalLocations(long worldTime) {

    }

}
