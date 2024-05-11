package wehavecookies56.bonfires.packets.server;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.setup.ComponentSetup;

/**
 * Created by Toby on 06/11/2016.
 */
public record ReinforceItem(int slot) implements Packet {

    public static final Type<ReinforceItem> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "reinforce_item"));

    public static final StreamCodec<FriendlyByteBuf, ReinforceItem> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ReinforceItem::slot,
            ReinforceItem::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (BonfiresConfig.Common.enableReinforcing) {
            ItemStack toReinforce = context.player().getInventory().getItem(slot);
            ItemStack required = ReinforceHandler.getRequiredResources(toReinforce);
            if (ReinforceHandler.canReinforce(toReinforce)) {
                ReinforceHandler.removeRequiredItems(context.player(), required);
                ReinforceHandler.levelUp(toReinforce);
                if (toReinforce.has(DataComponents.DAMAGE)) {
                    toReinforce.set(DataComponents.DAMAGE, 0);
                }
                if (toReinforce.has(DataComponents.MAX_DAMAGE) && toReinforce.has(ComponentSetup.REINFORCE_LEVEL)) {
                    int maxDamage = toReinforce.get(DataComponents.MAX_DAMAGE);
                    toReinforce.set(DataComponents.MAX_DAMAGE, maxDamage + (maxDamage * 10/100));
                }
                context.player().getInventory().setItem(slot, toReinforce);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
