package wehavecookies56.bonfires.packets.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncDiscoveryData extends Packet<SyncDiscoveryData> {

    public SyncDiscoveryData(FriendlyByteBuf buffer) {
        super(buffer);
    }

    Map<UUID, Instant> discovered;

    public SyncDiscoveryData(DiscoveryHandler.IDiscoveryHandler handler) {
        this.discovered = handler.getDiscovered();
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        if (size > 0) {
            discovered = new HashMap<>();
            CompoundTag tag = buffer.readNbt();
            tag.getAllKeys().forEach(uuidString -> {
                CompoundTag timeTag = tag.getCompound(uuidString);
                discovered.put(UUID.fromString(uuidString), Instant.ofEpochSecond(timeTag.getLong("epoch"), timeTag.getInt("nano")));
            });
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(discovered.size());
        CompoundTag tag = new CompoundTag();
        discovered.forEach((uuid, instant) -> {
            CompoundTag timeTag = new CompoundTag();
            timeTag.putLong("epoch", instant.getEpochSecond());
            timeTag.putInt("nano", instant.getNano());
            tag.put(uuid.toString(), timeTag);
            buffer.writeNbt(tag);
        });
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.syncDiscoveryData(this.discovered));

    }
}
