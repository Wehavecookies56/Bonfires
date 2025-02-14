package wehavecookies56.bonfires.setup;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.items.*;

public class ItemSetup {
    public static final Item
        ash_pile = new AshPileItem("ash_pile"),
        coiled_sword = new CoiledSwordItem("coiled_sword"),
        estus_flask = new EstusFlaskItem("estus_flask"),
        homeward_bone = new HomewardBoneItem("homeward_bone"),
        coiled_sword_fragment = new CoiledSwordFragmentItem("coiled_sword_fragment"),
        estus_shard = new EstusShardItem("estus_shard"),
        titanite_shard = new TitaniteShardItem("titanite_shard"),
        large_titanite_shard = new LargeTitaniteShardItem("large_titanite_shard"),
        titanite_chunk = new TitaniteChunkItem("titanite_chunk"),
        titanite_slab = new TitaniteSlabItem("titanite_slab"),
        undead_bone_shard = new UndeadBoneShardItem("undead_bone_shard");

    public static void init() {
        create("ash_pile", ash_pile);
        create("coiled_sword", coiled_sword);
        create("estus_flask", estus_flask);
        create("homeward_bone", homeward_bone);
        create("coiled_sword_fragment", coiled_sword_fragment);
        create("estus_shard", estus_shard);
        create("titanite_shard", titanite_shard);
        create("large_titanite_shard", large_titanite_shard);
        create("titanite_chunk", titanite_chunk);
        create("titanite_slab", titanite_slab);
        create("undead_bone_shard", undead_bone_shard);
    }

    public static Item create(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Bonfires.modid, name), item);
    }

}
