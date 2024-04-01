package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

public class DisplayBonfireTitle extends Packet<DisplayBonfireTitle> {

    public DisplayBonfireTitle(FriendlyByteBuf buffer) {
        super(buffer);
    }

    Bonfire bonfire;

    public DisplayBonfireTitle(Bonfire bonfire) {
        this.bonfire = bonfire;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        bonfire = new Bonfire(buffer.readNbt());
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(bonfire.serializeNBT());
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.displayBonfireTravelled(bonfire));
    }
}
