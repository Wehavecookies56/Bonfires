package wehavecookies56.bonfires.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public abstract class Packet<T extends Packet<T>> implements CustomPacketPayload {
    public abstract void handle(final PlayPayloadContext context);

    public abstract void decode(FriendlyByteBuf buffer);
}
