package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.client.SyncSaveData;

public class RequestDimensionsFromServer extends Packet<RequestDimensionsFromServer> {

    public RequestDimensionsFromServer(PacketBuffer buffer) {
        super(buffer);
    }

    public RequestDimensionsFromServer() {}

    @Override
    public void decode(PacketBuffer buffer) {}

    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void handle(NetworkEvent.Context context) {
        PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(context.getSender().server).getRegistry().getBonfires()), context.getSender());
        PacketHandler.sendTo(new SendBonfiresToClient(), context.getSender());
    }
}
