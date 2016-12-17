package uk.co.wehavecookies56.bonfires;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Created by Toby on 17/12/2016.
 */
public class BonfiresConfig {

    public static Configuration config;

    public static boolean renderTextAboveBonfire = true;

    public static void init(File file) {
        config = new Configuration(file);
        config.load();
        load();

        MinecraftForge.EVENT_BUS.register(new BonfiresConfig());
    }
    public static void load() {

        renderTextAboveBonfire = config.get(Configuration.CATEGORY_CLIENT, "Render Text Above Bonfires", renderTextAboveBonfire).getBoolean();

        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void OnConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Bonfires.modid)) load();
    }

}
