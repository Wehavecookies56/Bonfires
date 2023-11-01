package wehavecookies56.bonfires.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class OpenCreateScreen extends Packet<OpenCreateScreen> {

    public OpenCreateScreen(FriendlyByteBuf buffer) {
        super(buffer);
    }

    BlockPos tePos;

    public OpenCreateScreen(BonfireTileEntity te) {
        this.tePos = te.getBlockPos();
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        tePos = buffer.readBlockPos();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(tePos);
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        BonfireTileEntity te = (BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(tePos);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.openCreateScreen(te));
    }
}
