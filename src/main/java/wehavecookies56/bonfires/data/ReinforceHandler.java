package wehavecookies56.bonfires.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.setup.ComponentSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */

public class ReinforceHandler {

    public static boolean canReinforce(ItemStack stack) {
        Item i = stack.getItem();
        for (String s : BonfiresConfig.Common.reinforceBlacklist) {
            if (BuiltInRegistries.ITEM.containsKey(new ResourceLocation(s))) {
                Item blacklistedItem = BuiltInRegistries.ITEM.get(new ResourceLocation(s));
                if (i == blacklistedItem) {
                    return false;
                }
            } else {
                Bonfires.LOGGER.info("Unable to find blacklisted item '" + s + "' in the registry");
            }
        }
        return i instanceof TieredItem || i instanceof SwordItem || i instanceof EstusFlaskItem;
    }

    public record ReinforceLevel(int level, int maxLevel) {
        public static final Codec<ReinforceLevel> CODEC = RecordCodecBuilder.create(
                reinforceInstance -> reinforceInstance.group(
                        Codec.INT.fieldOf("reinforce_level").forGetter(ReinforceLevel::level),
                        Codec.INT.fieldOf("reinforce_max").forGetter(ReinforceLevel::maxLevel)
                ).apply(reinforceInstance, ReinforceLevel::new)
        );
        public static final StreamCodec<FriendlyByteBuf, ReinforceLevel> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                ReinforceLevel::level,
                ByteBufCodecs.INT,
                ReinforceLevel::maxLevel,
                ReinforceLevel::new
        );
    }

    public static ReinforceLevel getReinforceLevel(ItemStack stack) {
        if (canReinforce(stack)) {
            if (stack.has(ComponentSetup.REINFORCE_LEVEL)) {
                return stack.get(ComponentSetup.REINFORCE_LEVEL);
            } else {
                ReinforceLevel level = new ReinforceLevel(0, 10);
                stack.set(ComponentSetup.REINFORCE_LEVEL, level);
                return level;
            }
        } else {
            return null;
        }
    }

    public static void levelUp(ItemStack stack) {
        ReinforceLevel current = getReinforceLevel(stack);
        if (current != null) {
            if (current.level() < stack.get(ComponentSetup.REINFORCE_LEVEL).maxLevel) {
                stack.set(ComponentSetup.REINFORCE_LEVEL, new ReinforceLevel(current.level()+1, stack.get(ComponentSetup.REINFORCE_LEVEL).maxLevel));
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
