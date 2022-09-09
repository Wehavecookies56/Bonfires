package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SyncReinforceData;

/**
 * Created by Toby on 06/11/2016.
 */
public class ReinforceItem extends Packet<ReinforceItem> {

    private int slot;

    public ReinforceItem(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public ReinforceItem(int slot) {
        this.slot = slot;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.slot = buffer.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(slot);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ItemStack toReinforce = context.getSender().getInventory().getItem(slot);
        ItemStack required = ReinforceHandler.getRequiredResources(toReinforce);
        if (ReinforceHandler.hasHandler(toReinforce)) {
            ReinforceHandler.removeRequiredItems(context.getSender(), required);
            ReinforceHandler.getHandler(toReinforce).levelup(1);
            toReinforce.resetHoverName();
            toReinforce.getTag().putInt("Damage", 0);
            toReinforce.setHoverName(Component.translatable(toReinforce.getHoverName().getString() + " +" + ReinforceHandler.getHandler(toReinforce).level()).setStyle(Style.EMPTY.withItalic(false)));
            context.getSender().getInventory().setItem(slot, toReinforce);
            PacketHandler.sendTo(new SyncReinforceData(ReinforceHandler.getHandler(toReinforce), slot), context.getSender());
        }
    }
}
