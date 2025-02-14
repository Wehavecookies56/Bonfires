package wehavecookies56.bonfires.setup;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.client.ScreenshotUtils;
import wehavecookies56.bonfires.client.tiles.BonfireRenderer;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.items.EstusFlaskItem;

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
        WorldRenderEvents.END.register(ScreenshotUtils::clientTick);
        ItemTooltipCallback.EVENT.register(ClientSetup::tooltipEvent);
        NumericProperties.ID_MAPPER.put(
                Identifier.of(Bonfires.modid, "uses"),
                EstusFlaskItem.FlaskUses.MAP_CODEC
        );
        BlockEntityRendererFactories.register(EntitySetup.BONFIRE, BonfireRenderer::new);
        ClientPacketHandler.init();
    }
}
