package wehavecookies56.bonfires.packets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;

public class DisplayTitle implements FabricPacket {

    public static final PacketType<DisplayTitle> TYPE = PacketType.create(new Identifier(Bonfires.modid, "display_title"), DisplayTitle::new);

    public String title, subtitle;
    public int fadein, stay, fadeout;

    public DisplayTitle(PacketByteBuf buffer) {
        decode(buffer);
    }

    public DisplayTitle(String title, String subtitle, int fadein, int stay, int fadeout) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadein = fadein;
        this.stay = stay;
        this.fadeout = fadeout;
    }

    public void decode(PacketByteBuf buffer) {
        title = buffer.readString();
        subtitle = buffer.readString();
        fadein = buffer.readInt();
        stay = buffer.readInt();
        fadeout = buffer.readInt();
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeString(title);
        buffer.writeString(subtitle);
        buffer.writeInt(fadein);
        buffer.writeInt(stay);
        buffer.writeInt(fadeout);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketHandler.displayTitle(this);
        }
    }
}
