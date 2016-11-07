package uk.co.wehavecookies56.bonfires;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncBonfire extends AbstractMessage.AbstractClientMessage<SyncBonfire> {

    boolean bonfire;
    int x, y, z;

    public SyncBonfire() {}

    public SyncBonfire(boolean bonfire, TileEntityBonfire entityBonfire) {
        this.bonfire = bonfire;
        this.x = entityBonfire.getPos().getX();
        this.y = entityBonfire.getPos().getY();
        this.z = entityBonfire.getPos().getZ();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.bonfire = buffer.readBoolean();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeBoolean(bonfire);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntityBonfire te = (TileEntityBonfire) player.worldObj.getTileEntity(pos);
        te.setBonfire(bonfire);
    }
}
