package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public record QueueBonfireScreenshot(String name, UUID uuid) implements Packet {

    public static final Type<QueueBonfireScreenshot> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "queue_bonfire_screenshot"));

    public static final StreamCodec<FriendlyByteBuf, QueueBonfireScreenshot> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            QueueBonfireScreenshot::name,
            UUIDUtil.STREAM_CODEC,
            QueueBonfireScreenshot::uuid,
            QueueBonfireScreenshot::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.queueBonfireScreenshot(name, uuid);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
