package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.Packet;

/**
 * Created by Toby on 06/11/2016.
 */
public class ReinforceItem extends Packet<ReinforceItem> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "reinforce_item");
    private int slot;

    public ReinforceItem(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public ReinforceItem(int slot) {
        this.slot = slot;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.slot = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(slot);
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (BonfiresConfig.Common.enableReinforcing) {
            ItemStack toReinforce = context.player().get().getInventory().getItem(slot);
            ItemStack required = ReinforceHandler.getRequiredResources(toReinforce);
            if (ReinforceHandler.canReinforce(toReinforce)) {
                ReinforceHandler.removeRequiredItems(context.player().get(), required);
                ReinforceHandler.levelUp(toReinforce);
                toReinforce.getTag().putInt("Damage", 0);
                context.player().get().getInventory().setItem(slot, toReinforce);
            }
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
