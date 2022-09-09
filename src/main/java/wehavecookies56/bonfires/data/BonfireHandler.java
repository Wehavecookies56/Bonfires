package wehavecookies56.bonfires.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class BonfireHandler {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new BonfireHandler());
    }

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IBonfireHandler.class);
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(new ResourceLocation(Bonfires.modid, "bonfire"), new Provider());
    }

    public static IBonfireHandler getHandler(Level world) {
        LazyOptional<IBonfireHandler> bonfireHandler = world.getCapability(CAPABILITY_BONFIRE, null);
        return bonfireHandler.orElse(null);
    }

    public static IBonfireHandler getServerHandler(MinecraftServer server) {
        return getHandler(server.overworld());
    }

    public static final Capability<IBonfireHandler> CAPABILITY_BONFIRE = CapabilityManager.get(new CapabilityToken<>() {});

    public interface IBonfireHandler extends INBTSerializable<CompoundTag> {
        BonfireRegistry getRegistry();
        boolean addBonfire(Bonfire bonfire);
        boolean removeBonfire(UUID id);
    }

    public static class Default implements IBonfireHandler {

        BonfireRegistry registry = new BonfireRegistry();

        @Override
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();
            getRegistry().writeToNBT(tag, getRegistry().getBonfires());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            getRegistry().readFromNBT(tag, getRegistry().getBonfires());
        }

        @Override
        public BonfireRegistry getRegistry() {
            return registry;
        }

        @Override
        public boolean addBonfire(Bonfire bonfire) {
            return registry.addBonfire(bonfire);
        }

        @Override
        public boolean removeBonfire(UUID id) {
            return registry.removeBonfire(id);
        }

    }

    public static class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        IBonfireHandler instance = new Default();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_BONFIRE.orEmpty(cap, LazyOptional.of(() -> instance));
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
