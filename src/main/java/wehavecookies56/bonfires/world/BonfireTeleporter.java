package wehavecookies56.bonfires.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Toby on 10/11/2016.
 */
public class BonfireTeleporter {

    public static Vec3d attemptToPlaceNextToBonfire(BlockPos bonfirePos, World world) {
        Vec3d centre = new Vec3d((bonfirePos.getX()) + 0.5, (bonfirePos.getY()) + 0.5, (bonfirePos.getZ()) + 0.5);
        for (int i = 0; i <= 3; i++) {
            Direction dir = Direction.fromHorizontal(i);
            BlockPos newPos = bonfirePos.offset(dir);
            BlockState state = world.getBlockState(new BlockPos(newPos));
            if (state.getBlock().canMobSpawnInside(state)) {
                return new Vec3d(newPos.getX() + 0.5D, newPos.getY() + 0.5, newPos.getZ() + 0.5);
            }
        }
        return centre;
    }

    public static void placeEntity(Entity entity, BlockPos pos, ServerWorld destWorld) {
        if (entity instanceof ServerPlayerEntity playerMP) {
            playerMP.setVelocity(0, 0, 0);
            Vec3d destination = attemptToPlaceNextToBonfire(pos, destWorld);
            playerMP.teleport(destination.x, destination.y, destination.z);
        }
    }


    public static void travelToBonfire(ServerPlayerEntity player, BlockPos destination, RegistryKey<World> dimension) {
        ServerWorld destinationWorld = player.getServerWorld();
        if (!player.getWorld().getRegistryKey().getValue().equals(dimension.getValue())) {
            destinationWorld = player.getServerWorld().getServer().getWorld(dimension);
            player.teleport(destinationWorld, destination.getX(), destination.getY(), destination.getZ(), player.getYaw(), player.getPitch());
        } else {
            placeEntity(player, destination, destinationWorld);
        }
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
        player.getWorld().playSound(null, destination, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
    }
}
