package wehavecookies56.bonfires.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresGroup;
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
        ItemSetup.ITEMS.register(name, () -> new BlockItem(newBlock.get(), new Item.Properties().tab(BonfiresGroup.INSTANCE)));
        return newBlock;
    }
}
