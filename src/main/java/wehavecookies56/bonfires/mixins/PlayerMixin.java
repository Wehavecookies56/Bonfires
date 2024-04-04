package wehavecookies56.bonfires.mixins;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.setup.BlockSetup;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public class PlayerMixin {

    @Inject(at=@At(value = "RETURN", ordinal = 2), method = "findRespawnPosition")
    private static void respawn(ServerWorld world, BlockPos pos, float angle, boolean forced, boolean alive, CallbackInfoReturnable<Optional<Vec3d>> cir) {
        Block block = world.getBlockState(pos).getBlock();
        if (block == BlockSetup.ash_bone_pile) {
            AshBonePileBlock bonfire = (AshBonePileBlock) block;
            bonfire.getRespawnPosition(pos, world);
        }
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"))
    private void stopReset(PlayerEntity instance) {
        //do nothing
    }

    @Inject(at=@At("TAIL"), method = "attack")
    private void resetTicksAtEnd(Entity target, CallbackInfo ci) {
        ((PlayerEntity)(Object)this).resetLastAttackedTicks();
    }

}
