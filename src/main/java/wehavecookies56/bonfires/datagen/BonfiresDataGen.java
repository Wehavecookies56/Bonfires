package wehavecookies56.bonfires.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.concurrent.CompletableFuture;

public class BonfiresDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(BonfiresBlockLoot::new);
        pack.addProvider(Recipes::new);
    }

    public static class BonfiresBlockLoot extends FabricBlockLootTableProvider {

        public BonfiresBlockLoot(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            dropsWithSilkTouch(BlockSetup.ash_block);
            addDrop(BlockSetup.ash_block, new LootTable.Builder().pool(
                    new LootPool.Builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(ItemSetup.ash_pile)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 6))))
            ));
            addDrop(BlockSetup.ash_bone_pile, new LootTable.Builder().pool(
                    new LootPool.Builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(BlockSetup.ash_bone_pile))
            ));
        }
    }
}
