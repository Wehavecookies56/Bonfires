package wehavecookies56.bonfires.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.List;
import java.util.function.Consumer;

public class Recipes extends FabricRecipeProvider {

    public Recipes(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> recipeConsumer) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, BlockSetup.ash_block)
                .input(ItemSetup.ash_pile, 9)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.ash_pile), FabricRecipeProvider.conditionsFromItem(ItemSetup.ash_pile))
                .offerTo(recipeConsumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, BlockSetup.ash_bone_pile)
                .pattern("BBB")
                .pattern("AAA")
                .input('A', ItemSetup.ash_pile)
                .input('B', ItemSetup.homeward_bone)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.ash_pile), FabricRecipeProvider.conditionsFromItem(ItemSetup.ash_pile))
                .offerTo(recipeConsumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemSetup.ash_pile, 9)
                .input(BlockSetup.ash_block)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(BlockSetup.ash_block), FabricRecipeProvider.conditionsFromItem(BlockSetup.ash_block))
                .offerTo(recipeConsumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemSetup.coiled_sword)
                .pattern("OLO")
                .pattern("FSF")
                .pattern("OAO")
                .input('O', Blocks.OBSIDIAN)
                .input('L', Items.LAVA_BUCKET)
                .input('F', Items.FIRE_CHARGE)
                .input('S', Items.DIAMOND_SWORD)
                .input('A', ItemSetup.ash_pile)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(BlockSetup.ash_bone_pile), FabricRecipeProvider.conditionsFromItem(BlockSetup.ash_bone_pile))
                .offerTo(recipeConsumer);

        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.FIRE_CHARGE), Ingredient.ofItems(Items.IRON_SWORD), Ingredient.ofItems(ItemSetup.coiled_sword_fragment), RecipeCategory.COMBAT, ItemSetup.coiled_sword)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.coiled_sword_fragment), FabricRecipeProvider.conditionsFromItem(ItemSetup.coiled_sword_fragment))
                .offerTo(recipeConsumer, new Identifier(Bonfires.modid, "coiled_sword_smithing"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BREWING, ItemSetup.estus_flask)
                .input(Items.GLASS_BOTTLE)
                .input(ItemSetup.estus_shard, 3)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.estus_shard), FabricRecipeProvider.conditionsFromItem(ItemSetup.estus_shard))
                .offerTo(recipeConsumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BREWING, ItemSetup.estus_shard)
                .input(ConventionalItemTags.DIAMONDS)
                .input(Items.BLAZE_POWDER)
                .input(Items.GOLDEN_APPLE)
                .input(Items.GOLD_NUGGET)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(Items.DIAMOND), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.DIAMONDS))
                .offerTo(recipeConsumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemSetup.homeward_bone)
                .input(Items.BLAZE_ROD)
                .input(Items.ENDER_PEARL)
                .input(Items.BONE)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(Items.ENDER_PEARL), FabricRecipeProvider.conditionsFromItem(Items.ENDER_PEARL))
                .offerTo(recipeConsumer);

        RecipeProvider.offerSmelting(recipeConsumer, List.of(Blocks.OBSIDIAN), RecipeCategory.MISC, ItemSetup.titanite_shard, 0.25F, 1000, Bonfires.modid);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemSetup.large_titanite_shard)
                .input(ItemSetup.titanite_shard, 5)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.titanite_shard), FabricRecipeProvider.conditionsFromItem(ItemSetup.titanite_shard))
                .offerTo(recipeConsumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemSetup.titanite_chunk)
                .input(ItemSetup.large_titanite_shard, 3)
                .input(Items.NETHERITE_SCRAP)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.large_titanite_shard), FabricRecipeProvider.conditionsFromItem(ItemSetup.large_titanite_shard))
                .offerTo(recipeConsumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemSetup.titanite_slab)
                .pattern("CCC")
                .pattern("CEC")
                .pattern("CCC")
                .input('C', ItemSetup.titanite_chunk)
                .input('E', Items.END_CRYSTAL)
                .group(Bonfires.modid)
                .criterion(FabricRecipeProvider.hasItem(ItemSetup.titanite_chunk), FabricRecipeProvider.conditionsFromItem(ItemSetup.titanite_chunk))
                .offerTo(recipeConsumer);
    }
}
