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
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.world.BonfireTeleporter;

/**
 * Created by Toby on 06/11/2016.
 */
public record Travel(BlockPos pos, ResourceKey<Level> dim) implements Packet {

    public static final Type<Travel> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "travel"));

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
        BonfireTeleporter.travelToBonfire(player, pos, dim);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
