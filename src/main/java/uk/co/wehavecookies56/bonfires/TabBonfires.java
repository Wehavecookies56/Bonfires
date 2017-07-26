package uk.co.wehavecookies56.bonfires;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by Toby on 05/11/2016.
 */
class TabBonfires extends CreativeTabs {

    public TabBonfires(String label) {
        super(getNextID(), label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Bonfires.estusFlask);
    }
}
