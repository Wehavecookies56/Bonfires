package wehavecookies56.bonfires.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public abstract class Packet<T extends Packet<T>> {

    public Packet(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public Packet() {}

    public abstract void decode(FriendlyByteBuf buffer);
    public abstract void encode(FriendlyByteBuf buffer);
    public final void handlePacket(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> handle(context));
        context.setPacketHandled(true);
    }

    protected abstract void handle(CustomPayloadEvent.Context context);
}
