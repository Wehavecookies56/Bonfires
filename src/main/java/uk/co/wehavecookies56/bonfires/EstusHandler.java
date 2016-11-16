package uk.co.wehavecookies56.bonfires;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */

public class EstusHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(EstusHandler.IEstusHandler.class, new Storage(), EstusHandler.DefaultEstusHandler.class);
        MinecraftForge.EVENT_BUS.register(new EstusHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "estus"), new Provider());
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        final IEstusHandler original = getHandler(event.getOriginal());
        final IEstusHandler clone = getHandler(event.getEntity());
        clone.setUses(original.uses());
    }

    public static IEstusHandler getHandler(Entity entity) {
        if (entity.hasCapability(CAPABILITY_ESTUS, null)) {
            return entity.getCapability(CAPABILITY_ESTUS, null);
        }
        return null;
    }

    @CapabilityInject(IEstusHandler.class)
    public static final Capability<IEstusHandler> CAPABILITY_ESTUS = null;

    public interface IEstusHandler {
        int uses();
        void setUses(int uses);
    }

    public static class DefaultEstusHandler implements IEstusHandler {
        private int uses = 3;
        private UUID bonfire = null;

        @Override
        public int uses() {
            return this.uses;
        }

        @Override
        public void setUses(int uses) {
            this.uses = uses;
        }
    }

    public static class Storage implements Capability.IStorage<IEstusHandler> {

        @Override
        public NBTBase writeNBT(Capability<IEstusHandler> capability, IEstusHandler instance, EnumFacing side) {
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("uses", instance.uses());
            return tag;
        }

        @Override
        public void readNBT(Capability<IEstusHandler> capability, IEstusHandler instance, EnumFacing side, NBTBase nbt) {
            final NBTTagCompound tag = (NBTTagCompound) nbt;
            instance.setUses(tag.getInteger("uses"));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        IEstusHandler instance = CAPABILITY_ESTUS.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_ESTUS;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? CAPABILITY_ESTUS.<T>cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY_ESTUS.getStorage().writeNBT(CAPABILITY_ESTUS, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY_ESTUS.getStorage().readNBT(CAPABILITY_ESTUS, instance, null, nbt);
        }
    }

}
