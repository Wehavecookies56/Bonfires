package wehavecookies56.bonfires.setup;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.TooltipType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.client.ScreenshotUtils;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.util.List;

public class ClientSetup implements ClientModInitializer {

    public static void tooltipEvent(ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> tooltip) {
        ReinforceHandler.ReinforceLevel reinforceLevel = stack.get(ComponentSetup.REINFORCE_LEVEL);
        if (reinforceLevel != null) {
            int level = reinforceLevel.level();
            if (level > 0) {
                Text component = tooltip.get(0);
                MutableText name = (MutableText) component;
                name.append(" +" + level);
                tooltip.set(0, name.fillStyle(Style.EMPTY.withItalic(false)));
            }
        }
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(ScreenshotUtils::clientTick);
        ItemTooltipCallback.EVENT.register(ClientSetup::tooltipEvent);
        ModelPredicateProviderRegistry.register(ItemSetup.estus_flask, new Identifier(Bonfires.modid, "uses"), (stack, world, entity, seed) -> {
            return entity != null && stack.get(ComponentSetup.ESTUS) != null ? (float) stack.get(ComponentSetup.ESTUS).uses() / (float) stack.get(ComponentSetup.ESTUS).maxUses() : 0.0F;
        });
        BlockEntityRendererFactories.register(EntitySetup.BONFIRE, BonfireRenderer::new);
        ClientPacketHandler.init();
    }
}
