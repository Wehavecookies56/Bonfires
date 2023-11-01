package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

import java.util.UUID;

public class QueueBonfireScreenshot extends Packet<QueueBonfireScreenshot> {

    public QueueBonfireScreenshot(FriendlyByteBuf buffer) {
        super(buffer);
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
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(name);
        buffer.writeUUID(uuid);
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.queueBonfireScreenshot(name, uuid));
    }
}
