package wehavecookies56.bonfires.setup;

import net.minecraft.advancements.CriteriaTriggers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.PacketHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
        EstusHandler.init();
        BonfireHandler.init();
        BonfireLitTrigger.TRIGGER_BONFIRE_LIT = CriteriaTriggers.register(BonfireLitTrigger.ID.toString(), new BonfireLitTrigger());
    }

}
