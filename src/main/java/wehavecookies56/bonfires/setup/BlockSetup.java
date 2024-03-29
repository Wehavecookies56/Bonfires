package wehavecookies56.bonfires.setup;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.AshBlockBlock;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;

import java.util.function.Supplier;

public class BlockSetup {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bonfires.modid);

    public static final Supplier<Block>
            ash_bone_pile = create("ash_bone_pile", AshBonePileBlock::new),
            ash_block = create("ash_block", AshBlockBlock::new)
    ;

    public static DeferredBlock<Block> create(String name, Supplier<? extends Block> block) {
        DeferredBlock<Block> newBlock = BLOCKS.register(name, block);
        ItemSetup.ITEMS.registerSimpleBlockItem(name, newBlock);
        return newBlock;
    }
}
