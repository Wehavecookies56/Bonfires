package wehavecookies56.bonfires;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        public static boolean renderScreenshotsInGui = true;
        public final ForgeConfigSpec.ConfigValue<Boolean> renderScreenshotsInGuiConfig;

        public static boolean enableAutomaticScreenshotOnCreation = true;
        public final ForgeConfigSpec.ConfigValue<Boolean> enableAutomaticScreenshotOnCreationConfig;

        public static int screenshotWaitTicks = 5;
        public final ForgeConfigSpec.IntValue screenshotWaitTicksConfig;

        public static boolean deleteScreenshotsOnDestroyed = true;
        public final ForgeConfigSpec.ConfigValue<Boolean> deleteScreenshotsOnDestroyedConfig;

        public static List<String> hiddenDimensions = new ArrayList<>();
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> hiddenDimensionsConfig;

        public Client(ForgeConfigSpec.Builder builder) {
            this.renderTextAboveBonfireConfig = builder.comment("Whether to Render the name of the Bonfire above the Bonfire, default:true").define("Render Text Above Bonfire", renderTextAboveBonfire);
            this.tabIconsConfig = builder.comment("Set the icons to display for the dimension tabs in the Bonfire GUI, mod:dimensionname=mod:itemname").defineList("Bonfire Dimension Tab Icons", tabIcons, input -> validateIcon((String) input));
            this.renderScreenshotsInGuiConfig = builder.comment("Whether to render screenshots of the Bonfires in the Bonfire GUI, default:true").define("Render Screenshots in GUI", renderScreenshotsInGui);
            this.enableAutomaticScreenshotOnCreationConfig = builder.comment("Enables creating a screenshot of a Bonfire when it is created, default:true").define("Enable Automatic Screenshot on Creation", enableAutomaticScreenshotOnCreation);
            this.screenshotWaitTicksConfig = builder.comment("Set the number of ticks to wait to take a screenshot increase this if you are seeing the HUD and GUI in the screenshots, default:5").defineInRange("Screenshot Wait Ticks", screenshotWaitTicks, 1, 100);
            this.deleteScreenshotsOnDestroyedConfig = builder.comment("Whether to delete Bonfire screenshots when the Bonfire is destroyed, default:true").define("Delete Screenshots on Destroyed", deleteScreenshotsOnDestroyed);
            this.hiddenDimensionsConfig = builder.comment("List of dimensions to hide from the Bonfire GUI useful if you can't place a bonfire in the dimension, mod:dimensionname").defineList("Hidden Dimensions in GUI", hiddenDimensions, input -> ((String)input).contains(":"));
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

    public static class Server {
        public static double estusFlaskBaseHeal = 6;
        public final ForgeConfigSpec.DoubleValue estusFlaskBaseHealConfig;

        public static double estusFlaskHealPerLevel = 1;
        public final ForgeConfigSpec.DoubleValue estusFlaskHealPerLevelConfig;

        public static double reinforceDamagePerLevel = 0.5;
        public final ForgeConfigSpec.DoubleValue reinforceDamagePerLevelConfig;

        public Server(ForgeConfigSpec.Builder builder) {
            this.estusFlaskBaseHealConfig = builder.comment("Set how much the Estus Flask heals by default, 1 = half a heart, default:6").defineInRange("Estus Flask Base Heal", estusFlaskBaseHeal, 0, Double.MAX_VALUE);
            this.estusFlaskHealPerLevelConfig = builder.comment("Set the amount to increase Estus Flask healing per level, default:1").defineInRange("Estus Flask Heal Per Level", estusFlaskHealPerLevel, 0, Double.MAX_VALUE);
            this.reinforceDamagePerLevelConfig = builder.comment("Set the amount to increase damage for reinforced tools per level, default:0.5").defineInRange("Reinforce Damage Per Level", reinforceDamagePerLevel, 0, Double.MAX_VALUE);
        }

    }

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
        Pair<Server, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();
    }

    @SubscribeEvent
    public static void configEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            Client.renderTextAboveBonfire = CLIENT.renderTextAboveBonfireConfig.get();
            Client.tabIcons = (List<String>) CLIENT.tabIconsConfig.get();
            Client.renderScreenshotsInGui = CLIENT.renderScreenshotsInGuiConfig.get();
            Client.enableAutomaticScreenshotOnCreation = CLIENT.enableAutomaticScreenshotOnCreationConfig.get();
            Client.screenshotWaitTicks = CLIENT.screenshotWaitTicksConfig.get();
            Client.deleteScreenshotsOnDestroyed = CLIENT.deleteScreenshotsOnDestroyedConfig.get();
            Client.hiddenDimensions = (List<String>) CLIENT.hiddenDimensionsConfig.get();
        } else if (event.getConfig().getSpec() == COMMON_SPEC) {
            Common.enableReinforcing = COMMON.enableReinforcingConfig.get();
            Common.enableUBSBonfire = COMMON.enableUBSBonfireConfig.get();
            Common.reinforceBlacklist = (List<String>) COMMON.reinforceBlacklistConfig.get();
        } else if (event.getConfig().getSpec() == SERVER_SPEC) {
            Server.estusFlaskBaseHeal = SERVER.estusFlaskBaseHealConfig.get();
            Server.estusFlaskHealPerLevel = SERVER.estusFlaskHealPerLevelConfig.get();
            Server.reinforceDamagePerLevel = SERVER.reinforceDamagePerLevelConfig.get();
        }
    }
}
