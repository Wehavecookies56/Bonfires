package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;

public class RequestDimensionsFromServer extends Packet<RequestDimensionsFromServer> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "request_dimensions_from_server");
    public RequestDimensionsFromServer(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public RequestDimensionsFromServer() {}

    @Override
    public void decode(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {}

    @Override
    public void handle(PlayPayloadContext context) {
        PacketHandler.sendTo(new SendBonfiresToClient(), (ServerPlayer) context.player().get());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
