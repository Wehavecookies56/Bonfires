package wehavecookies56.bonfires.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.capabilities.*;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import wehavecookies56.bonfires.Bonfires;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */

public class EstusHandler {

    public static void init() {
        NeoForge.EVENT_BUS.register(new EstusHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "estus"), new Provider());
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        final IEstusHandler original = getHandler(event.getOriginal());
        final IEstusHandler clone = getHandler(event.getEntity());
        clone.setUses(original.uses());
        clone.setLastRested(original.lastRested());
        event.getOriginal().invalidateCaps();
    }

    public static IEstusHandler getHandler(Player player) {
        LazyOptional<IEstusHandler> estusHandler = player.getCapability(CAPABILITY_ESTUS, null);
        return estusHandler.orElse(null);
    }

    public static final Capability<IEstusHandler> CAPABILITY_ESTUS = CapabilityManager.get(new CapabilityToken<>() {});

    public interface IEstusHandler extends INBTSerializable<CompoundTag> {
        UUID lastRested();
        void setLastRested(UUID id);
        int uses();
        void setUses(int uses);
    }

    public static class Default implements IEstusHandler {
        private int uses = 3;
        private UUID bonfire;


        @Override
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();
            tag.putInt("uses", uses());
            if (lastRested() != null) {
                tag.putUUID("lastRested", lastRested());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            setUses(tag.getInt("uses"));
            if (tag.contains("lastRested")) {
                setLastRested(tag.getUUID("lastRested"));
            }
        }

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

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        IEstusHandler instance = new Default();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_ESTUS.orEmpty(cap, LazyOptional.of(() -> instance));
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }

}
