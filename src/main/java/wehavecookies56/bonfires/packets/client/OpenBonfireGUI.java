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

public record OpenBonfireGUI(BlockPos pos, String ownerName, BonfireRegistry registry, boolean canReinforce, List<RegistryKey<World>> dimensions) implements CustomPayload {

    public static final Id<OpenBonfireGUI> TYPE = new Id<>(new Identifier(Bonfires.modid, "open_bonfires_gui"));

    public static final PacketCodec<PacketByteBuf, OpenBonfireGUI> STREAM_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            OpenBonfireGUI::pos,
            PacketCodecs.STRING,
            OpenBonfireGUI::ownerName,
            BonfireRegistry.STREAM_CODEC,
            OpenBonfireGUI::registry,
            PacketCodecs.BOOL,
            OpenBonfireGUI::canReinforce,
            PacketCodecs.collection(ArrayList::new, RegistryKey.createPacketCodec(RegistryKeys.WORLD)),
            OpenBonfireGUI::dimensions,
            OpenBonfireGUI::new
    );

    public OpenBonfireGUI(BonfireTileEntity bonfire, String ownerName, BonfireRegistry registry, boolean canReinforce, MinecraftServer server) {
        this(bonfire.getPos(), ownerName, registry, canReinforce, new ArrayList<>(server.getWorldRegistryKeys()));
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
