package wehavecookies56.bonfires.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
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

    public SyncSaveData(PacketBuffer buffer) {
        super(buffer);
    }

    public SyncSaveData(Map<UUID, Bonfire> bonfires) {
        this.bonfires = bonfires;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        bonfires = new HashMap<>();
        while (buffer.isReadable()) {
            UUID key = buffer.readUUID();
            String name = buffer.readUtf();
            UUID owner = buffer.readUUID();
            BlockPos pos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            RegistryKey<World> dim = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(buffer.readUtf()));
            boolean isPublic = buffer.readBoolean();
            Instant time = Instant.ofEpochSecond(buffer.readLong(), buffer.readInt());
            Bonfire bonfire = new Bonfire(name, key, owner, pos, dim, isPublic, time);
            bonfires.put(key, bonfire);
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
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
