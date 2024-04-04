package wehavecookies56.bonfires.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */

public class ReinforceHandler {

    public static boolean canReinforce(ItemStack stack) {
        Item i = stack.getItem();
        for (String s : Bonfires.CONFIG.common.reinforceBlacklist()) {
            if (Registries.ITEM.containsId(new Identifier(s))) {
                Item blacklistedItem = Registries.ITEM.get(new Identifier(s));
                if (i == blacklistedItem) {
                    return false;
                }
            } else {
                Bonfires.LOGGER.info("Unable to find blacklisted item '" + s + "' in the registry");
            }
        }
        return i instanceof ToolItem || i instanceof SwordItem || i instanceof EstusFlaskItem;
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
            if (stack.hasNbt()) {
                if (stack.getNbt().contains("reinforce_level")) {
                    return new ReinforceLevel(stack.getNbt().getInt("reinforce_level"), stack.getNbt().getInt("reinforce_max"));
                } else {
                    stack.getNbt().putInt("reinforce_level", levelFromCap);
                    stack.getNbt().putInt("reinforce_max", maxLevelFromCap);
                    return new ReinforceLevel(levelFromCap, maxLevelFromCap);
                }
            } else {
                NbtCompound tag = new NbtCompound();
                tag.putInt("reinforce_level", levelFromCap);
                tag.putInt("reinforce_max", maxLevelFromCap);
                stack.setNbt(tag);
                return new ReinforceLevel(levelFromCap, maxLevelFromCap);
            }
        } else {
            return null;
        }
    }

    public static void levelUp(ItemStack stack) {
        ReinforceLevel current = getReinforceLevel(stack);
        if (current != null) {
            if (current.level() < stack.getNbt().getInt("reinforce_max")) {
                stack.getNbt().putInt("reinforce_level", current.level()+1);
            }
        }
    }

    public static ItemStack getRequiredResources(ItemStack toReinforce) {
        if (getReinforceLevel(toReinforce) != null) {
            ReinforceLevel rlevel = getReinforceLevel(toReinforce);
            if (toReinforce.getItem() == ItemSetup.estus_flask) {
                if (rlevel.level() < rlevel.maxLevel()) {
                    return new ItemStack(ItemSetup.undead_bone_shard);
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (rlevel.level() < rlevel.maxLevel()) {
                int cost = 1;
                Item material = ItemStack.EMPTY.getItem();
                if (rlevel.level() >= 0 && rlevel.level() <= 2) {
                    cost = 2 * (rlevel.level()+1);
                    material = ItemSetup.titanite_shard;
                }
                if (rlevel.level() >= 3 && rlevel.level() <= 5) {
                    cost = 2 * ((rlevel.level()+1)-3);
                    material = ItemSetup.large_titanite_shard;
                }
                if (rlevel.level() >= 6 && rlevel.level() <= 8) {
                    cost = 2 * ((rlevel.level()+1)-6);
                    material = ItemSetup.titanite_chunk;
                }
                if (rlevel.level() >= 9 && rlevel.level() < 10) {
                    cost = 1;
                    material = ItemSetup.titanite_slab;
                }
                return new ItemStack(material, cost);
            }

        }
        return ItemStack.EMPTY;
    }

    public static boolean hasRequiredItems(PlayerEntity player, ItemStack required) {
        int hasCount = 0;
        int countRequired = required.getCount();
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (ItemStack.areEqual(required, player.getInventory().getStack(i))) {
                return true;
            } else {
                if (ItemStack.areItemsEqual(player.getInventory().getStack(i), required)) {
                    hasCount += player.getInventory().getStack(i).getCount();
                }
            }
        }
        if (hasCount >= countRequired) {
            return true;
        } else {
            return false;
        }
    }

    public static void removeRequiredItems(PlayerEntity player, ItemStack required) {
        if (hasRequiredItems(player, required)) {
            int remaining = required.getCount();
            for (int i = 0; i < player.getInventory().main.size(); i++) {
                ItemStack item = player.getInventory().getStack(i);
                if (ItemStack.areItemsEqual(required, item)) {
                    if (item.getCount() >= remaining) {
                        item.decrement(remaining);
                        return;
                    } else {
                        remaining -= item.getCount();
                        player.getInventory().setStack(i, ItemStack.EMPTY);
                    }
                }
                if (remaining == 0) {
                    return;
                }
            }
        }
    }

}
