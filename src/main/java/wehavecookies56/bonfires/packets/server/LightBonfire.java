package wehavecookies56.bonfires.packets.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.client.SyncBonfire;
import wehavecookies56.bonfires.packets.client.SyncSaveData;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class LightBonfire extends Packet<LightBonfire> {

    private String name;
    private int x, y, z;
    private boolean isPublic;

    public LightBonfire(PacketBuffer buffer) {
        super(buffer);
    }

    public LightBonfire(String name, BonfireTileEntity bonfire, boolean isPublic) {
        this.name = name;
        this.x = bonfire.getBlockPos().getX();
        this.y = bonfire.getBlockPos().getY();
        this.z = bonfire.getBlockPos().getZ();
        this.isPublic = isPublic;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.name = buffer.readUtf();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.isPublic = buffer.readBoolean();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUtf(name);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBoolean(isPublic);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        BlockPos pos = new BlockPos(x, y, z);
        PlayerEntity player = context.getSender();
        if (player != null) {
            BonfireTileEntity te = (BonfireTileEntity) player.level.getBlockEntity(pos);
            if (te != null) {
                te.setLit(true);
                UUID id = UUID.randomUUID();
                te.createBonfire(name, id, player.getUUID(), isPublic);
                te.setID(id);
                player.level.setBlock(pos, player.level.getBlockState(pos).setValue(AshBonePileBlock.LIT, true), 2);
                ((ServerPlayerEntity) player).setRespawnPosition(te.getLevel().dimension(), te.getBlockPos(), player.yRot, false, true);
                EstusHandler.getHandler(player).setLastRested(te.getID());
                BonfireLitTrigger.TRIGGER_BONFIRE_LIT.trigger((ServerPlayerEntity) player);
                PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), te.getBonfireType(), te.isLit(), te.getID(), te));
                PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getHandler(player.level).getRegistry().getBonfires()));
                PacketHandler.sendTo(new DisplayTitle(LocalStrings.TEXT_LIT, name, 15, 20, 15), (ServerPlayerEntity) player);
                Bonfires.LOGGER.info("Bonfire" + "'" + name + "'" + " lit at: X" + x + " Y" + y + " Z" + z + " by " + player.getDisplayName().getString());
            }
        }
    }
}
