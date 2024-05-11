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

public record DeleteScreenshot(UUID uuid, String name) implements Packet {

    public static final Type<DeleteScreenshot> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "delete_screenshot"));

    public static final StreamCodec<FriendlyByteBuf, DeleteScreenshot> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            DeleteScreenshot::uuid,
            ByteBufCodecs.STRING_UTF8,
            DeleteScreenshot::name,
            DeleteScreenshot::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.deleteScreenshot(uuid, name);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
