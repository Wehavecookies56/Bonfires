package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class SendBonfiresToClient extends Packet<SendBonfiresToClient> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "send_bonfires_to_client");

    public SendBonfiresToClient(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public List<ResourceKey<Level>> dimensions;
    public BonfireRegistry registry;

    public SendBonfiresToClient() {
        dimensions = new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys());
        registry = BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry();
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            dimensions.add(ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation()));
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(registry.writeToNBT(new CompoundTag(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeResourceLocation(dimensions.get(i).location());
        }
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.setBonfiresFromServer(this);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
