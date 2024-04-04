package wehavecookies56.bonfires.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wehavecookies56.bonfires.client.gui.BonfireScreen;
import wehavecookies56.bonfires.client.gui.ReinforceScreen;

@Mixin(Screen.class)
public class ScreenMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    public void renderBackground(Screen instance, DrawContext context, int mouseX, int mouseY, float delta) {
        if (!(instance instanceof BonfireScreen || instance instanceof ReinforceScreen)) {
            instance.renderBackground(context, mouseX, mouseY, delta);
        }
    }
}
