package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;

import java.util.UUID;

public class QueueBonfireScreenshot implements FabricPacket {

    public static final PacketType<QueueBonfireScreenshot> TYPE = PacketType.create(new Identifier(Bonfires.modid, "queue_bonfire_screenshot"), QueueBonfireScreenshot::new);

    UUID uuid;
    String name;

    public QueueBonfireScreenshot(String name, UUID uuid) {
        this.uuid = uuid;
        this.name = name;
    }

    public QueueBonfireScreenshot(PacketByteBuf buffer) {
        decode(buffer);
    }

    public void decode(PacketByteBuf buffer) {
        name = buffer.readString();
        uuid = buffer.readUuid();
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeString(name);
        buffer.writeUuid(uuid);
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.queueBonfireScreenshot(name, uuid);
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
