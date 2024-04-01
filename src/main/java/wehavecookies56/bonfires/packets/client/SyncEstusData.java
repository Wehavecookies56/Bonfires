package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class SyncEstusData extends Packet<SyncEstusData> {

    public SyncEstusData(FriendlyByteBuf buffer) {
        super(buffer);
    }

    UUID lastRested;

    public SyncEstusData(EstusHandler.IEstusHandler handler) {
        this.lastRested = handler.lastRested();
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.lastRested = buffer.readUUID();
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.lastRested != null);
        if (this.lastRested != null) {
            buffer.writeUUID(this.lastRested);
        }
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.syncEstusData(this.lastRested));

    }
}
