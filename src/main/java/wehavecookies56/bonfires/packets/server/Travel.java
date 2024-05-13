package wehavecookies56.bonfires.packets.server;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.world.BonfireTeleporter;

public record Travel(BlockPos pos, RegistryKey<World> dim) implements CustomPayload {

    public static final Id<Travel> TYPE = new Id<>(new Identifier(Bonfires.modid, "travel"));

    public static final PacketCodec<PacketByteBuf, Travel> STREAM_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            Travel::pos,
            RegistryKey.createPacketCodec(RegistryKeys.WORLD),
            Travel::dim,
            Travel::new
    );

    public Travel(Bonfire bonfire) {
        this(bonfire.getPos(), bonfire.getDimension());
    }

    public void handle(ServerPlayerEntity player) {
        player.incrementStat(Bonfires.TIMES_TRAVELLED);
        BonfireTeleporter.travelToBonfire(player, pos, dim);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
