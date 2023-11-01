package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncSaveData extends Packet<SyncSaveData> {

    public Map<UUID, Bonfire> bonfires;

    public SyncSaveData(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public SyncSaveData(Map<UUID, Bonfire> bonfires) {
        this.bonfires = bonfires;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        bonfires = new HashMap<>();
        while (buffer.isReadable()) {
            UUID key = buffer.readUUID();
            String name = buffer.readUtf();
            UUID owner = buffer.readUUID();
            BlockPos pos = new BlockPos((int) buffer.readDouble(), (int) buffer.readDouble(), (int) buffer.readDouble());
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(buffer.readUtf()));
            boolean isPublic = buffer.readBoolean();
            Instant time = Instant.ofEpochSecond(buffer.readLong(), buffer.readInt());
            Bonfire bonfire = new Bonfire(name, key, owner, pos, dim, isPublic, time);
            bonfires.put(key, bonfire);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        for (Map.Entry<UUID, Bonfire> pair : bonfires.entrySet()) {
            buffer.writeUUID(pair.getKey());
            Bonfire bonfire = pair.getValue();
            buffer.writeUtf(bonfire.getName());
            buffer.writeUUID(bonfire.getOwner());
            buffer.writeDouble(bonfire.getPos().getX());
            buffer.writeDouble(bonfire.getPos().getY());
            buffer.writeDouble(bonfire.getPos().getZ());
            buffer.writeUtf(bonfire.getDimension().location().toString());
            buffer.writeBoolean(bonfire.isPublic());
            buffer.writeLong(bonfire.getTimeCreated().getEpochSecond());
            buffer.writeInt(bonfire.getTimeCreated().getNano());
        }
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.syncSaveData(this));
    }
}
