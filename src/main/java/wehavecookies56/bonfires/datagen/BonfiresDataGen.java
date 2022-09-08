package wehavecookies56.bonfires.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BonfiresDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new Recipes(generator));
        generator.addProvider(new Loot(generator));
    }

    public static class Loot extends LootTableProvider {

        public Loot(DataGenerator generator) {
            super(generator);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
            return Arrays.asList(
               Pair.of(BlockLoot::new, LootParameterSets.BLOCK)
            );
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> p_validate_1_, ValidationTracker p_validate_2_) {

        }
    }

    public static class BlockLoot extends BlockLootTables {

        @Override
        protected void addTables() {
            dropWhenSilkTouch(BlockSetup.ash_block.get());
            add(BlockSetup.ash_block.get(), new LootTable.Builder().withPool(
                    new LootPool.Builder()
                            .name("ash")
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(ItemSetup.ash_pile.get())
                                    .apply(SetCount.setCount(RandomValueRange.between(3, 6))))
            ));
            add(BlockSetup.ash_bone_pile.get(), new LootTable.Builder().withPool(
                    new LootPool.Builder()
                            .name("self")
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(BlockSetup.ash_bone_pile.get()))
            ));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BlockSetup.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}
