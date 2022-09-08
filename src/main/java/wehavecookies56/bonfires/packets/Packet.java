package wehavecookies56.bonfires.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class Packet<T extends Packet<T>> {

    public Packet(PacketBuffer buffer) {
        decode(buffer);
    }

    public Packet() {}

    public abstract void decode(PacketBuffer buffer);
    public abstract void encode(PacketBuffer buffer);
    public final void handle(Supplier<NetworkEvent.Context> contextSup) {
        NetworkEvent.Context context = contextSup.get();
        context.enqueueWork(() -> handle(context));
        context.setPacketHandled(true);
    }

    public abstract void handle(NetworkEvent.Context context);
}
