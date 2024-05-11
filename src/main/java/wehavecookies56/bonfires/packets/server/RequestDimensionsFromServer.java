package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;

public record RequestDimensionsFromServer() implements Packet {

    public static final Type<RequestDimensionsFromServer> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "request_dimensions_from_server"));

    public static final StreamCodec<FriendlyByteBuf, RequestDimensionsFromServer> STREAM_CODEC = StreamCodec.of((byteBuf, packet) -> {}, byteBuf -> new RequestDimensionsFromServer());

    @Override
    public void handle(IPayloadContext context) {
        PacketHandler.sendTo(new SendBonfiresToClient(), (ServerPlayer) context.player());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
