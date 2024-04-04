package wehavecookies56.bonfires.datagen;
//TODO port datagen to fabric
/*
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BonfiresDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new Recipes(generator));
        generator.addProvider(true, new LootTableProvider(generator.getPackOutput(), Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(BonfiresBlockLoot::new, LootContextParamSets.BLOCK))));
    }

    public static class BonfiresBlockLoot extends BlockLootSubProvider {

        protected BonfiresBlockLoot() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            dropWhenSilkTouch(BlockSetup.ash_block.get());
            add(BlockSetup.ash_block.get(), new LootTable.Builder().withPool(
                    new LootPool.Builder()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ItemSetup.ash_pile.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
            ));
            add(BlockSetup.ash_bone_pile.get(), new LootTable.Builder().withPool(
                    new LootPool.Builder()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(BlockSetup.ash_bone_pile.get()))
            ));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BlockSetup.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}
*/