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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;

import java.util.ArrayList;
import java.util.List;

public class SendBonfiresToClient implements FabricPacket {

    public static final PacketType<SendBonfiresToClient> TYPE = PacketType.create(new Identifier(Bonfires.modid, "send_bonfires_to_client"), SendBonfiresToClient::new);

    public SendBonfiresToClient(PacketByteBuf buffer) {
        decode(buffer);
    }

    public List<RegistryKey<World>> dimensions;
    public BonfireRegistry registry;

    public SendBonfiresToClient(MinecraftServer server) {
        dimensions = new ArrayList<>(server.getWorldRegistryKeys());
        registry = BonfireHandler.getServerHandler(server).getRegistry();
    }

    public void decode(PacketByteBuf buffer) {
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(RegistryKey.of(RegistryKeys.WORLD, buffer.readIdentifier()));
        }
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeNbt(registry.writeToNBT(new NbtCompound(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeIdentifier(dimensions.get(i).getValue());
        }
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

