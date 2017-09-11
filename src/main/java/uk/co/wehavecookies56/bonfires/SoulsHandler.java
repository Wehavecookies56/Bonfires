package uk.co.wehavecookies56.bonfires;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.co.wehavecookies56.bonfires.items.ItemEstusFlask;

import javax.annotation.Nullable;

/**
 * Created by Toby on 05/11/2016.
 */

public class SoulsHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(SoulsHandler.ISoulsHandler.class, new Storage(), SoulsHandler.Default.class);
        MinecraftForge.EVENT_BUS.register(new SoulsHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "souls"), new Provider());
        }
    }

    public static ISoulsHandler getHandler(EntityPlayer player) {
        if (player.hasCapability(CAPABILITY_SOULS, null)) {
            return player.getCapability(CAPABILITY_SOULS, null);
        }
        return null;
    }

    public static boolean hasHandler(EntityPlayer player) {
        if (player.hasCapability(CAPABILITY_SOULS, null)) {
            return true;
        }
        return false;
    }

    @CapabilityInject(ISoulsHandler.class)
    public static final Capability<ISoulsHandler> CAPABILITY_SOULS = null;

    public interface ISoulsHandler {
        int souls();
        void setSouls(int souls);
        void addSouls(int souls);
        void takeSouls(int souls);
    }

    public static class Default implements ISoulsHandler {
        private int souls = 0;

        @Override
        public int souls() {
            return souls;
        }

        @Override
        public void setSouls(int souls) {
            this.souls = souls;
        }

        @Override
        public void addSouls(int souls) {
            this.souls += souls;
        }

        @Override
        public void takeSouls(int souls) {
            this.souls -= souls;
        }
    }

    public static class Storage implements Capability.IStorage<ISoulsHandler> {

        @Override
        public NBTBase writeNBT(Capability<ISoulsHandler> capability, ISoulsHandler instance, EnumFacing side) {
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("souls", instance.souls());
            return tag;
        }

        @Override
        public void readNBT(Capability<ISoulsHandler> capability, ISoulsHandler instance, EnumFacing side, NBTBase nbt) {
            final NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.setSouls(tag.getInteger("souls"));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        ISoulsHandler instance = CAPABILITY_SOULS.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_SOULS;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? CAPABILITY_SOULS.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY_SOULS.getStorage().writeNBT(CAPABILITY_SOULS, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY_SOULS.getStorage().readNBT(CAPABILITY_SOULS, instance, null, nbt);
        }
    }

}
