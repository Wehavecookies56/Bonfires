package wehavecookies56.bonfires.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
    }

}
