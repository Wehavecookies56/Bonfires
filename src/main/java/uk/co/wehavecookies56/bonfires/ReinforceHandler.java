package uk.co.wehavecookies56.bonfires;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import uk.co.wehavecookies56.bonfires.items.ItemEstusFlask;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */

public class ReinforceHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(ReinforceHandler.IReinforceHandler.class, new Storage(), ReinforceHandler.Default.class);
        MinecraftForge.EVENT_BUS.register(new ReinforceHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ItemTool || event.getObject().getItem() instanceof ItemSword || event.getObject().getItem() instanceof ItemEstusFlask) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "reinforce"), new Provider());
        }
    }

    public static IReinforceHandler getHandler(ItemStack stack) {
        if (stack.hasCapability(CAPABILITY_REINFORCE, null)) {
            return stack.getCapability(CAPABILITY_REINFORCE, null);
        }
        return null;
    }

    public static boolean hasHandler(ItemStack stack) {
        if (stack.hasCapability(CAPABILITY_REINFORCE, null)) {
            return true;
        }
        return false;
    }

    public static ItemStack getRequiredResources(ItemStack toReinforce) {
        if (hasHandler(toReinforce)) {
            if (toReinforce.getItem() == Bonfires.estusFlask) {
                if (getHandler(toReinforce).level() < getHandler(toReinforce).maxLevel()) {
                    return new ItemStack(Bonfires.undeadBoneShard);
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (getHandler(toReinforce).level() < getHandler(toReinforce).maxLevel()) {
                int cost = 1;
                Item material = ItemStack.EMPTY.getItem();
                if (getHandler(toReinforce).level() >= 0 && getHandler(toReinforce).level() <= 2) {
                    cost = 2 * (getHandler(toReinforce).level()+1);
                    material = Bonfires.titaniteShard;
                }
                if (getHandler(toReinforce).level() >= 3 && getHandler(toReinforce).level() <= 5) {
                    cost = 2 * ((getHandler(toReinforce).level()+1)-3);
                    material = Bonfires.largeTitaniteShard;
                }
                if (getHandler(toReinforce).level() >= 6 && getHandler(toReinforce).level() <= 8) {
                    cost = 2 * ((getHandler(toReinforce).level()+1)-6);
                    material = Bonfires.titaniteChunk;
                }
                if (getHandler(toReinforce).level() >= 9 && getHandler(toReinforce).level() < 10) {
                    cost = 1;
                    material = Bonfires.titaniteSlab;
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

        @Override
        public NBTBase writeNBT(Capability<IReinforceHandler> capability, IReinforceHandler instance, EnumFacing side) {
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("level", instance.level());
            tag.setInteger("maxLevel", instance.maxLevel());
            return tag;
        }

        @Override
        public void readNBT(Capability<IReinforceHandler> capability, IReinforceHandler instance, EnumFacing side, NBTBase nbt) {
            final NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.setLevel(tag.getInteger("level"));
            instance.setMaxLevel(tag.getInteger("maxLevel"));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        IReinforceHandler instance = CAPABILITY_REINFORCE.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_REINFORCE;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? CAPABILITY_REINFORCE.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY_REINFORCE.getStorage().writeNBT(CAPABILITY_REINFORCE, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY_REINFORCE.getStorage().readNBT(CAPABILITY_REINFORCE, instance, null, nbt);
        }
    }

    public static boolean hasRequiredItems(EntityPlayer player, ItemStack required) {
        int hasCount = 0;
        int countRequired = required.getCount();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (ItemStack.areItemStacksEqual(required, player.inventory.getStackInSlot(i))) {
                return true;
            } else {
                if (ItemStack.areItemsEqual(player.inventory.getStackInSlot(i), required)) {
                    hasCount += player.inventory.getStackInSlot(i).getCount();
                }
            }
        }
        if (hasCount >= countRequired) {
            return true;
        } else {
            return false;
        }
    }

    public static void removeRequiredItems(EntityPlayer player, ItemStack required) {
        if (hasRequiredItems(player, required)) {
            int remaining = required.getCount();
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                if (ItemStack.areItemStacksEqual(required, player.inventory.getStackInSlot(i))) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    remaining = 0;
                    return;
                } else {
                    if (ItemStack.areItemsEqual(player.inventory.getStackInSlot(i), required)) {
                        if (player.inventory.getStackInSlot(i).getCount() >= remaining) {
                            ItemStack stackWithNewCount = player.inventory.getStackInSlot(i);
                            stackWithNewCount.shrink(remaining);
                            player.inventory.setInventorySlotContents(i, stackWithNewCount);
                            remaining = 0;
                            return;
                        } else {
                            player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            remaining -= player.inventory.getStackInSlot(i).getCount();
                        }

                    }
                }
            }
        }
    }

}
