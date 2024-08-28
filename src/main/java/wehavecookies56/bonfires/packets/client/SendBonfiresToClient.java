package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.*;

public class SendBonfiresToClient extends Packet<SendBonfiresToClient> {

    public SendBonfiresToClient(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public List<ResourceKey<Level>> dimensions;
    public BonfireRegistry registry;
    public Map<UUID, String> ownerNames;

    public SendBonfiresToClient() {
        dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
        registry = BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry();
        ownerNames = BonfireRegistry.getOwnerNames(ServerLifecycleHooks.getCurrentServer());
    }

    public SendBonfiresToClient(ServerPlayer player) {
        dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
        registry = BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry().getFilteredRegistry(DiscoveryHandler.getHandler(player).getDiscovered().keySet().stream().toList());
        ownerNames = BonfireRegistry.getOwnerNames(ServerLifecycleHooks.getCurrentServer());
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation()));
        }
        CompoundTag owners = buffer.readNbt();
        ownerNames = new HashMap<>();
        owners.getAllKeys().forEach(s -> {
            ownerNames.put(UUID.fromString(s), owners.getString(s));
        });
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(registry.writeToNBT(new CompoundTag(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeResourceLocation(dimensions.get(i).location());
        }
        CompoundTag owners = new CompoundTag();
        ownerNames.forEach((uuid, s) -> owners.putString(uuid.toString(), s));
        buffer.writeNbt(owners);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.setBonfiresFromServer(this));
    }
}
