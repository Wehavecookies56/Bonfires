package wehavecookies56.bonfires.packets.server;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        BonfireHandler.IBonfireHandler handler = BonfireHandler.getServerHandler(player.getServer());
        List<UUID> invalidBonfires = new ArrayList<>();
        for (Bonfire bonfire : handler.getRegistry().getBonfires().values()) {
            for (ServerWorld level : player.getServer().getWorlds()) {
                if (level.getRegistryKey().equals(bonfire.getDimension())) {
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
        PacketHandler.sendTo(new SendBonfiresToClient(player.server), player);
    }
}
