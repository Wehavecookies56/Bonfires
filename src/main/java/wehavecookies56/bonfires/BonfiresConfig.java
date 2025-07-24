package wehavecookies56.bonfires;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Modmenu(modId = "bonfires")
@Config(name = "bonfires-config", wrapperName = "BonfiresMainConfig")
public class BonfiresConfig {

    @SectionHeader("Client")
    @Nest
    public Client client = new Client();

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @SectionHeader("Common")
    @Nest
    public Common common = new Common();

    public static class Client {
        public boolean renderTextAboveBonfire = true;
        @PredicateConstraint("validateIcons")
        public List<String> tabIcons = Arrays.asList("minecraft:overworld=minecraft:grass_block", "minecraft:the_nether=minecraft:netherrack", "minecraft:the_end=minecraft:end_stone", "kingdomkeys:dive_to_the_heart=kingdomkeys:mosaic_stained_glass");
        public boolean renderScreenshotsInGui = true;
        public boolean enableAutomaticScreenshotOnCreation = true;
        public boolean deleteScreenshotsOnDestroyed = true;
        @PredicateConstraint("validateRegistryKey")
        public List<String> hiddenDimensions = new ArrayList<>();
        public boolean disableBonfireParticles = false;
        public String bonfireAmbientSound = SoundEvents.BLOCK_CAMPFIRE_CRACKLE.getId().toString();

        public static boolean validateRegistryKey(List<String> input) {
            for (String entry : input) {
                if (!entry.contains(":") && entry.contains(" ")) {
                    return false;
                }
            }
            return true;
        }

        public static boolean validateIcons(List<String> input) {
            for (String entry : input) {
                if (entry.contains("=")) {
                    String[] split = entry.split("=");
                    if (split.length == 2) {
                        if (!split[0].contains(":") && split[1].contains(":")) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Common {
        public boolean enableUBSBonfire = true;
        public boolean enableReinforcing = true;
        @PredicateConstraint("validateRegistryKey")
        public List<String> reinforceBlacklist = new ArrayList<>();

        public double bonfireMonsterCheckRadius = 8.0D;
        public boolean repairEquipment = false;

        public double estusFlaskBaseHeal = 6;
        public double estusFlaskHealPerLevel = 1;
        public double reinforceDamagePerLevel = 0.5;

        public boolean bonfireDiscoveryMode = true;

        public boolean disableAshDrops = false;
        public boolean disableBonfireRespawn = false;

        public static boolean validateRegistryKey(List<String> input) {
            for (String entry : input) {
                if (!entry.contains(":") && entry.contains(" ")) {
                    return false;
                }
            }
            return true;
        }
    }
}
