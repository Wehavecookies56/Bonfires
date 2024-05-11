package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

public record DisplayBonfireTitle(Bonfire bonfire) implements Packet {

    public static final Type<DisplayBonfireTitle> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "display_bonfire_title"));

    public static final StreamCodec<FriendlyByteBuf, DisplayBonfireTitle> STREAM_CODEC = StreamCodec.composite(
            Bonfire.STREAM_CODEC,
            DisplayBonfireTitle::bonfire,
            DisplayBonfireTitle::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.displayBonfireTravelled(bonfire);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
