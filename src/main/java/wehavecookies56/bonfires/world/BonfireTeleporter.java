package wehavecookies56.bonfires.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

    public static Vec3 attemptToPlaceNextToBonfire(BlockPos bonfirePos, Level world) {
        Vec3 centre = new Vec3((bonfirePos.getX()) + 0.5, (bonfirePos.getY()) + 0.5, (bonfirePos.getZ()) + 0.5);
        for (int i = 0; i <= 3; i++) {
            Direction dir = Direction.from2DDataValue(i);
            BlockPos newPos = bonfirePos.relative(dir);
            BlockState state = world.getBlockState(new BlockPos(newPos));
            if (state.getBlock().isPossibleToRespawnInThis(state)) {
                return new Vec3(newPos.getX() + 0.5D, newPos.getY() + 0.5, newPos.getZ() + 0.5);
            }
        }
        return centre;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        if (entity instanceof ServerPlayer playerMP) {
            playerMP.setDeltaMovement(0, 0, 0);
            Vec3 destination = attemptToPlaceNextToBonfire(pos, destWorld);
            playerMP.connection.teleport(destination.x, destination.y, destination.z, playerMP.getYRot(), playerMP.getXRot());
        }
        return repositionEntity.apply(false);
    }


    public static void travelToBonfire(ServerPlayer player, BlockPos destination, ResourceKey<Level> dimension) {
        BonfireTeleporter tp = new BonfireTeleporter(destination);
        ServerLevel destinationWorld = (ServerLevel) player.level();
        player.level().playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
        player.level().playSound(null, destination, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
        if (!player.level().dimension().location().equals(dimension.location())) {
            destinationWorld = player.level().getServer().getLevel(dimension);
            player.changeDimension(destinationWorld, tp);
        }
        tp.placeEntity(player, (ServerLevel) player.level(), destinationWorld, 0, (portal) -> player);
    }
}
