package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;

import java.util.*;

public class SendBonfiresToClient implements FabricPacket {

    public static final PacketType<SendBonfiresToClient> TYPE = PacketType.create(new Identifier(Bonfires.modid, "send_bonfires_to_client"), SendBonfiresToClient::new);

    public SendBonfiresToClient(PacketByteBuf buffer) {
        decode(buffer);
    }

    public List<RegistryKey<World>> dimensions;
    public BonfireRegistry registry;
    public Map<UUID, String> ownerNames;

    public SendBonfiresToClient(MinecraftServer server) {
        dimensions = new ArrayList<>(server.getWorldRegistryKeys());
        registry = BonfireHandler.getServerHandler(server).getRegistry();
        ownerNames = BonfireRegistry.getOwnerNames(server);
    }

    public SendBonfiresToClient(ServerPlayerEntity player) {
        dimensions = new ArrayList<>(player.server.getWorldRegistryKeys());
        registry = BonfireHandler.getServerHandler(player.server).getRegistry().getFilteredRegistry(DiscoveryHandler.getHandler(player).getDiscovered().keySet().stream().toList());
        ownerNames = BonfireRegistry.getOwnerNames(player.server);
    }

    public void decode(PacketByteBuf buffer) {
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(RegistryKey.of(RegistryKeys.WORLD, buffer.readIdentifier()));
        }
        NbtCompound owners = buffer.readNbt();
        ownerNames = new HashMap<>();
        owners.getKeys().forEach(s -> {
            ownerNames.put(UUID.fromString(s), owners.getString(s));
        });
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeNbt(registry.writeToNBT(new NbtCompound(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeIdentifier(dimensions.get(i).getValue());
        }
        NbtCompound owners = new NbtCompound();
        ownerNames.forEach((uuid, s) -> owners.putString(uuid.toString(), s));
        buffer.writeNbt(owners);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.setBonfiresFromServer(this);
        }
    }
}

