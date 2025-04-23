package wehavecookies56.bonfires.mixins;

import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.setup.BlockSetup;

import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {

    @Inject(at=@At(value = "RETURN", ordinal = 2), method = "findRespawnPosition", cancellable = true)
    private static void respawn(ServerWorld world, BlockPos pos, float angle, boolean forced, boolean alive, CallbackInfoReturnable<Optional<ServerPlayerEntity.RespawnPos>> cir) {
        Block block = world.getBlockState(pos).getBlock();
        if (block == BlockSetup.ash_bone_pile) {
            AshBonePileBlock bonfire = (AshBonePileBlock) block;
            cir.setReturnValue(bonfire.getRespawnPosition(pos, world));
        }
    }

}
