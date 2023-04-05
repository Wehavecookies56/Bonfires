package wehavecookies56.bonfires;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BonfiresGroup {

    @SubscribeEvent
    public static void buildCreativeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(Bonfires.modid, Bonfires.modid), builder -> {
            builder
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
                    });
        });
    }

}
