package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemAshPile extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        //if (!worldIn.isRemote)
            //worldIn.getMinecraftServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP) playerIn, 0, new Teleporter(worldIn.getMinecraftServer().worldServerForDimension(1)));
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
