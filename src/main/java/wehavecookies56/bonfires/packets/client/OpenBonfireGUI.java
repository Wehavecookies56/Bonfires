package wehavecookies56.bonfires.packets.client;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.ArrayList;
import java.util.List;

public class OpenBonfireGUI extends Packet<OpenBonfireGUI> {

    public BlockPos tileEntity;
    public String ownerName;
    public BonfireRegistry registry;
    public List<RegistryKey<World>> dimensions;
    public boolean canReinforce;

    public OpenBonfireGUI(PacketBuffer buffer) {
        super(buffer);
    }

    public OpenBonfireGUI(BonfireTileEntity bonfire, String ownerName, BonfireRegistry registry, boolean canReinforce) {
        this.ownerName = ownerName;
        this.tileEntity = bonfire.getBlockPos();
        this.registry = registry;
        this.dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
        this.canReinforce = canReinforce;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        canReinforce = buffer.readBoolean();
        ownerName = buffer.readUtf();
        tileEntity = buffer.readBlockPos();
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; ++i) {
            dimensions.add(RegistryKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation()));
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(canReinforce);
        buffer.writeUtf(ownerName);
        buffer.writeBlockPos(tileEntity);
        buffer.writeNbt(registry.writeToNBT(new CompoundNBT(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeResourceLocation(dimensions.get(i).location());
        }
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.openBonfire(this));
    }
}
