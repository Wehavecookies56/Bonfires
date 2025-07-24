package wehavecookies56.bonfires.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Final
    @Shadow
    private World world;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), method = "affectWorld")
    public void beforeDestroyedByExplosion(boolean particles, CallbackInfo ci, @Local(ordinal = 0) BlockPos blockPos, @Local(ordinal = 0) BlockState blockState) {
        if (blockState.getBlock() instanceof AshBonePileBlock block) {
            block.wasDestroyedByExplosion(world, blockPos);
        }
    }

}
