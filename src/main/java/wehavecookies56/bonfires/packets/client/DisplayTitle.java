package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

public class DisplayTitle extends Packet<DisplayTitle> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "display_title");

    public DisplayTitle(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    public String title, subtitle;
    public int fadein, stay, fadeout;

    public DisplayTitle(String title, String subtitle, int fadein, int stay, int fadeout) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadein = fadein;
        this.stay = stay;
        this.fadeout = fadeout;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        title = buffer.readUtf();
        subtitle = buffer.readUtf();
        fadein = buffer.readInt();
        stay = buffer.readInt();
        fadeout = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(title);
        buffer.writeUtf(subtitle);
        buffer.writeInt(fadein);
        buffer.writeInt(stay);
        buffer.writeInt(fadeout);
    }


    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.displayTitle(this);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
