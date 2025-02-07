package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.*;

public class OpenBonfireGUI implements FabricPacket {

    public static final PacketType<OpenBonfireGUI> TYPE = PacketType.create(new Identifier(Bonfires.modid, "open_bonfires_gui"), OpenBonfireGUI::new);

    public BlockPos tileEntity;
    public Map<UUID, String>  ownerNames;
    public BonfireRegistry registry;
    public List<RegistryKey<World>> dimensions;
    public boolean canReinforce;

    public OpenBonfireGUI(PacketByteBuf buffer) {
        decode(buffer);
    }

    public OpenBonfireGUI(BonfireTileEntity bonfire, Map<UUID, String> ownerNames, BonfireRegistry registry, boolean canReinforce, MinecraftServer server) {
        this.ownerNames = ownerNames;
        this.tileEntity = bonfire.getPos();
        this.registry = registry;
        this.dimensions = new ArrayList<>(server.getWorldRegistryKeys());
        this.canReinforce = canReinforce;
    }

    public void decode(PacketByteBuf buffer) {
        canReinforce = buffer.readBoolean();
        NbtCompound owners = buffer.readNbt();
        ownerNames = new HashMap<>();
        owners.getKeys().forEach(s -> {
            ownerNames.put(UUID.fromString(s), owners.getString(s));
        });
        tileEntity = buffer.readBlockPos();
        registry = new BonfireRegistry();
        registry.readFromNBT(buffer.readNbt(), registry.getBonfires());
        dimensions = new ArrayList<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; ++i) {
            dimensions.add(RegistryKey.of(RegistryKeys.WORLD, buffer.readIdentifier()));
        }
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeBoolean(canReinforce);
        NbtCompound owners = new NbtCompound();
        ownerNames.forEach((uuid, s) -> owners.putString(uuid.toString(), s));
        buffer.writeNbt(owners);
        buffer.writeBlockPos(tileEntity);
        buffer.writeNbt(registry.writeToNBT(new NbtCompound(), registry.getBonfires()));
        buffer.writeVarInt(dimensions.size());
        for (int i = 0; i < dimensions.size(); ++i) {
            buffer.writeIdentifier(dimensions.get(i).getValue());
        }
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.openBonfire(this);
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
