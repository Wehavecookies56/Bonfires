package wehavecookies56.bonfires.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    World world;

    @Unique
    BlockState state;

    @Unique
    BlockPos pos;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0), method = "affectWorld")
    public BlockState getBlockState(World instance, BlockPos pos) {
        state = instance.getBlockState(pos);
        this.pos = pos;
        return state;
    }


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), method = "affectWorld")
    public void beforeDestroyedByExplosion(boolean particles, CallbackInfo ci) {
        if (state.getBlock() instanceof AshBonePileBlock block) {
            block.wasDestroyedByExplosion(world, pos);
        }
    }

}
