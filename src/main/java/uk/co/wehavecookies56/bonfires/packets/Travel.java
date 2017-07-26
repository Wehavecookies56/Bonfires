package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;

import java.io.IOException;

/**
 * Created by Toby on 06/11/2016.
 */
public class Travel extends AbstractMessage.AbstractServerMessage<Travel> {

    private int x;
    private int y;
    private int z;
    private int dim;

    public Travel() {}

    public Travel(Bonfire bonfire) {
        this.x = bonfire.getPos().getX();
        this.y = bonfire.getPos().getY();
        this.z = bonfire.getPos().getZ();
        this.dim = bonfire.getDimension();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.dim = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(dim);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BlockPos pos = new BlockPos(x, y, z);
        BonfireTeleporter tp = new BonfireTeleporter(player.getServer().getWorld(dim));
        tp.teleport(player, player.world, pos, dim);
    }
}
