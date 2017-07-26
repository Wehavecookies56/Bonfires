package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.Bonfires;
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;

import javax.annotation.Nonnull;

/**
 * Created by Toby on 16/11/2016.
 */
public class ItemCoiledSwordFragment extends Item {

    public ItemCoiledSwordFragment(String name) {
        this.setMaxStackSize(1);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Bonfires.tabBonfires);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
        if (!worldIn.isRemote) {
            if (playerIn.getServer() != null) {
                BonfireTeleporter tp = new BonfireTeleporter(playerIn.getServer().getWorld(worldIn.provider.getDimension()));
                tp.teleport(playerIn, worldIn, playerIn.getBedLocation(), worldIn.provider.getDimension());
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
