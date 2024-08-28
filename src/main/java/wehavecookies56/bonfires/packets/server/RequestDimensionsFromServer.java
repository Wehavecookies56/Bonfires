package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.client.SyncSaveData;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        ServerPlayer player = context.getSender();
        BonfireHandler.IBonfireHandler handler = BonfireHandler.getServerHandler(player.getServer());
        List<UUID> invalidBonfires = new ArrayList<>();
        for (Bonfire bonfire : handler.getRegistry().getBonfires().values()) {
            for (ServerLevel level : player.getServer().getAllLevels()) {
                if (level.dimension().equals(bonfire.getDimension())) {
                    if (level.getBlockEntity(bonfire.getPos()) instanceof BonfireTileEntity te) {
                        if (!te.getID().equals(bonfire.getId())) {
                            invalidBonfires.add(bonfire.getId());
                        }
                    } else {
                        invalidBonfires.add(bonfire.getId());
                    }
                }
            }
        }
        invalidBonfires.forEach(handler::removeBonfire);
        PacketHandler.sendTo(new SyncSaveData(BonfireHandler.getServerHandler(player.server).getRegistry().getBonfires()), player);
        if (BonfiresConfig.Common.bonfireDiscoveryMode) {
            PacketHandler.sendTo(new SendBonfiresToClient(player), player);
        } else {
            PacketHandler.sendTo(new SendBonfiresToClient(), player);
        }
    }
}
