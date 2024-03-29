package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class SyncEstusData extends Packet<SyncEstusData> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "sync_estus_data");

    public SyncEstusData(FriendlyByteBuf buffer) {
        decode(buffer);
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
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.lastRested != null);
        if (this.lastRested != null) {
            buffer.writeUUID(this.lastRested);
        }
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.syncEstusData(this.lastRested);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
