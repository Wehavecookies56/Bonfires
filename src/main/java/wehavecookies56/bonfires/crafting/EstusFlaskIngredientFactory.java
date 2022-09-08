package wehavecookies56.bonfires.crafting;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.NBTIngredient;


import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class EstusFlaskIngredientFactory implements IIngredientSerializer<IngredientNBT> {

    @Override
    public IngredientNBT parse(PacketBuffer buffer) {
        return new IngredientNBT(buffer.readItem());
    }

    @Override
    public IngredientNBT parse(JsonObject json) {
        final ItemStack stack = CraftingHelper.getItemStack(json, true);

        int estus = JsonUtils.getIntOr("estus", json, 0);
        int uses = JsonUtils.getIntOr("uses", json, 0);

        stack.setTag(new CompoundNBT());
        if (stack.getTag() != null) {
            stack.getTag().putInt("estus", estus);
            stack.getTag().putInt("uses", uses);
        }

        return new IngredientNBT(stack);
    }

    @Override
    public void write(PacketBuffer buffer, IngredientNBT ingredient) {
        buffer.writeItem(ingredient.getItems()[0]);
    }
}
