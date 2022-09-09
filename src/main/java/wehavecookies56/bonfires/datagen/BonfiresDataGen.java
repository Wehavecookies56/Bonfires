package wehavecookies56.bonfires.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;
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
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return Arrays.asList(
               Pair.of(BonfiresBlockLoot::new, LootContextParamSets.BLOCK)
            );
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {

        }
    }

    public static class BonfiresBlockLoot extends BlockLoot {

        @Override
        protected void addTables() {
            dropWhenSilkTouch(BlockSetup.ash_block.get());
            add(BlockSetup.ash_block.get(), new LootTable.Builder().withPool(
                    new LootPool.Builder()
                            .name("ash")
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ItemSetup.ash_pile.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
            ));
            add(BlockSetup.ash_bone_pile.get(), new LootTable.Builder().withPool(
                    new LootPool.Builder()
                            .name("self")
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
