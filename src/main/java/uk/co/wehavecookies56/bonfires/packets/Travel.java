package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class Travel extends AbstractMessage.AbstractServerMessage<Travel> {

    int x, y, z, dim;

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
        BonfireTeleporter tp = new BonfireTeleporter(player.getServer().worldServerForDimension(dim));
        tp.teleport(player, player.world, pos, dim);
    }
}
