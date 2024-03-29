package wehavecookies56.bonfires.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;

import java.util.UUID;

public class BonfireHandler extends SavedData {

    BonfireRegistry registry = new BonfireRegistry();

    public BonfireRegistry getRegistry() {
        return registry;
    }

    public boolean addBonfire(Bonfire bonfire) {
        boolean result = registry.addBonfire(bonfire);
        setDirty();
        return result;
    }

    public boolean removeBonfire(UUID id) {
        boolean result = registry.removeBonfire(id);
        setDirty();
        return result;
    }

    private static BonfireHandler create() {
        return new BonfireHandler();
    }

    private static BonfireHandler load(CompoundTag tag) {
        BonfireHandler data = BonfireHandler.create();
        data.getRegistry().readFromNBT(tag, data.getRegistry().getBonfires());
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        getRegistry().writeToNBT(tag, getRegistry().getBonfires());
        return tag;
    }

    public static BonfireHandler getServerHandler(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(BonfireHandler::create, BonfireHandler::load), "bonfires_data");
    }

}
