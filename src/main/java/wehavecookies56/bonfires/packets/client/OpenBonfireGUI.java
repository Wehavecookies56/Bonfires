package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
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
    public List<ResourceKey<Level>> dimensions;
    public boolean canReinforce;

    public OpenBonfireGUI(FriendlyByteBuf buffer) {
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
    public void decode(FriendlyByteBuf buffer) {
        canReinforce = buffer.readBoolean();
        ownerName = buffer.readUtf();
        tileEntity = buffer.readBlockPos();
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; ++i) {
            dimensions.add(ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation()));
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(canReinforce);
        buffer.writeUtf(ownerName);
        buffer.writeBlockPos(tileEntity);
        buffer.writeNbt(registry.writeToNBT(new CompoundTag(), registry.getBonfires()));
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
