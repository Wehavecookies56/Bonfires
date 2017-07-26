package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.item.Item;
import uk.co.wehavecookies56.bonfires.Bonfires;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemAshPile extends Item {

    public ItemAshPile(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Bonfires.tabBonfires);
    }
}
