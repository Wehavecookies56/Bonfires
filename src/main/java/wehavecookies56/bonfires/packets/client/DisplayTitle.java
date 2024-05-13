package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;

public record DisplayTitle(String title, String subtitle, int fadein, int stay, int fadeout) implements CustomPayload {

    public static final Id<DisplayTitle> TYPE = new Id<>(new Identifier(Bonfires.modid, "display_title"));

    public static final PacketCodec<PacketByteBuf, DisplayTitle> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            DisplayTitle::title,
            PacketCodecs.STRING,
            DisplayTitle::subtitle,
            PacketCodecs.INTEGER,
            DisplayTitle::fadein,
            PacketCodecs.INTEGER,
            DisplayTitle::stay,
            PacketCodecs.INTEGER,
            DisplayTitle::fadeout,
            DisplayTitle::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.displayTitle(this);
        }
    }
}
