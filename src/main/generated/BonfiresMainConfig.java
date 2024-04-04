package wehavecookies56.bonfires;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BonfiresMainConfig extends ConfigWrapper<wehavecookies56.bonfires.BonfiresConfig> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Boolean> client_renderTextAboveBonfire = this.optionForKey(this.keys.client_renderTextAboveBonfire);
    private final Option<java.util.List<java.lang.String>> client_tabIcons = this.optionForKey(this.keys.client_tabIcons);
    private final Option<java.lang.Boolean> client_renderScreenshotsInGui = this.optionForKey(this.keys.client_renderScreenshotsInGui);
    private final Option<java.lang.Boolean> client_enableAutomaticScreenshotOnCreation = this.optionForKey(this.keys.client_enableAutomaticScreenshotOnCreation);
    private final Option<java.lang.Integer> client_screenshotWaitTicks = this.optionForKey(this.keys.client_screenshotWaitTicks);
    private final Option<java.lang.Boolean> client_deleteScreenshotsOnDestroyed = this.optionForKey(this.keys.client_deleteScreenshotsOnDestroyed);
    private final Option<java.util.List<java.lang.String>> client_hiddenDimensions = this.optionForKey(this.keys.client_hiddenDimensions);
    private final Option<java.lang.Boolean> common_enableUBSBonfire = this.optionForKey(this.keys.common_enableUBSBonfire);
    private final Option<java.lang.Boolean> common_enableReinforcing = this.optionForKey(this.keys.common_enableReinforcing);
    private final Option<java.util.List<java.lang.String>> common_reinforceBlacklist = this.optionForKey(this.keys.common_reinforceBlacklist);
    private final Option<java.lang.Double> common_estusFlaskBaseHeal = this.optionForKey(this.keys.common_estusFlaskBaseHeal);
    private final Option<java.lang.Double> common_estusFlaskHealPerLevel = this.optionForKey(this.keys.common_estusFlaskHealPerLevel);
    private final Option<java.lang.Double> common_reinforceDamagePerLevel = this.optionForKey(this.keys.common_reinforceDamagePerLevel);

    private BonfiresMainConfig() {
        super(wehavecookies56.bonfires.BonfiresConfig.class);
    }

    private BonfiresMainConfig(Consumer<Jankson.Builder> janksonBuilder) {
        super(wehavecookies56.bonfires.BonfiresConfig.class, janksonBuilder);
    }

    public static BonfiresMainConfig createAndLoad() {
        var wrapper = new BonfiresMainConfig();
        wrapper.load();
        return wrapper;
    }

    public static BonfiresMainConfig createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new BonfiresMainConfig(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public final Client_ client = new Client_();
    public class Client_ implements Client {
        public boolean renderTextAboveBonfire() {
            return client_renderTextAboveBonfire.value();
        }

        public void renderTextAboveBonfire(boolean value) {
            client_renderTextAboveBonfire.set(value);
        }

        public java.util.List<java.lang.String> tabIcons() {
            return client_tabIcons.value();
        }

        public void tabIcons(java.util.List<java.lang.String> value) {
            client_tabIcons.set(value);
        }

        public boolean renderScreenshotsInGui() {
            return client_renderScreenshotsInGui.value();
        }

        public void renderScreenshotsInGui(boolean value) {
            client_renderScreenshotsInGui.set(value);
        }

        public boolean enableAutomaticScreenshotOnCreation() {
            return client_enableAutomaticScreenshotOnCreation.value();
        }

        public void enableAutomaticScreenshotOnCreation(boolean value) {
            client_enableAutomaticScreenshotOnCreation.set(value);
        }

        public int screenshotWaitTicks() {
            return client_screenshotWaitTicks.value();
        }

        public void screenshotWaitTicks(int value) {
            client_screenshotWaitTicks.set(value);
        }

        public boolean deleteScreenshotsOnDestroyed() {
            return client_deleteScreenshotsOnDestroyed.value();
        }

        public void deleteScreenshotsOnDestroyed(boolean value) {
            client_deleteScreenshotsOnDestroyed.set(value);
        }

        public java.util.List<java.lang.String> hiddenDimensions() {
            return client_hiddenDimensions.value();
        }

        public void hiddenDimensions(java.util.List<java.lang.String> value) {
            client_hiddenDimensions.set(value);
        }

    }
    public final Common_ common = new Common_();
    public class Common_ implements Common {
        public boolean enableUBSBonfire() {
            return common_enableUBSBonfire.value();
        }

        public void enableUBSBonfire(boolean value) {
            common_enableUBSBonfire.set(value);
        }

        public boolean enableReinforcing() {
            return common_enableReinforcing.value();
        }

        public void enableReinforcing(boolean value) {
            common_enableReinforcing.set(value);
        }

        public java.util.List<java.lang.String> reinforceBlacklist() {
            return common_reinforceBlacklist.value();
        }

        public void reinforceBlacklist(java.util.List<java.lang.String> value) {
            common_reinforceBlacklist.set(value);
        }

        public double estusFlaskBaseHeal() {
            return common_estusFlaskBaseHeal.value();
        }

        public void estusFlaskBaseHeal(double value) {
            common_estusFlaskBaseHeal.set(value);
        }

        public double estusFlaskHealPerLevel() {
            return common_estusFlaskHealPerLevel.value();
        }

        public void estusFlaskHealPerLevel(double value) {
            common_estusFlaskHealPerLevel.set(value);
        }

        public double reinforceDamagePerLevel() {
            return common_reinforceDamagePerLevel.value();
        }

        public void reinforceDamagePerLevel(double value) {
            common_reinforceDamagePerLevel.set(value);
        }

    }
    public interface Client {
        boolean renderTextAboveBonfire();
        void renderTextAboveBonfire(boolean value);
        java.util.List<java.lang.String> tabIcons();
        void tabIcons(java.util.List<java.lang.String> value);
        boolean renderScreenshotsInGui();
        void renderScreenshotsInGui(boolean value);
        boolean enableAutomaticScreenshotOnCreation();
        void enableAutomaticScreenshotOnCreation(boolean value);
        int screenshotWaitTicks();
        void screenshotWaitTicks(int value);
        boolean deleteScreenshotsOnDestroyed();
        void deleteScreenshotsOnDestroyed(boolean value);
        java.util.List<java.lang.String> hiddenDimensions();
        void hiddenDimensions(java.util.List<java.lang.String> value);
    }
    public interface Common {
        boolean enableUBSBonfire();
        void enableUBSBonfire(boolean value);
        boolean enableReinforcing();
        void enableReinforcing(boolean value);
        java.util.List<java.lang.String> reinforceBlacklist();
        void reinforceBlacklist(java.util.List<java.lang.String> value);
        double estusFlaskBaseHeal();
        void estusFlaskBaseHeal(double value);
        double estusFlaskHealPerLevel();
        void estusFlaskHealPerLevel(double value);
        double reinforceDamagePerLevel();
        void reinforceDamagePerLevel(double value);
    }
    public static class Keys {
        public final Option.Key client_renderTextAboveBonfire = new Option.Key("client.renderTextAboveBonfire");
        public final Option.Key client_tabIcons = new Option.Key("client.tabIcons");
        public final Option.Key client_renderScreenshotsInGui = new Option.Key("client.renderScreenshotsInGui");
        public final Option.Key client_enableAutomaticScreenshotOnCreation = new Option.Key("client.enableAutomaticScreenshotOnCreation");
        public final Option.Key client_screenshotWaitTicks = new Option.Key("client.screenshotWaitTicks");
        public final Option.Key client_deleteScreenshotsOnDestroyed = new Option.Key("client.deleteScreenshotsOnDestroyed");
        public final Option.Key client_hiddenDimensions = new Option.Key("client.hiddenDimensions");
        public final Option.Key common_enableUBSBonfire = new Option.Key("common.enableUBSBonfire");
        public final Option.Key common_enableReinforcing = new Option.Key("common.enableReinforcing");
        public final Option.Key common_reinforceBlacklist = new Option.Key("common.reinforceBlacklist");
        public final Option.Key common_estusFlaskBaseHeal = new Option.Key("common.estusFlaskBaseHeal");
        public final Option.Key common_estusFlaskHealPerLevel = new Option.Key("common.estusFlaskHealPerLevel");
        public final Option.Key common_reinforceDamagePerLevel = new Option.Key("common.reinforceDamagePerLevel");
    }
}

