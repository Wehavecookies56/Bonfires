package wehavecookies56.bonfires.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.world.BonfireTeleporter;

/**
 * Created by Toby on 06/11/2016.
 */
public class Travel extends Packet<Travel> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "travel");

    private int x;
    private int y;
    private int z;
    private ResourceKey<Level> dim;

    public Travel(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public Travel(Bonfire bonfire) {
        this.x = bonfire.getPos().getX();
        this.y = bonfire.getPos().getY();
        this.z = bonfire.getPos().getZ();
        this.dim = bonfire.getDimension();
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(buffer.readUtf()));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeUtf(dim.location().toString());
    }

    @Override
    public void handle(PlayPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player().get();
        BlockPos pos = new BlockPos(x, y, z);
        BonfireTeleporter.travelToBonfire(player, pos, dim);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
