package wehavecookies56.bonfires.setup;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.items.*;

import java.util.function.Supplier;

public class ItemSetup {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bonfires.modid);

    public static final Supplier<Item>
        ash_pile = create("ash_pile", AshPileItem::new),
        coiled_sword = create("coiled_sword", CoiledSwordItem::new),
        estus_flask = create("estus_flask", EstusFlaskItem::new),
        homeward_bone = create("homeward_bone", () -> new HomewardBoneItem(new Item.Properties())),
        coiled_sword_fragment = create("coiled_sword_fragment", CoiledSwordFragmentItem::new),
        estus_shard = create("estus_shard", EstusShardItem::new),
        titanite_shard = create("titanite_shard", TitaniteShardItem::new),
        large_titanite_shard = create("large_titanite_shard", LargeTitaniteShardItem::new),
        titanite_chunk = create("titanite_chunk", TitaniteChunkItem::new),
        titanite_slab = create("titanite_slab", TitaniteSlabItem::new),
        undead_bone_shard = create("undead_bone_shard", UndeadBoneShardItem::new);


    public static <T extends Item> DeferredItem<T> create(String name, Supplier<? extends T> item) {
        return ITEMS.register(name, item);
    }

}
