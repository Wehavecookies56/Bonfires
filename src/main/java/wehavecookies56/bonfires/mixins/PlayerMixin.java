package wehavecookies56.bonfires.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin {

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"))
    private void stopReset(PlayerEntity instance) {
        //do nothing
    }

    @Inject(at=@At("TAIL"), method = "attack")
    private void resetTicksAtEnd(Entity target, CallbackInfo ci) {
        ((PlayerEntity)(Object)this).resetLastAttackedTicks();
    }

}
