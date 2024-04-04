package wehavecookies56.bonfires.packets.server;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.data.ReinforceHandler;

public class ReinforceItem implements FabricPacket {

    public static final PacketType<ReinforceItem> TYPE = PacketType.create(new Identifier(Bonfires.modid, "reinforce_item"), ReinforceItem::new);

    private int slot;

    public ReinforceItem(PacketByteBuf buffer) {
        decode(buffer);
    }

    public ReinforceItem(int slot) {
        this.slot = slot;
    }

    public void decode(PacketByteBuf buffer) {
        this.slot = buffer.readInt();
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeInt(slot);
    }

    public void handle(ServerPlayerEntity player) {
        if (Bonfires.CONFIG.common.enableReinforcing()) {
            ItemStack toReinforce = player.getInventory().getStack(slot);
            ItemStack required = ReinforceHandler.getRequiredResources(toReinforce);
            if (ReinforceHandler.canReinforce(toReinforce)) {
                ReinforceHandler.removeRequiredItems(player, required);
                ReinforceHandler.levelUp(toReinforce);
                toReinforce.getNbt().putInt("Damage", 0);
                player.getInventory().setStack(slot, toReinforce);
            }
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

