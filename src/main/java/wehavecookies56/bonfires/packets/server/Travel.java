package wehavecookies56.bonfires.packets.server;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.world.BonfireTeleporter;

public class Travel implements FabricPacket {

    public static final PacketType<Travel> TYPE = PacketType.create(new Identifier(Bonfires.modid, "travel"), Travel::new);

    private int x;
    private int y;
    private int z;
    private RegistryKey<World> dim;

    public Travel(PacketByteBuf buffer) {
        decode(buffer);
    }

    public Travel(Bonfire bonfire) {
        this.x = bonfire.getPos().getX();
        this.y = bonfire.getPos().getY();
        this.z = bonfire.getPos().getZ();
        this.dim = bonfire.getDimension();
    }

    public void decode(PacketByteBuf buffer) {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.dim = RegistryKey.of(RegistryKeys.WORLD, buffer.readIdentifier());
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeIdentifier(dim.getValue());
    }

    public void handle(ServerPlayerEntity player) {
        BlockPos pos = new BlockPos(x, y, z);
        player.incrementStat(Bonfires.TIMES_TRAVELLED);
        BonfireTeleporter.travelToBonfire(player, pos, dim);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
