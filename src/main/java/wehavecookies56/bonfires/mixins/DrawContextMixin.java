package wehavecookies56.bonfires.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import wehavecookies56.bonfires.client.IDrawContextExtensions;

@Mixin(DrawContext.class)
public class DrawContextMixin implements IDrawContextExtensions {

    @Shadow
    @Final
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Unique
    public VertexConsumerProvider.Immediate getVertexConsumers() {
        return vertexConsumers;
    }

}
