package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class QueueBonfireScreenshot extends Packet<QueueBonfireScreenshot> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "queue_bonfire_screenshot");

    public QueueBonfireScreenshot(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    UUID uuid;
    String name;
    public QueueBonfireScreenshot(String name, UUID uuid) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        name = buffer.readUtf();
        uuid = buffer.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(name);
        buffer.writeUUID(uuid);
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.queueBonfireScreenshot(name, uuid);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
