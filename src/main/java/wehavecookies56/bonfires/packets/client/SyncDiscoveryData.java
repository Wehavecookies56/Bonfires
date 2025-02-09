package wehavecookies56.bonfires.packets.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.DiscoveryHandler;
import wehavecookies56.bonfires.packets.Packet;

public record SyncDiscoveryData(CompoundTag nbt) implements Packet {

    public static final Type<SyncDiscoveryData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "sync_discovery_data"));

    public static final StreamCodec<FriendlyByteBuf, SyncDiscoveryData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SyncDiscoveryData::nbt,
            SyncDiscoveryData::new
    );

    public SyncDiscoveryData(DiscoveryHandler.IDiscoveryHandler handler, Player player) {
        this(handler.serializeNBT(player.level().registryAccess()));
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.syncDiscoveryData(this.nbt);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
