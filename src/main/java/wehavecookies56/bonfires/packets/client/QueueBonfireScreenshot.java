package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class QueueBonfireScreenshot extends Packet<QueueBonfireScreenshot> {

    public QueueBonfireScreenshot(PacketBuffer buffer) {
        super(buffer);
    }

    UUID uuid;
    String name;
    public QueueBonfireScreenshot(String name, UUID uuid) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        name = buffer.readUtf();
        uuid = buffer.readUUID();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(name);
        buffer.writeUUID(uuid);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.queueBonfireScreenshot(name, uuid));
    }
}
