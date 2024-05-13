package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;

public record DisplayBonfireTitle(Bonfire bonfire) implements CustomPayload {

    public static Id<DisplayBonfireTitle> TYPE = new Id<>(new Identifier(Bonfires.modid, "display_bonfire_title"));

    public static final PacketCodec<PacketByteBuf, DisplayBonfireTitle> STREAM_CODEC = PacketCodec.tuple(
            Bonfire.STREAM_CODEC,
            DisplayBonfireTitle::bonfire,
            DisplayBonfireTitle::new
    );

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.displayBonfireTravelled(bonfire);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
