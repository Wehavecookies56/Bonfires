package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
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

public record SendBonfiresToClient(List<RegistryKey<World>> dimensions, BonfireRegistry registry) implements CustomPayload {

    public static final Id<SendBonfiresToClient> TYPE = new Id<>(new Identifier(Bonfires.modid, "send_bonfires_to_client"));

    public static final PacketCodec<PacketByteBuf, SendBonfiresToClient> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.collection(ArrayList::new, RegistryKey.createPacketCodec(RegistryKeys.WORLD)),
            SendBonfiresToClient::dimensions,
            BonfireRegistry.STREAM_CODEC,
            SendBonfiresToClient::registry,
            SendBonfiresToClient::new
    );

    public SendBonfiresToClient(MinecraftServer server) {
        this(new ArrayList<>(server.getWorldRegistryKeys()), BonfireHandler.getServerHandler(server).getRegistry());
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.setBonfiresFromServer(this);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}

