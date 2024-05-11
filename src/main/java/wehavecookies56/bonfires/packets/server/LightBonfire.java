package wehavecookies56.bonfires.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
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
public record LightBonfire(String name, BlockPos bonfireTE, boolean isPublic, boolean createScreenshot) implements Packet {

    public static final Type<LightBonfire> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "light_bonfire"));

    public static final StreamCodec<FriendlyByteBuf, LightBonfire> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            LightBonfire::name,
            BlockPos.STREAM_CODEC,
            LightBonfire::bonfireTE,
            ByteBufCodecs.BOOL,
            LightBonfire::isPublic,
            ByteBufCodecs.BOOL,
            LightBonfire::createScreenshot,
            LightBonfire::new
    );

    public LightBonfire(String name, BonfireTileEntity bonfire, boolean isPublic, boolean createScreenshot) {
        this(name, bonfire.getBlockPos(), isPublic, createScreenshot);
    }

    @Override
    public void handle(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        if (player != null) {
            BonfireTileEntity te = (BonfireTileEntity) player.level().getBlockEntity(bonfireTE);
            if (te != null && !te.isLit()) {
                te.setLit(true);
                UUID id = UUID.randomUUID();
                te.createBonfire(name, id, player.getUUID(), isPublic);
                te.setID(id);
                player.level().setBlock(bonfireTE, player.level().getBlockState(bonfireTE).setValue(AshBonePileBlock.LIT, true), 2);
                player.setRespawnPosition(te.getLevel().dimension(), te.getBlockPos(), player.getYRot(), false, true);
                EstusHandler.getHandler(player).setLastRested(te.getID());
                ((BonfireLitTrigger)BonfireLitTrigger.TRIGGER_BONFIRE_LIT.get()).trigger(player);
                PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), te.getBonfireType(), te.isLit(), te.getID(), te));
                PacketHandler.sendToAll(new SendBonfiresToClient());
                if (createScreenshot) {
                    PacketHandler.sendTo(new QueueBonfireScreenshot(name, id), player);
                }
                PacketHandler.sendTo(new DisplayTitle(LocalStrings.TEXT_LIT, name, 15, 20, 15), player);
                Bonfires.LOGGER.info("Bonfire" + "'" + name + "'" + " lit at: X" + bonfireTE.getX() + " Y" + bonfireTE.getY() + " Z" + bonfireTE.getZ() + " by " + player.getDisplayName().getString());
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
