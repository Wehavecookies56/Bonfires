package wehavecookies56.bonfires.packets.client;


import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public record OpenCreateScreen(BlockPos tePos) implements CustomPayload {

    public static final Id<OpenCreateScreen> TYPE = new Id<>(new Identifier(Bonfires.modid, "open_create_screen"));

    public static final PacketCodec<PacketByteBuf, OpenCreateScreen> STREAM_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            OpenCreateScreen::tePos,
            OpenCreateScreen::new
    );

    public OpenCreateScreen(BonfireTileEntity te) {
        this(te.getPos());
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.openCreateScreen((BonfireTileEntity) MinecraftClient.getInstance().world.getBlockEntity(tePos));
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
