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

    @Config.Name("Bonfire dimension tab icons")
    public static String[] tabIcons = { "0=minecraft:grass", "-1=minecraft:netherrack", "1=minecraft:end_stone" };

    @Config.Name("Enable titanite shard drops from obsidian when adjacent to lava")
    public static boolean enableTitaniteObsidian = true;

    @Config.Name("Enable undead bone shard drops from blowing up a bonfire")
    public static boolean enableUBSBonfire = true;

    @Config.Name("Enable weapon/tool reinforcing")
    public static boolean enableReinforcing = true;

    @Config.RequiresMcRestart
    @Config.Name("Reinforce item blacklist")
    public static String[] reinforceBlackList = {};

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
