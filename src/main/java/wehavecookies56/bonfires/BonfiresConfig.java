package wehavecookies56.bonfires;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Toby on 17/12/2016.
 */
@Mod.EventBusSubscriber(modid = Bonfires.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BonfiresConfig {

    public static class Client {
        public static boolean renderTextAboveBonfire = true;
        public final ForgeConfigSpec.ConfigValue<Boolean> renderTextAboveBonfireConfig;

        public static List<String> tabIcons = Arrays.asList("minecraft:overworld=minecraft:grass_block", "minecraft:the_nether=minecraft:netherrack", "minecraft:the_end=minecraft:end_stone", "kingdomkeys:dive_to_the_heart=kingdomkeys:mosaic_stained_glass");
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> tabIconsConfig;

        public Client(ForgeConfigSpec.Builder builder) {
            this.renderTextAboveBonfireConfig = builder.comment("Whether to Render the name of the Bonfire above the Bonfire, default:true").define("Render Text Above Bonfire", renderTextAboveBonfire);
            this.tabIconsConfig = builder.comment("Set the icons to display for the dimension tabs in the Bonfire GUI, mod:dimensionname=mod:itemname").defineList("Bonfire Dimension Tab Icons", tabIcons, input -> validateIcon((String) input));
        }

        public boolean validateIcon(String input) {
            if (input.contains("=")) {
                String[] split = input.split("=");
                if (split.length == 2) {
                    return split[0].contains(":") && split[1].contains(":");
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static class Common {
        public static boolean enableUBSBonfire = true;
        public final ForgeConfigSpec.BooleanValue enableUBSBonfireConfig;

        public static boolean enableReinforcing = true;
        public final ForgeConfigSpec.BooleanValue enableReinforcingConfig;

        public static List<String> reinforceBlacklist = new ArrayList<>();
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> reinforceBlacklistConfig;

        public Common(ForgeConfigSpec.Builder builder) {
            this.enableUBSBonfireConfig = builder.comment("Enable undead bone shard drops from blowing up a bonfire, default:true").define("Enable Undead Bone Shard drops", enableUBSBonfire);
            this.enableReinforcingConfig = builder.comment("Enable weapon/tool reinforcing, default:true").define("Enable reinforcing", enableReinforcing);
            this.reinforceBlacklistConfig = builder.worldRestart().comment("Disable specific items from being able to reinforce them").defineList("Reinforce item blacklist", reinforceBlacklist, input -> validateBlacklist((String) input));
        }

        public boolean validateBlacklist(String input) {
            return input.contains(":");
        }
    }


    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    @SubscribeEvent
    public static void configEvent(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            Client.renderTextAboveBonfire = CLIENT.renderTextAboveBonfireConfig.get();
            Client.tabIcons = (List<String>) CLIENT.tabIconsConfig.get();
        } else if (event.getConfig().getSpec() == COMMON_SPEC) {
            Common.enableReinforcing = COMMON.enableReinforcingConfig.get();
            Common.enableUBSBonfire = COMMON.enableUBSBonfireConfig.get();
            Common.reinforceBlacklist = (List<String>) COMMON.reinforceBlacklistConfig.get();
        }
    }
}
