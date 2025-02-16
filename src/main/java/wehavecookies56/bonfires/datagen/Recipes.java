package wehavecookies56.bonfires.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.concurrent.CompletableFuture;

public class Recipes extends RecipeProvider {

    public Recipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shapeless(RecipeCategory.BUILDING_BLOCKS, BlockSetup.ash_block.get())
                .requires(ItemSetup.ash_pile.get(), 9)
                .group(Bonfires.modid)
                .unlockedBy("has_ash_pile", has(ItemSetup.ash_pile.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, BlockSetup.ash_bone_pile.get())
                .pattern("BBB")
                .pattern("AAA")
                .define('A', ItemSetup.ash_pile.get())
                .define('B', ItemSetup.homeward_bone.get())
                .group(Bonfires.modid)
                .unlockedBy("has_ash_pile", has(ItemSetup.ash_pile.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, ItemSetup.ash_pile.get(), 9)
                .requires(BlockSetup.ash_block.get())
                .group(Bonfires.modid)
                .unlockedBy("has_ash_block", has(BlockSetup.ash_block.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, ItemSetup.coiled_sword.get())
                .pattern("OLO")
                .pattern("FSF")
                .pattern("OAO")
                .define('O', Tags.Items.OBSIDIANS)
                .define('L', Items.LAVA_BUCKET)
                .define('F', Items.FIRE_CHARGE)
                .define('S', Items.DIAMOND_SWORD)
                .define('A', ItemSetup.ash_pile.get())
                .group(Bonfires.modid)
                .unlockedBy("has_ash_bone_pile", has(BlockSetup.ash_bone_pile.get()))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.FIRE_CHARGE), Ingredient.of(Items.IRON_SWORD), Ingredient.of(ItemSetup.coiled_sword_fragment.get()), RecipeCategory.COMBAT, ItemSetup.coiled_sword.get())
                .unlocks("has_coiled_sword_fragment", has(ItemSetup.coiled_sword_fragment.get()))
                .save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "coiled_sword_smithing")));

        shapeless(RecipeCategory.BREWING, ItemSetup.estus_shard.get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.GOLDEN_APPLE)
                .requires(Tags.Items.NUGGETS_GOLD)
                .group(Bonfires.modid)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        shapeless(RecipeCategory.TOOLS, ItemSetup.homeward_bone.get())
                .requires(Tags.Items.RODS_BLAZE)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Tags.Items.BONES)
                .group(Bonfires.modid)
                .unlockedBy("has_ender_pearl", has(Tags.Items.ENDER_PEARLS))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(tag(Tags.Items.OBSIDIANS), RecipeCategory.MISC, ItemSetup.titanite_shard.get(), 0.25F, 1000)
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
                .save(output);

        shapeless(RecipeCategory.MISC, ItemSetup.large_titanite_shard.get())
                .requires(ItemSetup.titanite_shard.get(), 5)
                .group(Bonfires.modid)
                .unlockedBy("has_titanite_shard", has(ItemSetup.titanite_shard.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, ItemSetup.titanite_chunk.get())
                .requires(ItemSetup.large_titanite_shard.get(), 3)
                .requires(Items.NETHERITE_SCRAP)
                .group(Bonfires.modid)
                .unlockedBy("has_large_titanite_shard", has(ItemSetup.large_titanite_shard.get()))
                .save(output);

        shaped(RecipeCategory.MISC, ItemSetup.titanite_slab.get())
                .pattern("CCC")
                .pattern("CEC")
                .pattern("CCC")
                .define('C', ItemSetup.titanite_chunk.get())
                .define('E', Items.END_CRYSTAL)
                .group(Bonfires.modid)
                .unlockedBy("has_titanite_chunk", has(ItemSetup.titanite_chunk.get()))
                .save(output);

        shapeless(RecipeCategory.TOOLS, ItemSetup.estus_flask.get())
                .requires(ItemSetup.estus_shard.get(), 3)
                .requires(Items.GLASS_BOTTLE)
                .group(Bonfires.modid)
                .unlockedBy("has_estus_shard", has(ItemSetup.estus_shard.get()))
                .save(output);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new Recipes(registries, output);
        }

        @Override
        public String getName() {
            return "Bonfires Recipes";
        }
    }
}