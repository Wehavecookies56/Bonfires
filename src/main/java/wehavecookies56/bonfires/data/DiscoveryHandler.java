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

import java.time.Instant;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DiscoveryHandler {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new DiscoveryHandler());
    }

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {}

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(Bonfires.modid, "discovery"), new DiscoveryHandler.Provider());
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        final IDiscoveryHandler original = getHandler(event.getOriginal());
        final IDiscoveryHandler clone = getHandler(event.getEntity());

        event.getOriginal().invalidateCaps();
    }

    public static IDiscoveryHandler getHandler(Player player) {
        LazyOptional<IDiscoveryHandler> discoveryHandler = player.getCapability(CAPABILITY_DISCOVERY, null);
        return discoveryHandler.orElse(null);
    }

    public static final Capability<IDiscoveryHandler> CAPABILITY_DISCOVERY = CapabilityManager.get(new CapabilityToken<>() {});

    public interface IDiscoveryHandler extends INBTSerializable<CompoundTag> {
        void setDiscovered(UUID bonfire, Instant time);
        void discover(UUID bonfire);
        Map<UUID, Instant> getDiscovered();
    }

    public static class Default implements IDiscoveryHandler {
        private Map<UUID, Instant> discovered = new HashMap<>();

        @Override
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();
            discovered.forEach((uuid, instant) -> {
                CompoundTag time = new CompoundTag();
                time.putLong("epoch", instant.getEpochSecond());
                time.putInt("nano", instant.getNano());
                tag.put(uuid.toString(), time);
            });
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            tag.getAllKeys().forEach(uuidString -> {
                CompoundTag timeTag = tag.getCompound(uuidString);
                Instant time = Instant.ofEpochSecond(timeTag.getLong("epoch"), timeTag.getInt("nano"));
                discovered.put(UUID.fromString(uuidString), time);
            });
        }

        @Override
        public void setDiscovered(UUID bonfire, Instant time) {
            discovered.put(bonfire, time);
        }

        @Override
        public void discover(UUID bonfire) {
            if (!discovered.containsKey(bonfire)) {
                discovered.put(bonfire, Instant.now());
            }
        }

        @Override
        public Map<UUID, Instant> getDiscovered() {
            return discovered;
        }
    }

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        IDiscoveryHandler instance = new DiscoveryHandler.Default();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_DISCOVERY.orEmpty(cap, LazyOptional.of(() -> instance));
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
