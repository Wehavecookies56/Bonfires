package uk.co.wehavecookies56.bonfires.packets;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.ReinforceHandler;
import uk.co.wehavecookies56.bonfires.blocks.BlockAshBonePile;
import uk.co.wehavecookies56.bonfires.gui.GuiReinforce;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class ReinforceItem extends AbstractMessage.AbstractServerMessage<ReinforceItem> {

    private ItemStack stack;
    private int slot, level;

    @SuppressWarnings("unused")
    public ReinforceItem() {}

    public ReinforceItem(ItemStack stack, int slot, int level) {
        this.stack = stack;
        this.slot = slot;
        this.level = level;
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        this.stack = buffer.readItemStack();
        this.slot = buffer.readInt();
        this.level = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeItemStack(stack);
        buffer.writeInt(slot);
        buffer.writeInt(level);
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        ItemStack required = ReinforceHandler.getRequiredResources(stack);
        if (ReinforceHandler.hasHandler(stack)) {
            GuiReinforce.removeRequiredItems(player, required);
            ReinforceHandler.getHandler(stack).setLevel(level);

            PacketDispatcher.sendTo(new SyncReinforceData(ReinforceHandler.getHandler(stack), stack, slot), (EntityPlayerMP) player);
        }
    }
}
