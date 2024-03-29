package wehavecookies56.bonfires.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class OpenCreateScreen extends Packet<OpenCreateScreen> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "open_create_screen");

    public OpenCreateScreen(FriendlyByteBuf buffer) {
        decode(buffer);
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
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(tePos);
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            BonfireTileEntity te = (BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(tePos);
            ClientPacketHandler.openCreateScreen(te);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
