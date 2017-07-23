package uk.co.wehavecookies56.bonfires.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class EstusFlaskIngredientFactory implements IIngredientFactory {

    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {

        final ItemStack stack = CraftingHelper.getItemStack(json, context);

        int estus = JsonUtils.getInt(json, "estus", 0);
        int uses = JsonUtils.getInt(json, "uses", 0);

        stack.setTagCompound(new NBTTagCompound());
        if (stack.getTagCompound() != null) {
            stack.getTagCompound().setInteger("estus", estus);
            stack.getTagCompound().setInteger("uses", uses);
        }

        return new IngredientNBT(stack);
    }
}
