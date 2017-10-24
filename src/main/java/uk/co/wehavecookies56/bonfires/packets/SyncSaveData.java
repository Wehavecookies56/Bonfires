package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
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

    private Map<UUID, Bonfire> bonfires;

    @SuppressWarnings("unused")
    public SyncSaveData() {}

    public SyncSaveData(Map<UUID, Bonfire> bonfires) {
        this.bonfires = bonfires;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        bonfires = new HashMap<UUID, Bonfire>();
        while (buffer.isReadable()) {
            UUID key = buffer.readUniqueId();
            String name = buffer.readString(100);
            UUID owner = buffer.readUniqueId();
            BlockPos pos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            int dim = buffer.readInt();
            boolean isPublic = buffer.readBoolean();
            Bonfire bonfire = new Bonfire(name, key, owner, pos, dim, isPublic);
            bonfires.put(key, bonfire);
        }
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        for (Map.Entry<UUID, Bonfire> pair : bonfires.entrySet()) {
            buffer.writeUniqueId(pair.getKey());
            Bonfire bonfire = pair.getValue();
            buffer.writeString(bonfire.getName());
            buffer.writeUniqueId(bonfire.getOwner());
            buffer.writeDouble(bonfire.getPos().getX());
            buffer.writeDouble(bonfire.getPos().getY());
            buffer.writeDouble(bonfire.getPos().getZ());
            buffer.writeInt(bonfire.getDimension());
            buffer.writeBoolean(bonfire.isPublic());
        }
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BonfireWorldSavedData.get(player.world).bonfires.clearBonfires();
        for (Map.Entry<UUID, Bonfire> pair : bonfires.entrySet()) {
            BonfireWorldSavedData.get(player.getEntityWorld()).addBonfire(pair.getValue());
        }
    }
}
