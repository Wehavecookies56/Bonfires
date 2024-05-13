package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;

import java.util.UUID;

public record DeleteScreenshot(UUID uuid, String name) implements CustomPayload {

    public static final Id<DeleteScreenshot> TYPE = new Id<>(new Identifier(Bonfires.modid, "delete_screenshot"));

    public static final PacketCodec<PacketByteBuf, DeleteScreenshot> STREAM_CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC,
            DeleteScreenshot::uuid,
            PacketCodecs.STRING,
            DeleteScreenshot::name,
            DeleteScreenshot::new
    );

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.deleteScreenshot(uuid, name);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
