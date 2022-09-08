package wehavecookies56.bonfires.datagen;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.NBTIngredient;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.crafting.EstusFlaskIngredientFactory;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    DataGenerator generator;

    public Recipes(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> recipeConsumer) {

        ShapelessRecipeBuilder.shapeless(BlockSetup.ash_block.get())
                .requires(ItemSetup.ash_pile.get(), 9)
                .group(Bonfires.modid)
                .unlockedBy("has_ash_pile", InventoryChangeTrigger.Instance.hasItems(ItemSetup.ash_pile.get()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(BlockSetup.ash_bone_pile.get())
                .pattern("BBB")
                .pattern("AAA")
                .define('A', ItemSetup.ash_pile.get())
                .define('B', ItemSetup.homeward_bone.get())
                .group(Bonfires.modid)
                .unlockedBy("has_ash_pile", InventoryChangeTrigger.Instance.hasItems(ItemSetup.ash_pile.get()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(ItemSetup.ash_pile.get(), 9)
                .requires(BlockSetup.ash_block.get())
                .group(Bonfires.modid)
                .unlockedBy("has_ash_block", InventoryChangeTrigger.Instance.hasItems(BlockSetup.ash_block.get()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(ItemSetup.coiled_sword.get())
                .pattern("OLO")
                .pattern("FSF")
                .pattern("OAO")
                .define('O', Tags.Items.OBSIDIAN)
                .define('L', Items.LAVA_BUCKET)
                .define('F', Items.FIRE_CHARGE)
                .define('S', Items.DIAMOND_SWORD)
                .define('A', ItemSetup.ash_pile.get())
                .group(Bonfires.modid)
                .unlockedBy("has_ash_bone_pile", InventoryChangeTrigger.Instance.hasItems(BlockSetup.ash_bone_pile.get()))
                .save(recipeConsumer);

        SmithingRecipeBuilder.smithing(Ingredient.of(Items.IRON_SWORD), Ingredient.of(ItemSetup.coiled_sword_fragment.get()), ItemSetup.coiled_sword.get())
                .unlocks("has_coiled_sword_fragment", InventoryChangeTrigger.Instance.hasItems(ItemSetup.coiled_sword_fragment.get()))
                .save(recipeConsumer, new ResourceLocation(Bonfires.modid, "coiled_sword_smithing"));

        ShapelessRecipeBuilder.shapeless(ItemSetup.estus_shard.get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.GOLDEN_APPLE)
                .requires(Tags.Items.NUGGETS_GOLD)
                .group(Bonfires.modid)
                .unlockedBy("has_diamond", InventoryChangeTrigger.Instance.hasItems(ItemPredicate.Builder.item().of(Tags.Items.GEMS_DIAMOND).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(ItemSetup.homeward_bone.get())
                .requires(Tags.Items.RODS_BLAZE)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Tags.Items.BONES)
                .group(Bonfires.modid)
                .unlockedBy("has_ender_pearl", InventoryChangeTrigger.Instance.hasItems(ItemPredicate.Builder.item().of(Tags.Items.ENDER_PEARLS).build()))
                .save(recipeConsumer);

        CookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.OBSIDIAN), ItemSetup.titanite_shard.get(), 0.25F, 1000)
                .unlockedBy("has_obsidian", InventoryChangeTrigger.Instance.hasItems(ItemPredicate.Builder.item().of(Tags.Items.OBSIDIAN).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(ItemSetup.large_titanite_shard.get())
                .requires(ItemSetup.titanite_shard.get(), 5)
                .group(Bonfires.modid)
                .unlockedBy("has_titanite_shard", InventoryChangeTrigger.Instance.hasItems(ItemSetup.titanite_shard.get()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(ItemSetup.titanite_chunk.get())
                .requires(ItemSetup.large_titanite_shard.get(), 3)
                .requires(Items.NETHERITE_SCRAP)
                .group(Bonfires.modid)
                .unlockedBy("has_large_titanite_shard", InventoryChangeTrigger.Instance.hasItems(ItemSetup.large_titanite_shard.get()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(ItemSetup.titanite_slab.get())
                .pattern("CCC")
                .pattern("CEC")
                .pattern("CCC")
                .define('C', ItemSetup.titanite_chunk.get())
                .define('E', Items.END_CRYSTAL)
                .group(Bonfires.modid)
                .unlockedBy("has_titanite_chunk", InventoryChangeTrigger.Instance.hasItems(ItemSetup.titanite_chunk.get()))
                .save(recipeConsumer);
    }
}