package wehavecookies56.bonfires.items;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

public class LargeTitaniteShardItem extends Item {

    public LargeTitaniteShardItem(String name) {
        super(new Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Bonfires.modid, name))));
    }
}
