package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;

public record RequestDimensionsFromServer() implements CustomPayload {

    public static final Id<RequestDimensionsFromServer> TYPE = new Id<>(new Identifier(Bonfires.modid, "request_dimensions_from_server"));

    public static final PacketCodec<PacketByteBuf, RequestDimensionsFromServer> STREAM_CODEC = PacketCodec.of((byteBuf, packet) -> {}, byteBuf -> new RequestDimensionsFromServer());

    public void handle(ServerPlayerEntity player) {
        //PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(context.getSender().server).getRegistry().getBonfires()), context.getSender());
        PacketHandler.sendTo(new SendBonfiresToClient(player.server), player);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
