package wehavecookies56.bonfires.setup;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;
import wehavecookies56.bonfires.data.ReinforceHandler;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientSetup());
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

    @SubscribeEvent
    public void tooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        boolean tryUseCap = true;
        if (event.getItemStack().hasTag()) {
            CompoundTag tag = event.getItemStack().getTag();
            if (tag.contains("reinforce_level")) {
                int level = tag.getInt("reinforce_level");
                if (level > 0) {
                    Component component = event.getToolTip().get(0);
                    TextComponent name = (TextComponent) component;
                    name.append(" +" + level);
                    event.getToolTip().set(0, name);
                }
                tryUseCap = false;
            }
        }
        if (tryUseCap && ReinforceHandler.hasHandler(stack)) {
            ReinforceHandler.IReinforceHandler reinforceHandler = ReinforceHandler.getHandler(stack);
            if (reinforceHandler.level() > 0) {
                Component component = event.getToolTip().get(0);
                TextComponent name = (TextComponent) component;
                name.append(" +" + reinforceHandler.level());
                event.getToolTip().set(0, name);
            }
        }
    }
}
