package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class DeleteScreenshot extends Packet<DeleteScreenshot> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "delete_screenshot");

    UUID uuid;
    String name;

    public DeleteScreenshot(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public DeleteScreenshot(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        uuid = buffer.readUUID();
        name = buffer.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUtf(name);
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.deleteScreenshot(uuid, name);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
