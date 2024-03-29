package wehavecookies56.bonfires.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.client.QueueBonfireScreenshot;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.client.SyncBonfire;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class LightBonfire extends Packet<LightBonfire> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "light_bonfire");

    private String name;
    private int x, y, z;
    private boolean isPublic, createScreenshot;

    public LightBonfire(FriendlyByteBuf buffer) {
        decode(buffer);
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
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(name);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBoolean(isPublic);
        buffer.writeBoolean(createScreenshot);
    }

    @Override
    public void handle(PlayPayloadContext context) {
        BlockPos pos = new BlockPos(x, y, z);
        ServerPlayer player = (ServerPlayer) context.player().get();
        if (player != null) {
            BonfireTileEntity te = (BonfireTileEntity) player.level().getBlockEntity(pos);
            if (te != null && !te.isLit()) {
                te.setLit(true);
                UUID id = UUID.randomUUID();
                te.createBonfire(name, id, player.getUUID(), isPublic);
                te.setID(id);
                player.level().setBlock(pos, player.level().getBlockState(pos).setValue(AshBonePileBlock.LIT, true), 2);
                player.setRespawnPosition(te.getLevel().dimension(), te.getBlockPos(), player.getYRot(), false, true);
                EstusHandler.getHandler(player).setLastRested(te.getID());
                ((BonfireLitTrigger)BonfireLitTrigger.TRIGGER_BONFIRE_LIT.get()).trigger(player);
                PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), te.getBonfireType(), te.isLit(), te.getID(), te));
                PacketHandler.sendToAll(new SendBonfiresToClient());
                if (createScreenshot) {
                    PacketHandler.sendTo(new QueueBonfireScreenshot(name, id), player);
                }
                PacketHandler.sendTo(new DisplayTitle(LocalStrings.TEXT_LIT, name, 15, 20, 15), player);
                Bonfires.LOGGER.info("Bonfire" + "'" + name + "'" + " lit at: X" + x + " Y" + y + " Z" + z + " by " + player.getDisplayName().getString());
            }
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
