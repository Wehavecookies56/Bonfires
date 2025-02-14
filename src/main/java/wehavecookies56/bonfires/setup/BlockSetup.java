package wehavecookies56.bonfires.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.AshBlockBlock;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;

public class BlockSetup {
    public static final Block
            ash_bone_pile = new AshBonePileBlock("ash_bone_pile"),
            ash_block = new AshBlockBlock("ash_block")
    ;

    public static Block create(String name, Block block) {
        Identifier id = Identifier.of(Bonfires.modid, name);
        Block newBlock = Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(newBlock, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
        return newBlock;
    }

    public static void init() {
        create("ash_bone_pile", ash_bone_pile);
        create("ash_block", ash_block);
    }
}
