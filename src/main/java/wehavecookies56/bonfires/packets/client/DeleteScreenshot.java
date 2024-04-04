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

public class DeleteScreenshot implements FabricPacket {

    public static final PacketType<DeleteScreenshot> TYPE = PacketType.create(new Identifier(Bonfires.modid, "delete_screenshot"), DeleteScreenshot::new);

    UUID uuid;
    String name;

    public DeleteScreenshot(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public DeleteScreenshot(PacketByteBuf buffer) {
        decode(buffer);
    }

    public void decode(PacketByteBuf buffer) {
        uuid = buffer.readUuid();
        name = buffer.readString();
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeUuid(uuid);
        buffer.writeString(name);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.deleteScreenshot(uuid, name);
        }
    }
}
