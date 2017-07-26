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

    private boolean bonfire;
    private boolean lit;
    private UUID id;
    private int x;
    private int y;
    private int z;
    private TileEntityBonfire.BonfireType type;

    @SuppressWarnings("unused")
    public SyncBonfire() {}

    public SyncBonfire(boolean bonfire, TileEntityBonfire.BonfireType type, boolean lit, UUID id, TileEntityBonfire entityBonfire) {
        this.bonfire = bonfire;
        this.type = type;
        this.lit = lit;
        this.id = id;
        this.x = entityBonfire.getPos().getX();
        this.y = entityBonfire.getPos().getY();
        this.z = entityBonfire.getPos().getZ();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.bonfire = buffer.readBoolean();
        this.type = TileEntityBonfire.BonfireType.values()[buffer.readInt()];
        this.lit = buffer.readBoolean();
        if(this.lit)
            this.id = buffer.readUniqueId();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeBoolean(bonfire);
        buffer.writeInt(type.ordinal());
        buffer.writeBoolean(lit);
        if(lit)
            buffer.writeUniqueId(id);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        BlockPos pos = new BlockPos(x, y, z);
        if (player.world.getTileEntity(pos) != null && player.world.getTileEntity(pos) instanceof TileEntityBonfire) {
            TileEntityBonfire te = (TileEntityBonfire) player.world.getTileEntity(pos);
            if (te != null) {
                te.setBonfire(bonfire);
                te.setBonfireType(type);
                te.setLit(lit);
                if (lit)
                    te.setID(id);
            }
        }
    }
}
