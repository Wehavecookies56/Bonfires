package wehavecookies56.bonfires.packets.server;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;

public class RequestDimensionsFromServer implements FabricPacket {

    public static final PacketType<RequestDimensionsFromServer> TYPE = PacketType.create(new Identifier(Bonfires.modid, "request_dimensions_from_server"), RequestDimensionsFromServer::new);

    public RequestDimensionsFromServer(PacketByteBuf buffer) {
        decode(buffer);
    }

    public RequestDimensionsFromServer() {}

    public void decode(PacketByteBuf buffer) {}

    @Override
    public void write(PacketByteBuf buffer) {}

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle(ServerPlayerEntity player) {
        //PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(context.getSender().server).getRegistry().getBonfires()), context.getSender());
        PacketHandler.sendTo(new SendBonfiresToClient(player.server), player);
    }
}
