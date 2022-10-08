package wehavecookies56.bonfires.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IReinforceHandler.class);
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        for (String s : BonfiresConfig.Common.reinforceBlacklist) {
            if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(s))) {
                Item blacklistedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
                if (event.getObject().getItem() == blacklistedItem) {
                    return;
                }
            } else {
                Bonfires.LOGGER.info("Unable to find blacklisted item '" + s + "' in the registry");
            }
        }
        if (event.getObject().getItem() instanceof TieredItem || event.getObject().getItem() instanceof SwordItem || event.getObject().getItem() instanceof EstusFlaskItem) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "reinforce"), new Provider());
        }
    }

    public static IReinforceHandler getHandler(ItemStack stack) {
        return stack.getCapability(CAPABILITY_REINFORCE, null).orElse(null);
    }

    public static boolean hasHandler(ItemStack stack) {
        return stack.getCapability(CAPABILITY_REINFORCE, null).isPresent();
    }

    public static ItemStack getRequiredResources(ItemStack toReinforce) {
        if (hasHandler(toReinforce)) {
            if (toReinforce.getItem() == ItemSetup.estus_flask.get()) {
                if (getHandler(toReinforce).level() < getHandler(toReinforce).maxLevel()) {
                    return new ItemStack(ItemSetup.undead_bone_shard.get());
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (getHandler(toReinforce).level() < getHandler(toReinforce).maxLevel()) {
                int cost = 1;
                Item material = ItemStack.EMPTY.getItem();
                if (getHandler(toReinforce).level() >= 0 && getHandler(toReinforce).level() <= 2) {
                    cost = 2 * (getHandler(toReinforce).level()+1);
                    material = ItemSetup.titanite_shard.get();
                }
                if (getHandler(toReinforce).level() >= 3 && getHandler(toReinforce).level() <= 5) {
                    cost = 2 * ((getHandler(toReinforce).level()+1)-3);
                    material = ItemSetup.large_titanite_shard.get();
                }
                if (getHandler(toReinforce).level() >= 6 && getHandler(toReinforce).level() <= 8) {
                    cost = 2 * ((getHandler(toReinforce).level()+1)-6);
                    material = ItemSetup.titanite_chunk.get();
                }
                if (getHandler(toReinforce).level() >= 9 && getHandler(toReinforce).level() < 10) {
                    cost = 1;
                    material = ItemSetup.titanite_slab.get();
                }
                return new ItemStack(material, cost);
            }

        }
        return ItemStack.EMPTY;
    }

    public static final Capability<IReinforceHandler> CAPABILITY_REINFORCE = CapabilityManager.get(new CapabilityToken<>() {});

    public interface IReinforceHandler extends INBTSerializable<CompoundTag> {
        int level();
        int maxLevel();
        void setLevel(int level);
        void levelup(int levelup);
        void setMaxLevel(int maxLevel);
    }

    public static class Default implements IReinforceHandler {
        private int level = 0;
        private int maxLevel = 10;

        @Override
        public int level() {
            return level;
        }

        @Override
        public void setLevel(int level) {
            if (level > maxLevel) {
                this.level = maxLevel;
            } else {
                this.level = level;
            }
        }

        @Override
        public void levelup(int levelup) {
            if (level + levelup <= maxLevel) {
                level += levelup;
            } else {
                level = maxLevel;
            }
        }

        @Override
        public int maxLevel() {
            return maxLevel;
        }

        @Override
        public void setMaxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
        }

        @Override
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();
            tag.putInt("level", level());
            tag.putInt("maxLevel", maxLevel());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            setLevel(tag.getInt("level"));
            setMaxLevel(tag.getInt("maxLevel"));
        }
    }

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        IReinforceHandler instance = new ReinforceHandler.Default();

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_REINFORCE.orEmpty(cap, LazyOptional.of(() -> instance));
        }
    }

    public static boolean hasRequiredItems(Player player, ItemStack required) {
        int hasCount = 0;
        int countRequired = required.getCount();
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            if (ItemStack.matches(required, player.getInventory().getItem(i))) {
                return true;
            } else {
                if (ItemStack.isSame(player.getInventory().getItem(i), required)) {
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
                if (ItemStack.isSame(required, item)) {
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
