package wehavecookies56.bonfires.setup;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;

import java.util.Random;

/**
 * Created by Toby on 05/11/2016.
 */

public class CreativeTabSetup {

    public static final ItemGroup tab = FabricItemGroup.builder()
                .displayName(Text.translatable(LocalStrings.ITEMGROUP_BONFIRES))
                .icon(() -> new ItemStack(ItemSetup.coiled_sword))
                .entries((pParams, pOutput) -> {
                    Registries.ITEM.getEntrySet().stream().filter(registryKeyItemEntry -> registryKeyItemEntry.getKey().getValue().getNamespace().equals(Bonfires.modid)).filter(registryKeyItemEntry -> registryKeyItemEntry.getValue() != ItemSetup.estus_flask).forEach(registryKeyItemEntry -> pOutput.add(registryKeyItemEntry.getValue()));
                    for (int i = 3; i < 16; ++i) {
                        ItemStack stack = new ItemStack(ItemSetup.estus_flask);
                        stack.setNbt(new NbtCompound());
                        if (stack.getNbt() != null) {
                            stack.getNbt().putInt("uses", i);
                            stack.getNbt().putInt("estus", i);
                        }
                        pOutput.add(stack);
                    }
                    ItemStack stack = new ItemStack(BlockSetup.ash_bone_pile);
                    stack.setNbt(new NbtCompound());
                    if (stack.getNbt() != null) {
                        stack.getNbt().putBoolean("bonfire_private", false);
                    }
                    stack.setCustomName(Text.translatable(LocalStrings.TOOLTIP_UNLIT));
                    pOutput.add(stack);
                    stack = stack.copy();
                    stack.getNbt().putBoolean("bonfire_private", true);
                    int[] random = new Random().ints(2,0, 9999).toArray();
                    stack.getNbt().putString("bonfire_name", "Bonfire" + random[0]);
                    pOutput.add(stack);
                    stack = stack.copy();
                    stack.getNbt().putBoolean("bonfire_private", false);
                    stack.getNbt().putString("bonfire_name", "Bonfire" + random[1]);
                    pOutput.add(stack);
                })
                .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(Bonfires.modid, Bonfires.modid), tab);
    }
}
