package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

public class DisplayTitle extends Packet<DisplayTitle> {

    public DisplayTitle(FriendlyByteBuf buffer) {
        super(buffer);
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
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(title);
        buffer.writeUtf(subtitle);
        buffer.writeInt(fadein);
        buffer.writeInt(stay);
        buffer.writeInt(fadeout);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.displayTitle(this));
    }
}
