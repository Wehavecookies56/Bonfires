package wehavecookies56.bonfires.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;
import wehavecookies56.bonfires.world.BonfireTeleporter;

public record Travel(BlockPos pos, ResourceKey<Level> dim) implements Packet {

    public static final Type<Travel> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "travel"));

    public static final StreamCodec<FriendlyByteBuf, Travel> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            Travel::pos,
            ResourceKey.streamCodec(Registries.DIMENSION),
            Travel::dim,
            Travel::new
    );

    public Travel(Bonfire bonfire) {
        this(bonfire.getPos(), bonfire.getDimension());
    }

    @Override
    public void handle(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        BonfireTileEntity te = (BonfireTileEntity) player.level().getBlockEntity(pos);
        BonfireTeleporter.travelToBonfire(player, pos, dim);
        if (!BonfiresConfig.Common.disableBonfireRespawn) {
            player.setRespawnPosition(dim, pos, player.getYRot(), false, true);
        }
        EstusHandler.getHandler(player).setLastRested(te.getID());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
