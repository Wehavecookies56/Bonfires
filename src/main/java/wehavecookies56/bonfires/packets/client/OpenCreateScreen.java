package wehavecookies56.bonfires.packets.client;


import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class OpenCreateScreen implements FabricPacket {

    public static final PacketType<OpenCreateScreen> TYPE = PacketType.create(new Identifier(Bonfires.modid, "open_create_screen"), OpenCreateScreen::new);

    BlockPos tePos;

    public OpenCreateScreen(BonfireTileEntity te) {
        this.tePos = te.getPos();
    }

    public OpenCreateScreen(PacketByteBuf buf) {
        decode(buf);
    }

    public void decode(PacketByteBuf buf) {
        tePos = buf.readBlockPos();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(tePos);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.openCreateScreen((BonfireTileEntity) MinecraftClient.getInstance().world.getBlockEntity(tePos));
        }
    }
}

/*
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
    public void handle(NetworkEvent.Context context) {
        BonfireTileEntity te = (BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(tePos);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.openCreateScreen(te));
    }
}

 */
