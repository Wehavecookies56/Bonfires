package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        ServerPlayer player = (ServerPlayer) context.player().get();
        BonfireHandler handler = BonfireHandler.getServerHandler(player.getServer());
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
        PacketHandler.sendTo(new SendBonfiresToClient(), player);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
