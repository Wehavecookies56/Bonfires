package wehavecookies56.bonfires.setup;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.items.EstusFlaskItem;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ClientSetup.GameBusEvents());
    }

    @SubscribeEvent
    public static void registerItemProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "uses"), EstusFlaskItem.FlaskUses.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EntitySetup.BONFIRE.get(), BonfireRenderer::new);
    }

    public static class GameBusEvents {
        @SubscribeEvent
        public void tooltipEvent(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            if (event.getItemStack().has(ComponentSetup.REINFORCE_LEVEL)) {
                ReinforceHandler.ReinforceLevel reinforceLevel = event.getItemStack().get(ComponentSetup.REINFORCE_LEVEL);
                int level = reinforceLevel.level();
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
