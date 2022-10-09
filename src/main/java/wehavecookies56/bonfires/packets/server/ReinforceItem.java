package wehavecookies56.bonfires.packets.server;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SyncReinforceData;

/**
 * Created by Toby on 06/11/2016.
 */
public class ReinforceItem extends Packet<ReinforceItem> {

    private int slot;

    public ReinforceItem(PacketBuffer buffer) {
        super(buffer);
    }

    public ReinforceItem(int slot) {
        this.slot = slot;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.slot = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(slot);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ItemStack toReinforce = context.getSender().inventory.getItem(slot);
        ItemStack required = ReinforceHandler.getRequiredResources(toReinforce);
        if (ReinforceHandler.hasHandler(toReinforce)) {
            if (!toReinforce.hasTag() || !toReinforce.getTag().contains("reinforce_level")) {
                if (ReinforceHandler.getHandler(toReinforce).level() > 0) {

                }
            }
        }
        if (ReinforceHandler.canReinforce(toReinforce)) {

        }
        if (ReinforceHandler.hasHandler(toReinforce)) {
            ReinforceHandler.removeRequiredItems(context.getSender(), required);
            ReinforceHandler.levelUp(toReinforce);
            toReinforce.getTag().putInt("Damage", 0);
            context.getSender().inventory.setItem(slot, toReinforce);
        }
    }
}
