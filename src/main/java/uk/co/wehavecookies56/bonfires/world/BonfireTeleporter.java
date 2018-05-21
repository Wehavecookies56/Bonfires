package uk.co.wehavecookies56.bonfires.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ITeleporter;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBonePile;

/**
 * Created by Toby on 10/11/2016.
 */
public class BonfireTeleporter implements ITeleporter {

    BlockPos pos;

    public BonfireTeleporter(BlockPos pos) {
        this.pos = pos;
    }

    //There's gotta be a better way of doing this
    public Vec3d attemptToPlaceNextToBonfire(BlockPos bonfirePos, World world) {
        Vec3d centre = new Vec3d((bonfirePos.getX()) + 0.5, (bonfirePos.getY()) + 0.5, (bonfirePos.getZ()) + 0.5);
        if (world.getBlockState(new BlockPos(centre.addVector(0,0, -1))).getBlock().isPassable(world, new BlockPos(centre.addVector(0,0, -1)))) {
            return centre.addVector(0, 0, -1);
        } else if (world.getBlockState(new BlockPos(centre.addVector(0,0, 1))).getBlock().isPassable(world, new BlockPos(centre.addVector(0,0, 1)))) {
            return centre.addVector(0,0, 1);
        } else if (world.getBlockState(new BlockPos(centre.addVector(-1,0, 0))).getBlock().isPassable(world, new BlockPos(centre.addVector(-1,0, 0)))) {
            return centre.addVector(-1,0, 0);
        } else if (world.getBlockState(new BlockPos(centre.addVector(1,0, 0))).getBlock().isPassable(world, new BlockPos(centre.addVector(1,0, 0)))) {
            return centre.addVector(1,0, 0);
        } else {
            return centre;
        }
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        if (entity instanceof EntityPlayer) {
            EntityPlayerMP playerMP = (EntityPlayerMP) entity;
            playerMP.motionX = playerMP.motionY = playerMP.motionZ = 0;
            Vec3d destination = attemptToPlaceNextToBonfire(pos, world);
            playerMP.connection.setPlayerLocation(destination.x, destination.y, destination.z, playerMP.rotationYaw, playerMP.rotationPitch);
        }
    }
}
