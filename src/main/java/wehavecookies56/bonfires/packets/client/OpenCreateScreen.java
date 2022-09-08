package wehavecookies56.bonfires.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class OpenCreateScreen extends Packet<OpenCreateScreen> {

    public OpenCreateScreen(PacketBuffer buffer) {
        super(buffer);
    }

    BlockPos tePos;

    public OpenCreateScreen(BonfireTileEntity te) {
        this.tePos = te.getBlockPos();
    }

    @Override
    public void decode(PacketBuffer buffer) {
        tePos = buffer.readBlockPos();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(tePos);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        BonfireTileEntity te = (BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(tePos);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.openCreateScreen(te));
    }
}
