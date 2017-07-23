package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.EstusHandler;
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;

/**
 * Created by Toby on 16/11/2016.
 */
public class ItemHomewardBone extends Item {

    public ItemHomewardBone(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Bonfires.tabBonfires);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            BonfireTeleporter tp = new BonfireTeleporter(playerIn.getServer().getWorld(worldIn.provider.getDimension()));
            tp.teleport(playerIn, worldIn, playerIn.getBedLocation(), worldIn.provider.getDimension());
            playerIn.getHeldItemMainhand().shrink(1);
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
