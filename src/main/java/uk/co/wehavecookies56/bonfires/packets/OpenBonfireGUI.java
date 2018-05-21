package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.ReinforceHandler;
import uk.co.wehavecookies56.bonfires.gui.GuiBonfire;

import java.io.IOException;

public class OpenBonfireGUI extends AbstractMessage.AbstractClientMessage<OpenBonfireGUI> {

    String ownerName;

    public OpenBonfireGUI() {}

    public OpenBonfireGUI(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        ownerName = buffer.readString(16);
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeString(ownerName);
    }

    @Override
    protected void process(EntityPlayer player, Side side) {
        GuiBonfire.ownerName = ownerName;
    }
}
