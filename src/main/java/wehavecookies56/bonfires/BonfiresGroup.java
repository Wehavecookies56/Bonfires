package wehavecookies56.bonfires;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */
public class BonfiresGroup extends ItemGroup {

    public static BonfiresGroup INSTANCE = new BonfiresGroup(LocalStrings.ITEMGROUP_BONFIRES);

    public BonfiresGroup(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemSetup.coiled_sword.get());
    }
}
