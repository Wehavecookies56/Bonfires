package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;

public class DisplayBonfireTitle implements FabricPacket {

    public static PacketType<DisplayBonfireTitle> TYPE = PacketType.create(new Identifier(Bonfires.modid, "display_bonfire_title"), DisplayBonfireTitle::new);

    public DisplayBonfireTitle(PacketByteBuf buffer) {
        decode(buffer);
    }

    Bonfire bonfire;

    public DisplayBonfireTitle(Bonfire bonfire) {
        this.bonfire = bonfire;
    }

    public void decode(PacketByteBuf buffer) {
        bonfire = new Bonfire(buffer.readNbt());
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeNbt(bonfire.serializeNBT());
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.displayBonfireTravelled(bonfire);
        }
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
