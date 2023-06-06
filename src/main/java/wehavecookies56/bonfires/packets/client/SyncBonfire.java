package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.time.Instant;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncBonfire extends Packet<SyncBonfire> {

    public boolean bonfire;
    public boolean lit;
    public UUID id;
    public int x;
    public int y;
    public int z;
    public BonfireTileEntity.BonfireType type;
    public Instant time;

    public SyncBonfire(PacketBuffer buffer) {
        super(buffer);
    }

    public SyncBonfire(boolean bonfire, BonfireTileEntity.BonfireType type, boolean lit, UUID id, BonfireTileEntity entityBonfire) {
        this.bonfire = bonfire;
        this.type = type;
        this.lit = lit;
        this.id = id;
        this.x = entityBonfire.getBlockPos().getX();
        this.y = entityBonfire.getBlockPos().getY();
        this.z = entityBonfire.getBlockPos().getZ();
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.bonfire = buffer.readBoolean();
        this.type = BonfireTileEntity.BonfireType.values()[buffer.readInt()];
        this.lit = buffer.readBoolean();
        if(this.lit)
            this.id = buffer.readUUID();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(bonfire);
        buffer.writeInt(type.ordinal());
        buffer.writeBoolean(lit);
        if(lit)
            buffer.writeUUID(id);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.syncBonfire(this));
    }
}
