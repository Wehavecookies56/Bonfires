package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.client.SyncSaveData;

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
    public void handle(NetworkEvent.Context context) {
        PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(context.getSender().server).getRegistry().getBonfires()), context.getSender());
        PacketHandler.sendTo(new SendBonfiresToClient(), context.getSender());
    }
}
