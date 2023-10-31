package wehavecookies56.bonfires.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */

public class ReinforceHandler {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new ReinforceHandler());
    }

    public static boolean canReinforce(ItemStack stack) {
        Item i = stack.getItem();
        for (String s : BonfiresConfig.Common.reinforceBlacklist) {
            if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(s))) {
                Item blacklistedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
                if (i == blacklistedItem) {
                    return false;
                }
            } else {
                Bonfires.LOGGER.info("Unable to find blacklisted item '" + s + "' in the registry");
            }
        }
        return i instanceof TieredItem || i instanceof SwordItem || i instanceof EstusFlaskItem;
    }

    public static final class ReinforceLevel {
        private int level;
        private int maxLevel;

        private ReinforceLevel(int level, int maxLevel) {
            this.level = level;
            this.maxLevel = maxLevel;
        }

        public int level() {
            return level;
        }

        public int maxLevel() {
            return maxLevel;
        }
    }

    public static ReinforceLevel getReinforceLevel(ItemStack stack) {
        int levelFromCap = 0;
        int maxLevelFromCap = 10;
        if (canReinforce(stack)) {
            if (stack.hasTag()) {
                if (stack.getTag().contains("reinforce_level")) {
                    return new ReinforceLevel(stack.getTag().getInt("reinforce_level"), stack.getTag().getInt("reinforce_max"));
                } else {
                    stack.getTag().putInt("reinforce_level", levelFromCap);
                    stack.getTag().putInt("reinforce_max", maxLevelFromCap);
                    return new ReinforceLevel(levelFromCap, maxLevelFromCap);
                }
            } else {
                CompoundTag tag = new CompoundTag();
                tag.putInt("reinforce_level", levelFromCap);
                tag.putInt("reinforce_max", maxLevelFromCap);
                stack.setTag(tag);
                return new ReinforceLevel(levelFromCap, maxLevelFromCap);
            }
        } else {
            return null;
        }
    }

    public static void levelUp(ItemStack stack) {
        ReinforceLevel current = getReinforceLevel(stack);
        if (current != null) {
            if (current.level() < stack.getTag().getInt("reinforce_max")) {
                stack.getTag().putInt("reinforce_level", current.level()+1);
            }
        }
    }

    public static ItemStack getRequiredResources(ItemStack toReinforce) {
        if (getReinforceLevel(toReinforce) != null) {
            ReinforceLevel rlevel = getReinforceLevel(toReinforce);
            if (toReinforce.getItem() == ItemSetup.estus_flask.get()) {
                if (rlevel.level() < rlevel.maxLevel()) {
                    return new ItemStack(ItemSetup.undead_bone_shard.get());
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (rlevel.level() < rlevel.maxLevel()) {
                int cost = 1;
                Item material = ItemStack.EMPTY.getItem();
                if (rlevel.level() >= 0 && rlevel.level() <= 2) {
                    cost = 2 * (rlevel.level()+1);
                    material = ItemSetup.titanite_shard.get();
                }
                if (rlevel.level() >= 3 && rlevel.level() <= 5) {
                    cost = 2 * ((rlevel.level()+1)-3);
                    material = ItemSetup.large_titanite_shard.get();
                }
                if (rlevel.level() >= 6 && rlevel.level() <= 8) {
                    cost = 2 * ((rlevel.level()+1)-6);
                    material = ItemSetup.titanite_chunk.get();
                }
                if (rlevel.level() >= 9 && rlevel.level() < 10) {
                    cost = 1;
                    material = ItemSetup.titanite_slab.get();
                }
                return new ItemStack(material, cost);
            }

        }
        return ItemStack.EMPTY;
    }

    public static boolean hasRequiredItems(Player player, ItemStack required) {
        int hasCount = 0;
        int countRequired = required.getCount();
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            if (ItemStack.matches(required, player.getInventory().getItem(i))) {
                return true;
            } else {
                if (ItemStack.isSameItem(player.getInventory().getItem(i), required)) {
                    hasCount += player.getInventory().getItem(i).getCount();
                }
            }
        }
        if (hasCount >= countRequired) {
            return true;
        } else {
            return false;
        }
    }

    public static void removeRequiredItems(Player player, ItemStack required) {
        if (hasRequiredItems(player, required)) {
            int remaining = required.getCount();
            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (ItemStack.isSameItem(required, item)) {
                    if (item.getCount() >= remaining) {
                        item.shrink(remaining);
                        return;
                    } else {
                        remaining -= item.getCount();
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                }
                if (remaining == 0) {
                    return;
                }
            }
        }
    }

}
