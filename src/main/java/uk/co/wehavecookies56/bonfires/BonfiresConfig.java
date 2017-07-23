package uk.co.wehavecookies56.bonfires;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

/**
 * Created by Toby on 17/12/2016.
 */
@Config(modid = Bonfires.modid, name = "bonfires")
public class BonfiresConfig {

    @Config.Name("Render Text Above Bonfire")
    public static boolean renderTextAboveBonfire = true;

    @Config.RequiresMcRestart
    @Config.Name("Bonfire dimension tab icons")
    public static Map<String, String> tabIcons;

    static {
        tabIcons = Maps.newHashMap();
        tabIcons.put("" + 0, "minecraft:grass");
        tabIcons.put("" + -1, "minecraft:netherrack");
        tabIcons.put("" + 1, "minecraft:end_stone");
    }

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
