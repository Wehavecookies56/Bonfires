package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncSaveData extends AbstractMessage.AbstractClientMessage<SyncSaveData> {

    Map<UUID, Bonfire> bonfires;

    public SyncSaveData() {}

    public SyncSaveData(Map<UUID, Bonfire> bonfires) {
        this.bonfires = bonfires;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        bonfires = new HashMap<UUID, Bonfire>();
        while (buffer.isReadable()) {
            UUID key = buffer.readUuid();
            String name = buffer.readStringFromBuffer(100);
            UUID owner = buffer.readUuid();
            BlockPos pos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            int dim = buffer.readInt();
            boolean isPublic = buffer.readBoolean();
            Bonfire bonfire = new Bonfire(name, key, owner, pos, dim, isPublic);
            bonfires.put(key, bonfire);
        }
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        Iterator<Map.Entry<UUID, Bonfire>> it = bonfires.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Bonfire> pair = (Map.Entry<UUID, Bonfire>) it.next();
            buffer.writeUuid(pair.getKey());
            Bonfire bonfire = pair.getValue();
            buffer.writeString(bonfire.getName());
            buffer.writeUuid(bonfire.getOwner());
            buffer.writeDouble(bonfire.getPos().getX());
            buffer.writeDouble(bonfire.getPos().getY());
            buffer.writeDouble(bonfire.getPos().getZ());
            buffer.writeInt(bonfire.getDimension());
            buffer.writeBoolean(bonfire.isPublic());
        }
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BonfireRegistry.INSTANCE.clearBonfires();
        Iterator<Map.Entry<UUID, Bonfire>> it = bonfires.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Bonfire> pair = (Map.Entry<UUID, Bonfire>) it.next();
            BonfireWorldSavedData.get(player.getEntityWorld()).addBonfire(pair.getValue());
        }
    }
}
