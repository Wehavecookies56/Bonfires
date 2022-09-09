package wehavecookies56.bonfires;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */
public class BonfiresGroup extends CreativeModeTab {

    public static BonfiresGroup INSTANCE = new BonfiresGroup(LocalStrings.ITEMGROUP_BONFIRES);

    public BonfiresGroup(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemSetup.coiled_sword.get());
    }
}
