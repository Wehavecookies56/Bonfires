package wehavecookies56.bonfires.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class LightBonfire extends Packet<LightBonfire> {

    private String name;
    private int x, y, z;
    private boolean isPublic, createScreenshot;

    public LightBonfire(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public LightBonfire(String name, BonfireTileEntity bonfire, boolean isPublic, boolean createScreenshot) {
        this.name = name;
        this.x = bonfire.getBlockPos().getX();
        this.y = bonfire.getBlockPos().getY();
        this.z = bonfire.getBlockPos().getZ();
        this.isPublic = isPublic;
        this.createScreenshot = createScreenshot;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.name = buffer.readUtf();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.isPublic = buffer.readBoolean();
        this.createScreenshot = buffer.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(name);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBoolean(isPublic);
        buffer.writeBoolean(createScreenshot);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        BlockPos pos = new BlockPos(x, y, z);
        Player player = context.getSender();
        if (player != null) {
            BonfireTileEntity te = (BonfireTileEntity) player.level().getBlockEntity(pos);
            if (te != null && !te.isLit()) {
                te.setLit(true);
                UUID id = UUID.randomUUID();
                te.createBonfire(name, id, player.getUUID(), isPublic);
                te.setID(id);
                player.level().setBlock(pos, player.level().getBlockState(pos).setValue(AshBonePileBlock.LIT, true), 2);
                ((ServerPlayer) player).setRespawnPosition(te.getLevel().dimension(), te.getBlockPos(), player.getYRot(), false, true);
                EstusHandler.getHandler(player).setLastRested(te.getID());
                BonfireLitTrigger.TRIGGER_BONFIRE_LIT.trigger((ServerPlayer) player);
                PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), te.getBonfireType(), te.isLit(), te.getID(), te));
                PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getHandler(player.level()).getRegistry().getBonfires()));
                PacketHandler.sendToAll(new SendBonfiresToClient());
                if (createScreenshot) {
                    PacketHandler.sendTo(new QueueBonfireScreenshot(name, id), (ServerPlayer) player);
                }
                PacketHandler.sendTo(new DisplayTitle(LocalStrings.TEXT_LIT, name, 15, 20, 15), (ServerPlayer) player);
                Bonfires.LOGGER.info("Bonfire" + "'" + name + "'" + " lit at: X" + x + " Y" + y + " Z" + z + " by " + player.getDisplayName().getString());
            }
        }
    }
}
