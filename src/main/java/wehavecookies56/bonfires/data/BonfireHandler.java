package wehavecookies56.bonfires.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class BonfireHandler {

    public static void init() {
        CapabilityManager.INSTANCE.register(IBonfireHandler.class, new Storage(), Default::new);
        MinecraftForge.EVENT_BUS.register(new BonfireHandler());
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<World> event) {
        event.addCapability(new ResourceLocation(Bonfires.modid, "bonfire"), new Provider());
    }

    public static IBonfireHandler getHandler(World world) {
        LazyOptional<IBonfireHandler> bonfireHandler = world.getCapability(CAPABILITY_BONFIRE, null);
        return bonfireHandler.orElse(null);
    }

    public static IBonfireHandler getServerHandler(MinecraftServer server) {
        return getHandler(server.overworld());
    }

    @CapabilityInject(IBonfireHandler.class)
    public static final Capability<IBonfireHandler> CAPABILITY_BONFIRE = null;

    public interface IBonfireHandler {
        BonfireRegistry getRegistry();
        boolean addBonfire(Bonfire bonfire);
        boolean removeBonfire(UUID id);
    }

    public static class Default implements IBonfireHandler {

        BonfireRegistry registry = new BonfireRegistry();

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

    public static class Storage implements Capability.IStorage<IBonfireHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IBonfireHandler> capability, IBonfireHandler instance, Direction side) {
            final CompoundNBT tag = new CompoundNBT();
            instance.getRegistry().writeToNBT(tag, instance.getRegistry().getBonfires());
            return tag;
        }

        @Override
        public void readNBT(Capability<IBonfireHandler> capability, IBonfireHandler instance, Direction side, INBT nbt) {
            final CompoundNBT tag = (CompoundNBT) nbt;
            instance.getRegistry().readFromNBT(tag, instance.getRegistry().getBonfires());
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        IBonfireHandler instance = CAPABILITY_BONFIRE.getDefaultInstance();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CAPABILITY_BONFIRE.orEmpty(cap, LazyOptional.of(() -> instance));
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) CAPABILITY_BONFIRE.getStorage().writeNBT(CAPABILITY_BONFIRE, instance, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            CAPABILITY_BONFIRE.getStorage().readNBT(CAPABILITY_BONFIRE, instance, null, nbt);
        }
    }

}
