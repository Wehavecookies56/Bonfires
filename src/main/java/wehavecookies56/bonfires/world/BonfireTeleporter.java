package wehavecookies56.bonfires.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

/**
 * Created by Toby on 10/11/2016.
 */
public class BonfireTeleporter implements ITeleporter {

    BlockPos pos;

    public BonfireTeleporter(BlockPos pos) {
        this.pos = pos;
    }

    public static Vector3d attemptToPlaceNextToBonfire(BlockPos bonfirePos, World world) {
        Vector3d centre = new Vector3d((bonfirePos.getX()) + 0.5, (bonfirePos.getY()) + 0.5, (bonfirePos.getZ()) + 0.5);
        for (int i = 0; i <= 3; i++) {
            Direction dir = Direction.from2DDataValue(i);
            BlockPos newPos = bonfirePos.relative(dir);
            if (world.getBlockState(new BlockPos(newPos)).getBlock().isPossibleToRespawnInThis()) {
                return new Vector3d(newPos.getX() + 0.5D, newPos.getY() + 0.5, newPos.getZ() + 0.5);
            }
        }
        return centre;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        if (entity instanceof PlayerEntity) {
            ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
            playerMP.setDeltaMovement(0, 0, 0);
            Vector3d destination = attemptToPlaceNextToBonfire(pos, destWorld);
            playerMP.connection.teleport(destination.x, destination.y, destination.z, playerMP.yRot, playerMP.xRot);
        }
        return repositionEntity.apply(false);
    }


    public static void travelToBonfire(ServerPlayerEntity player, BlockPos destination, RegistryKey<World> dimension) {
        BonfireTeleporter tp = new BonfireTeleporter(destination);
        ServerWorld destinationWorld = (ServerWorld) player.level;
        player.level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
        player.level.playSound(null, destination, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
        if (!player.level.dimension().location().equals(dimension.location())) {
            destinationWorld = player.level.getServer().getLevel(dimension);
            player.changeDimension(destinationWorld, tp);
        }
        tp.placeEntity(player, (ServerWorld) player.level, destinationWorld, 0, (portal) -> player);
    }
}
