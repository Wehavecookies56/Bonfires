package wehavecookies56.bonfires.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.Packet;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncReinforceData extends Packet<SyncReinforceData> {

    public int level;
    public int maxLevel;
    public int slot;

    public SyncReinforceData(PacketBuffer buffer) {
       super(buffer);
    }

    public SyncReinforceData(ReinforceHandler.IReinforceHandler handler, int slot) {
        level = handler.level();
        maxLevel = handler.maxLevel();
        this.slot = slot;
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.level = buffer.readInt();
        this.maxLevel = buffer.readInt();
        this.slot = buffer.readInt();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(level);
        buffer.writeInt(maxLevel);
        buffer.writeInt(slot);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.syncReinforceData(this));
    }
}
