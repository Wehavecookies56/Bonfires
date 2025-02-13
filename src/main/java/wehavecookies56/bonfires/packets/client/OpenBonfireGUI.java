package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OpenBonfireGUI(BlockPos pos, Map<UUID, String> ownerNames, BonfireRegistry registry, boolean canReinforce, List<RegistryKey<World>> dimensions) implements CustomPayload {

    public static final Id<OpenBonfireGUI> TYPE = new Id<>(Identifier.of(Bonfires.modid, "open_bonfires_gui"));

    public static final PacketCodec<PacketByteBuf, OpenBonfireGUI> STREAM_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            OpenBonfireGUI::pos,
            Bonfires.OWNER_NAMES,
            OpenBonfireGUI::ownerNames,
            BonfireRegistry.STREAM_CODEC,
            OpenBonfireGUI::registry,
            PacketCodecs.BOOL,
            OpenBonfireGUI::canReinforce,
            PacketCodecs.collection(ArrayList::new, RegistryKey.createPacketCodec(RegistryKeys.WORLD)),
            OpenBonfireGUI::dimensions,
            OpenBonfireGUI::new
    );

    public OpenBonfireGUI(BonfireTileEntity bonfire, Map<UUID, String> ownerNames, BonfireRegistry registry, boolean canReinforce, MinecraftServer server) {
        this(bonfire.getPos(), ownerNames, registry, canReinforce, new ArrayList<>(server.getWorldRegistryKeys()));
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.openBonfire(this);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
