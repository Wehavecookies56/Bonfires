package wehavecookies56.bonfires.setup;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.client.ScreenshotUtils;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;
import wehavecookies56.bonfires.items.EstusFlaskItem;

import java.util.List;

public class ClientSetup implements ClientModInitializer {

    public static void tooltipEvent(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        if (stack.hasNbt()) {
            NbtCompound tag = stack.getNbt();
            if (tag.contains("reinforce_level")) {
                int level = tag.getInt("reinforce_level");
                if (level > 0) {
                    Text component = tooltip.get(0);
                    MutableText name = (MutableText) component;
                    name.append(" +" + level);
                    tooltip.set(0, name.fillStyle(Style.EMPTY.withItalic(false)));
                    if (!(stack.getItem() instanceof EstusFlaskItem)) {
                        //tooltip.add(1, Text.translatable(LocalStrings.TOOLTIP_REINFORCE, Bonfires.CONFIG.common.reinforceDamagePerLevel() * level));
                    }
                }
            }
        }
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(ScreenshotUtils::clientTick);
        ItemTooltipCallback.EVENT.register(ClientSetup::tooltipEvent);
        ModelPredicateProviderRegistry.register(ItemSetup.estus_flask, new Identifier(Bonfires.modid, "uses"), (stack, world, entity, seed) -> {
            return entity != null && stack.getNbt() != null ? (float) stack.getNbt().getInt("estus") / (float) stack.getNbt().getInt("uses") : 0.0F;
        });
        BlockEntityRendererFactories.register(EntitySetup.BONFIRE, BonfireRenderer::new);
        ClientPacketHandler.init();
    }
}
