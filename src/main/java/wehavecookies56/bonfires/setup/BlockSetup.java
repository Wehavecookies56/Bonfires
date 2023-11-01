package wehavecookies56.bonfires.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.AshBlockBlock;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;

import java.util.function.Supplier;

public class BlockSetup {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bonfires.modid);

    public static final RegistryObject<Block>
            ash_bone_pile = create("ash_bone_pile", AshBonePileBlock::new),
            ash_block = create("ash_block", AshBlockBlock::new)
    ;

    public static RegistryObject<Block> create(String name, Supplier<? extends Block> block) {
        RegistryObject<Block> newBlock = BLOCKS.register(name, block);
        ItemSetup.ITEMS.register(name, () -> new BlockItem(newBlock.get(), new Item.Properties()));
        return newBlock;
    }
}
