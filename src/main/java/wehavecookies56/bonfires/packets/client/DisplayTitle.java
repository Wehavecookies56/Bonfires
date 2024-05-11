package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

public record DisplayTitle(String title, String subtitle, int fadein, int stay, int fadeout) implements Packet {

    public static final Type<DisplayTitle> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "display_title"));

    public static final StreamCodec<FriendlyByteBuf, DisplayTitle> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            DisplayTitle::title,
            ByteBufCodecs.STRING_UTF8,
            DisplayTitle::subtitle,
            ByteBufCodecs.INT,
            DisplayTitle::fadein,
            ByteBufCodecs.INT,
            DisplayTitle::stay,
            ByteBufCodecs.INT,
            DisplayTitle::fadeout,
            DisplayTitle::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.displayTitle(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
