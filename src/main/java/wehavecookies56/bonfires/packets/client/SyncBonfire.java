package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public record SyncBonfire(boolean bonfire, boolean lit, UUID id, BlockPos pos, BonfireTileEntity.BonfireType bonfireType) implements Packet {

    public static final Type<SyncBonfire> TYPE = new Type<>(new ResourceLocation(Bonfires.modid, "sync_bonfire"));

    public static final StreamCodec<FriendlyByteBuf, SyncBonfire> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SyncBonfire::bonfire,
            ByteBufCodecs.BOOL,
            SyncBonfire::lit,
            Bonfires.NULLABLE_UUID,
            SyncBonfire::id,
            BlockPos.STREAM_CODEC,
            SyncBonfire::pos,
            BonfireTileEntity.BonfireType.STREAM_CODEC,
            SyncBonfire::bonfireType,
            SyncBonfire::new
    );

    public SyncBonfire(boolean bonfire, BonfireTileEntity.BonfireType type, boolean lit, UUID id, BonfireTileEntity entityBonfire) {
        this(bonfire, lit, id, entityBonfire.getBlockPos(), type);
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.syncBonfire(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
