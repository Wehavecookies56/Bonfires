package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;

public class RequestDimensionsFromServer extends Packet<RequestDimensionsFromServer> {

    public RequestDimensionsFromServer(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public RequestDimensionsFromServer() {}

    @Override
    public void decode(FriendlyByteBuf buffer) {}

    @Override
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        PacketHandler.sendTo(new SendBonfiresToClient(), context.getSender());
    }
}
