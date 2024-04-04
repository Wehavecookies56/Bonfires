package wehavecookies56.bonfires.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.AshBlockBlock;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;

public class BlockSetup {
    public static final Block
            ash_bone_pile = new AshBonePileBlock(),
            ash_block = new AshBlockBlock()
    ;

    public static Block create(String name, Block block) {
        Block newBlock = Registry.register(Registries.BLOCK, new Identifier(Bonfires.modid, name), block);
        Registry.register(Registries.ITEM, new Identifier(Bonfires.modid, name), new BlockItem(newBlock, new Item.Settings()));
        return newBlock;
    }

    public static void init() {
        create("ash_bone_pile", ash_bone_pile);
        create("ash_block", ash_block);
    }
}
