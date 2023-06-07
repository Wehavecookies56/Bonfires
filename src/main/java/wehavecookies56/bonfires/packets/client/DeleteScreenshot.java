package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class DeleteScreenshot extends Packet<DeleteScreenshot> {

    public DeleteScreenshot(PacketBuffer buffer) {
        super(buffer);
    }

    UUID uuid;
    String name;

    public DeleteScreenshot(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        uuid = buffer.readUUID();
        name = buffer.readUtf();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUtf(name);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.deleteScreenshot(uuid, name));
    }
}
