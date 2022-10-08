package wehavecookies56.bonfires.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.setup.ItemSetup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Toby on 05/11/2016.
 */

public class ReinforceHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(ReinforceHandler.IReinforceHandler.class, new Storage(), Default::new);
        MinecraftForge.EVENT_BUS.register(new ReinforceHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        for (String s : BonfiresConfig.Common.reinforceBlacklist) {
            if (GameRegistry.findRegistry(Item.class).containsKey(new ResourceLocation(s))) {
                Item blacklistedItem = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(s));
                if (event.getObject().getItem() == blacklistedItem) {
                    return;
                }
            } else {
                Bonfires.LOGGER.info("Unable to find blacklisted item '" + s + "' in the registry");
            }
        }
        if (event.getObject().getItem() instanceof ToolItem || event.getObject().getItem() instanceof SwordItem || event.getObject().getItem() instanceof EstusFlaskItem) {
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

    @CapabilityInject(IReinforceHandler.class)
    public static final Capability<IReinforceHandler> CAPABILITY_REINFORCE = null;

    public interface IReinforceHandler {
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
    }

    public static class Storage implements Capability.IStorage<IReinforceHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IReinforceHandler> capability, IReinforceHandler instance, Direction side) {
            final CompoundNBT tag = new CompoundNBT();
            tag.putInt("level", instance.level());
            tag.putInt("maxLevel", instance.maxLevel());
            return tag;
        }

        @Override
        public void readNBT(Capability<IReinforceHandler> capability, IReinforceHandler instance, Direction side, INBT nbt) {
            final CompoundNBT tag = (CompoundNBT) nbt;
            instance.setLevel(tag.getInt("level"));
            instance.setMaxLevel(tag.getInt("maxLevel"));
        }
    }

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
        IReinforceHandler instance = CAPABILITY_REINFORCE.getDefaultInstance();

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) CAPABILITY_REINFORCE.getStorage().writeNBT(CAPABILITY_REINFORCE, instance, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            CAPABILITY_REINFORCE.getStorage().readNBT(CAPABILITY_REINFORCE, instance, null, nbt);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_REINFORCE.orEmpty(cap, LazyOptional.of(() -> instance));
        }
    }

    public static boolean hasRequiredItems(PlayerEntity player, ItemStack required) {
        int hasCount = 0;
        int countRequired = required.getCount();
        for (int i = 0; i < player.inventory.items.size(); i++) {
            if (ItemStack.matches(required, player.inventory.getItem(i))) {
                return true;
            } else {
                if (ItemStack.isSame(player.inventory.getItem(i), required)) {
                    hasCount += player.inventory.getItem(i).getCount();
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
            for (int i = 0; i < player.inventory.items.size(); i++) {
                ItemStack item = player.inventory.getItem(i);
                if (ItemStack.isSame(required, item)) {
                    if (item.getCount() >= remaining) {
                        item.shrink(remaining);
                        return;
                    } else {
                        remaining -= item.getCount();
                        player.inventory.setItem(i, ItemStack.EMPTY);
                    }
                }
                if (remaining == 0) {
                    return;
                }
            }
        }
    }

}
