package wehavecookies56.bonfires.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wehavecookies56.bonfires.Bonfires;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */

public class EstusHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(EstusHandler.IEstusHandler.class, new Storage(), Default::new);
        MinecraftForge.EVENT_BUS.register(new EstusHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "estus"), new Provider());
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        final IEstusHandler original = getHandler(event.getOriginal());
        final IEstusHandler clone = getHandler(event.getPlayer());
        clone.setUses(original.uses());
        clone.setLastRested(original.lastRested());
    }

    public static IEstusHandler getHandler(PlayerEntity player) {
        LazyOptional<IEstusHandler> estusHandler = player.getCapability(CAPABILITY_ESTUS, null);
        return estusHandler.orElse(null);
    }

    @CapabilityInject(IEstusHandler.class)
    public static final Capability<IEstusHandler> CAPABILITY_ESTUS = null;

    public interface IEstusHandler {
        UUID lastRested();
        void setLastRested(UUID id);
        int uses();
        void setUses(int uses);
    }

    public static class Default implements IEstusHandler {
        private int uses = 3;
        private UUID bonfire;

        @Override
        public UUID lastRested() {
            return bonfire;
        }

        @Override
        public void setLastRested(UUID id) {
            bonfire = id;
        }

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

        @Nullable
        @Override
        public INBT writeNBT(Capability<IEstusHandler> capability, IEstusHandler instance, Direction side) {
            final CompoundNBT tag = new CompoundNBT();
            tag.putInt("uses", instance.uses());
            if (instance.lastRested() != null) {
                tag.putUUID("lastRested", instance.lastRested());
            }
            return tag;
        }

        @Override
        public void readNBT(Capability<IEstusHandler> capability, IEstusHandler instance, Direction side, INBT nbt) {
            final CompoundNBT tag = (CompoundNBT) nbt;
            instance.setUses(tag.getInt("uses"));
            if (tag.contains("lastRested")) {
                instance.setLastRested(tag.getUUID("lastRested"));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        IEstusHandler instance = CAPABILITY_ESTUS.getDefaultInstance();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_ESTUS.orEmpty(cap, LazyOptional.of(() -> instance));
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) CAPABILITY_ESTUS.getStorage().writeNBT(CAPABILITY_ESTUS, instance, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            CAPABILITY_ESTUS.getStorage().readNBT(CAPABILITY_ESTUS, instance, null, nbt);
        }
    }

}
