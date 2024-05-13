package wehavecookies56.bonfires.setup;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.items.EstusFlaskItem;

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
                        stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(i, i));
                        pOutput.add(stack);
                    }
                    ItemStack stack = new ItemStack(BlockSetup.ash_bone_pile);
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData(null, false));
                    stack.set(DataComponentTypes.CUSTOM_NAME, Text.translatable(LocalStrings.TOOLTIP_UNLIT));
                    pOutput.add(stack);
                    stack = stack.copy();
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData(null, true));
                    int[] random = new Random().ints(2,0, 9999).toArray();
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData("Bonfire" + random[0], true));
                    pOutput.add(stack);
                    stack = stack.copy();
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData("Bonfire" + random[1], false));
                    pOutput.add(stack);
                })
                .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(Bonfires.modid, Bonfires.modid), tab);
    }
}
