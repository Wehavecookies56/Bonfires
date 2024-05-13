package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.client.QueueBonfireScreenshot;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

public record LightBonfire(String name, BlockPos bonfireTE, boolean isPublic, boolean createScreenshot) implements CustomPayload {

    public static final Id<LightBonfire> TYPE = new Id<>(new Identifier(Bonfires.modid, "light_bonfire"));

    public static final PacketCodec<PacketByteBuf, LightBonfire> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            LightBonfire::name,
            BlockPos.PACKET_CODEC,
            LightBonfire::bonfireTE,
            PacketCodecs.BOOL,
            LightBonfire::isPublic,
            PacketCodecs.BOOL,
            LightBonfire::createScreenshot,
            LightBonfire::new
    );

    public LightBonfire(String name, BonfireTileEntity bonfire, boolean isPublic, boolean createScreenshot) {
        this(name, bonfire.getPos(), isPublic, createScreenshot);
    }

    public void handle(ServerPlayerEntity player) {
        if (player != null) {
            BonfireTileEntity te = (BonfireTileEntity) player.getWorld().getBlockEntity(bonfireTE);
            if (te != null && !te.isLit()) {
                te.setLit(true);
                UUID id = UUID.randomUUID();
                te.createBonfire(name, id, player.getUuid(), isPublic);
                te.setID(id);
                player.getWorld().setBlockState(bonfireTE, player.getWorld().getBlockState(bonfireTE).with(AshBonePileBlock.LIT, true), 2);
                player.setSpawnPoint(te.getWorld().getRegistryKey(), te.getPos(), player.getYaw(), false, true);
                EstusHandler.getHandler(player).setLastRested(te.getID());
                BonfireLitTrigger.INSTANCE.trigger(player);
                player.incrementStat(Bonfires.BONFIRES_LIT);
                //PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), te.getBonfireType(), te.isLit(), te.getID(), te));
                //PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getHandler(player.level()).getRegistry().getBonfires()));
                PacketHandler.sendToAll(new SendBonfiresToClient(player.server), player.server);
                if (createScreenshot) {
                    PacketHandler.sendTo(new QueueBonfireScreenshot(name, id), player);
                }
                PacketHandler.sendTo(new DisplayTitle(LocalStrings.TEXT_LIT, name, 15, 20, 15), player);
                Bonfires.LOGGER.info("Bonfire" + "'" + name + "'" + " lit at: X" + bonfireTE.getX() + " Y" + bonfireTE.getY() + " Z" + bonfireTE.getZ() + " by " + player.getDisplayName().getString());
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}