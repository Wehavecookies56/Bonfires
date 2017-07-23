package uk.co.wehavecookies56.bonfires;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Created by Toby on 17/12/2016.
 */
@Config(modid = Bonfires.modid, name = "bonfires")
public class BonfiresConfig {

    @Config.Name("Render Text Above Bonfire")
    public static boolean renderTextAboveBonfire = true;

    @Mod.EventBusSubscriber(modid = Bonfires.modid)
    private static class Events {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent event) {
            if (event.getModID().equals(Bonfires.modid)) {
                ConfigManager.sync(Bonfires.modid, Config.Type.INSTANCE);
            }
        }

    }

}
