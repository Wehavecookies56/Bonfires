package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.item.Item;
import uk.co.wehavecookies56.bonfires.Bonfires;

public class ItemTitaniteSlab extends Item {

    public ItemTitaniteSlab(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Bonfires.tabBonfires);
    }
}
