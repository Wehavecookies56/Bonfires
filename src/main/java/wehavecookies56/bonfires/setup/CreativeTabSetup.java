package wehavecookies56.bonfires.setup;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.items.EstusFlaskItem;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by Toby on 05/11/2016.
 */

public class CreativeTabSetup {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bonfires.modid);

    public static final Supplier<CreativeModeTab> tab = TABS.register(Bonfires.modid, () ->
        CreativeModeTab.builder()
                .title(Component.translatable(LocalStrings.ITEMGROUP_BONFIRES))
                .icon(() -> new ItemStack(ItemSetup.coiled_sword.get()))
                .displayItems((pParams, pOutput) -> {
                    ItemSetup.ITEMS.getEntries().stream().filter(i -> i != ItemSetup.estus_flask).map(Supplier::get).map(ItemStack::new).toList().forEach(pOutput::accept);
                    for (int i = 3; i < 16; ++i) {
                        ItemStack stack = new ItemStack(ItemSetup.estus_flask.get());
                        stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(i, i));
                        pOutput.accept(stack);
                    }
                    ItemStack stack = new ItemStack(BlockSetup.ash_bone_pile.get());

                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData(null, false));
                    stack.set(DataComponents.CUSTOM_NAME, Component.translatable(LocalStrings.TOOLTIP_UNLIT));
                    pOutput.accept(stack);
                    stack = stack.copy();
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData(null, true));
                    int[] random = new Random().ints(2,0, 9999).toArray();
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData("Bonfire" + random[0], true));
                    pOutput.accept(stack);
                    stack = stack.copy();
                    stack.set(ComponentSetup.BONFIRE_DATA, new AshBonePileBlock.BonfireData("Bonfire" + random[1], false));
                    pOutput.accept(stack);
                }).build()
    );

}
