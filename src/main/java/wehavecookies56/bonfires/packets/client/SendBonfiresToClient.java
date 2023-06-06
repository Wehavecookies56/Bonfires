package wehavecookies56.bonfires.packets.client;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class SendBonfiresToClient extends Packet<SendBonfiresToClient> {

    public SendBonfiresToClient(PacketBuffer buffer) {
        super(buffer);
    }

    public List<RegistryKey<World>> dimensions;
    public BonfireRegistry registry;

    public SendBonfiresToClient() {
        dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
        registry = BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry();
    }

    @Override
    public void decode(PacketBuffer buffer) {
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(RegistryKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation()));
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeNbt(registry.writeToNBT(new CompoundNBT(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeResourceLocation(dimensions.get(i).location());
        }
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.setBonfiresFromServer(this));
    }
}
