package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class SendDimensionsToClient extends Packet<SendDimensionsToClient> {

    public SendDimensionsToClient(PacketBuffer buffer) {
        super(buffer);
    }

    public List<RegistryKey<World>> dimensions;

    public SendDimensionsToClient() {
        dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
    }

    @Override
    public void decode(PacketBuffer buffer) {
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(RegistryKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation()));
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
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
