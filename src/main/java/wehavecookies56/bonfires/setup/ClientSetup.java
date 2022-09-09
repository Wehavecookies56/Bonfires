package wehavecookies56.bonfires.setup;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ItemSetup.estus_flask.get(), new ResourceLocation(Bonfires.modid, "uses"), (stack, world, entity, seed) -> {
                return entity != null && stack.getTag() != null ? (float) stack.getTag().getInt("estus") / (float) stack.getTag().getInt("uses") : 0.0F;
            });
            ItemBlockRenderTypes.setRenderLayer(BlockSetup.ash_bone_pile.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EntitySetup.BONFIRE.get(), BonfireRenderer::new);
    }
}
