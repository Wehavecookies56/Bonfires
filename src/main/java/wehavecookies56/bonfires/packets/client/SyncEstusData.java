package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public record SyncEstusData(UUID lastRested) implements Packet {

    public static final Type<SyncEstusData> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "sync_estus_data"));

    public static final StreamCodec<FriendlyByteBuf, SyncEstusData> STREAM_CODEC = StreamCodec.composite(
            Bonfires.NULLABLE_UUID,
            SyncEstusData::lastRested,
            SyncEstusData::new
    );

    public SyncEstusData(EstusHandler.IEstusHandler handler) {
        this(handler.lastRested());
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.syncEstusData(this.lastRested);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
