package wehavecookies56.bonfires.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;

/**
 * Created by Toby on 05/11/2016.
 */

public class CreativeTabSetup {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bonfires.modid);

    public static final RegistryObject<CreativeModeTab> tab = TABS.register(Bonfires.modid, () ->
        CreativeModeTab.builder()
                .title(Component.translatable(LocalStrings.ITEMGROUP_BONFIRES))
                .icon(() -> new ItemStack(ItemSetup.coiled_sword.get()))
                .displayItems((pParams, pOutput) -> {
                    ItemSetup.ITEMS.getEntries().stream().map(RegistryObject::get).map(ItemStack::new).toList().forEach(pOutput::accept);
                    for (int i = 3; i < 16; ++i) {
                        ItemStack stack = new ItemStack(ItemSetup.estus_flask.get());
                        stack.setTag(new CompoundTag());
                        if (stack.getTag() != null) {
                            stack.getTag().putInt("uses", i);
                            stack.getTag().putInt("estus", i);
                        }
                        pOutput.accept(stack);
                    }
                }).build()
    );

}
