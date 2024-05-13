package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;

import java.util.UUID;

public record QueueBonfireScreenshot(String name, UUID uuid) implements CustomPayload {

    public static final Id<QueueBonfireScreenshot> TYPE = new Id<>(new Identifier(Bonfires.modid, "queue_bonfire_screenshot"));

    public static final PacketCodec<PacketByteBuf, QueueBonfireScreenshot> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            QueueBonfireScreenshot::name,
            Uuids.PACKET_CODEC,
            QueueBonfireScreenshot::uuid,
            QueueBonfireScreenshot::new
    );

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.queueBonfireScreenshot(name, uuid);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
