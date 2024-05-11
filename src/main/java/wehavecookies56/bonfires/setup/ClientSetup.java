package wehavecookies56.bonfires.setup;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ClientSetup.ForgeBusEvents());
        event.enqueueWork(() -> {
            ItemProperties.register(ItemSetup.estus_flask.get(), new ResourceLocation(Bonfires.modid, "uses"), (stack, world, entity, seed) -> {
                return entity != null && stack.getTag() != null ? (float) stack.getTag().getInt("estus") / (float) stack.getTag().getInt("uses") : 0.0F;
            });
        });
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EntitySetup.BONFIRE.get(), BonfireRenderer::new);
    }

    public static class ForgeBusEvents {
        @SubscribeEvent
        public void tooltipEvent(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            if (event.getItemStack().hasTag()) {
                CompoundTag tag = event.getItemStack().getTag();
                if (tag.contains("reinforce_level")) {
                    int level = tag.getInt("reinforce_level");
                    if (level > 0) {
                        Component component = event.getToolTip().get(0);
                        MutableComponent name = (MutableComponent) component;
                        name.append(" +" + level);
                        event.getToolTip().set(0, name.withStyle(Style.EMPTY.withItalic(false)));
                    }
                }
            }
        }
    }
}
