package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.ReinforceHandler;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class SyncReinforceData extends AbstractMessage.AbstractClientMessage<SyncReinforceData> {

    private int level;
    private int maxLevel;
    private ItemStack stack;
    private int slot;

    @SuppressWarnings("unused")
    public SyncReinforceData() {}

    public SyncReinforceData(ReinforceHandler.IReinforceHandler handler, ItemStack stack, int slot) {
        level = handler.level();
        maxLevel = handler.maxLevel();
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.level = buffer.readInt();
        this.maxLevel = buffer.readInt();
        this.stack = buffer.readItemStack();
        this.slot = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(level);
        buffer.writeInt(maxLevel);
        buffer.writeItemStack(stack);
        buffer.writeInt(slot);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ReinforceHandler.IReinforceHandler handler = ReinforceHandler.getHandler(stack);
        handler.setMaxLevel(maxLevel);
        handler.setLevel(level);
        if (level != 0)
            stack.setStackDisplayName(I18n.format(stack.getUnlocalizedName()+".name") + " +" + level);
        player.inventory.setInventorySlotContents(slot, stack);
    }
}
