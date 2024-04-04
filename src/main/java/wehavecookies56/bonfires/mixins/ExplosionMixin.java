package wehavecookies56.bonfires.mixins;

/*
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

}

 */
