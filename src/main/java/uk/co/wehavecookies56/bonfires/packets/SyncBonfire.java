package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncBonfire extends AbstractMessage.AbstractClientMessage<SyncBonfire> {

    boolean bonfire, lit;
    UUID id;
    int x, y, z;

    public SyncBonfire() {}

    public SyncBonfire(boolean bonfire, boolean lit, UUID id, TileEntityBonfire entityBonfire) {
        this.bonfire = bonfire;
        this.lit = lit;
        this.id = id;
        this.x = entityBonfire.getPos().getX();
        this.y = entityBonfire.getPos().getY();
        this.z = entityBonfire.getPos().getZ();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.bonfire = buffer.readBoolean();
        this.lit = buffer.readBoolean();
        if(this.lit)
            this.id = buffer.readUuid();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeBoolean(bonfire);
        buffer.writeBoolean(lit);
        if(lit)
            buffer.writeUuid(id);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntityBonfire te = (TileEntityBonfire) player.worldObj.getTileEntity(pos);
        te.setBonfire(bonfire);
        te.setLit(lit);
        if(lit)
            te.setID(id);
    }
}
