package wehavecookies56.bonfires.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
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
        MinecraftForge.EVENT_BUS.register(new EstusHandler());
    }

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {}

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
    }

    public static class Default implements IEstusHandler {
        private UUID bonfire;

        @Override
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();
            if (lastRested() != null) {
                tag.putUUID("lastRested", lastRested());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
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
