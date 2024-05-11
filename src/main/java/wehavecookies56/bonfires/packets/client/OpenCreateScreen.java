package wehavecookies56.bonfires.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public record OpenCreateScreen(BlockPos tePos) implements Packet {

    public static final Type<OpenCreateScreen> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "open_create_screen"));

    public static final StreamCodec<FriendlyByteBuf, OpenCreateScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenCreateScreen::tePos,
            OpenCreateScreen::new
    );

    public OpenCreateScreen(BonfireTileEntity te) {
        this(te.getBlockPos());
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            BonfireTileEntity te = (BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(tePos);
            ClientPacketHandler.openCreateScreen(te);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
