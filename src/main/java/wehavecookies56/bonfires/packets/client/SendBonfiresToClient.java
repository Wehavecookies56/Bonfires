package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record SendBonfiresToClient(List<ResourceKey<Level>> dimensions, BonfireRegistry registry, Map<UUID, String> ownerNames) implements Packet {

    public static final Type<SendBonfiresToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "send_bonfires_to_client"));

    public static final StreamCodec<FriendlyByteBuf, SendBonfiresToClient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, ResourceKey.streamCodec(Registries.DIMENSION)),
            SendBonfiresToClient::dimensions,
            BonfireRegistry.STREAM_CODEC,
            SendBonfiresToClient::registry,
            Bonfires.OWNER_NAMES,
            SendBonfiresToClient::ownerNames,
            SendBonfiresToClient::new
    );

    public SendBonfiresToClient() {
        this(new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys()), BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry(), BonfireRegistry.getOwnerNames(ServerLifecycleHooks.getCurrentServer()));
    }

    public SendBonfiresToClient(ServerPlayer player) {
        this(new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys()), BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry().getFilteredRegistry(DiscoveryHandler.getHandler(player).getDiscovered().keySet().stream().toList()), BonfireRegistry.getOwnerNames(ServerLifecycleHooks.getCurrentServer()));
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.setBonfiresFromServer(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
