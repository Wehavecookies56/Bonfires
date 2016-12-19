package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.Bonfires;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemEstusShard extends Item {

    public ItemEstusShard() {}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++) {
                if (playerIn.getHeldItemMainhand() != ItemStack.EMPTY) {
                    if (playerIn.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                        if (playerIn.inventory.getStackInSlot(i).getItem() == Bonfires.estusFlask) {
                            if (playerIn.inventory.getStackInSlot(i).hasTagCompound()) {
                                if (playerIn.inventory.getStackInSlot(i).getTagCompound().getInteger("uses")+playerIn.getHeldItemMainhand().getCount() <= 15) {
                                    playerIn.inventory.getStackInSlot(i).getTagCompound().setInteger("uses", playerIn.inventory.getStackInSlot(i).getTagCompound().getInteger("uses") + playerIn.getHeldItemMainhand().getCount());
                                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                                } else if (playerIn.inventory.getStackInSlot(i).getTagCompound().getInteger("uses") < 15) {
                                    int remaining = playerIn.getHeldItemMainhand().getCount() - (15 - playerIn.inventory.getStackInSlot(i).getTagCompound().getInteger("uses"));
                                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(this, remaining));
                                    playerIn.inventory.getStackInSlot(i).getTagCompound().setInteger("uses", 15);
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
