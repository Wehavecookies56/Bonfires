package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class SendDimensionsToClient extends Packet<SendDimensionsToClient> {

    public SendDimensionsToClient(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public List<ResourceKey<Level>> dimensions;

    public SendDimensionsToClient() {
        dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation()));
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeResourceLocation(dimensions.get(i).location());
        }
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.setDimensionsFromServer(this));
    }
}
