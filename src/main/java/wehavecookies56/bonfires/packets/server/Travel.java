package wehavecookies56.bonfires.packets.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.world.BonfireTeleporter;

/**
 * Created by Toby on 06/11/2016.
 */
public class Travel extends Packet<Travel> {

    private int x;
    private int y;
    private int z;
    private RegistryKey<World> dim;

    public Travel(PacketBuffer buffer) {
        super(buffer);
    }

    public Travel(Bonfire bonfire) {
        this.x = bonfire.getPos().getX();
        this.y = bonfire.getPos().getY();
        this.z = bonfire.getPos().getZ();
        this.dim = bonfire.getDimension();
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.dim = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(buffer.readUtf()));
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeUtf(dim.location().toString());
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        BlockPos pos = new BlockPos(x, y, z);
        BonfireTeleporter.travelToBonfire(player, pos, dim);
    }
}
