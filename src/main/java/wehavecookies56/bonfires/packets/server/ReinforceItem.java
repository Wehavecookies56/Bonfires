package wehavecookies56.bonfires.packets.server;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.setup.ComponentSetup;

public record ReinforceItem(int slot) implements CustomPayload {

    public static final Id<ReinforceItem> TYPE = new Id<>(new Identifier(Bonfires.modid, "reinforce_item"));

    public static final PacketCodec<PacketByteBuf, ReinforceItem> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER,
            ReinforceItem::slot,
            ReinforceItem::new
    );

    public void handle(ServerPlayerEntity player) {
        if (Bonfires.CONFIG.common.enableReinforcing()) {
            ItemStack toReinforce = player.getInventory().getStack(slot);
            ItemStack required = ReinforceHandler.getRequiredResources(toReinforce);
            if (ReinforceHandler.canReinforce(toReinforce)) {
                ReinforceHandler.removeRequiredItems(player, required);
                ReinforceHandler.levelUp(toReinforce);
                if (toReinforce.get(DataComponentTypes.DAMAGE) != null) {
                    toReinforce.set(DataComponentTypes.DAMAGE, 0);
                }
                if (toReinforce.get(DataComponentTypes.MAX_DAMAGE) != null && toReinforce.get(ComponentSetup.REINFORCE_LEVEL) != null) {
                    int maxDamage = toReinforce.get(DataComponentTypes.MAX_DAMAGE);
                    toReinforce.set(DataComponentTypes.MAX_DAMAGE, maxDamage + (maxDamage * 10/100));
                }
                player.getInventory().setStack(slot, toReinforce);
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}

