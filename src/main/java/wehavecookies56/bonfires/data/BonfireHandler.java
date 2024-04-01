package wehavecookies56.bonfires.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.LevelResource;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;

import java.io.*;
import java.util.UUID;

public class BonfireHandler extends SavedData {

    BonfireRegistry registry = new BonfireRegistry();
    private boolean loadedOldData = false;

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

    public void loadOldBonfireData(MinecraftServer server) {
        if (!loadedOldData) {
            File worldDataFolder = server.getWorldPath(new LevelResource("data")).toFile();
            File capabilitiesDat = new File(worldDataFolder, "capabilities.dat");
            if (capabilitiesDat.exists()) {
                try {
                    Bonfires.LOGGER.info("Attempting to convert existing Bonfire data");
                    FileInputStream fileinputstream = new FileInputStream(capabilitiesDat);
                    PushbackInputStream pushbackinputstream = new PushbackInputStream(fileinputstream, 2);
                    DataInputStream inputStream = new DataInputStream(pushbackinputstream);
                    CompoundTag main = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
                    CompoundTag data = main.getCompound("data").getCompound("bonfires:bonfire");
                    BonfireRegistry reg = new BonfireRegistry();
                    reg.readFromNBT(data, reg.getBonfires());
                    reg.getBonfires().entrySet().forEach(entry -> {
                        this.addBonfire(entry.getValue());
                    });
                    loadedOldData = true;
                    this.setDirty();
                    Bonfires.LOGGER.info("Existing data successfully loaded");
                } catch (IOException e) {
                    Bonfires.LOGGER.info("Existing data either doesn't exist or failed to load, ignoring.");
                    loadedOldData = true;
                    this.setDirty();
                }
            }
        }
    }

    private static BonfireHandler create() {
        return new BonfireHandler();
    }

    private static BonfireHandler load(CompoundTag tag) {
        BonfireHandler data = BonfireHandler.create();
        data.getRegistry().readFromNBT(tag, data.getRegistry().getBonfires());
        data.loadedOldData = tag.getBoolean("loaded_old_data");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        getRegistry().writeToNBT(tag, getRegistry().getBonfires());
        tag.putBoolean("loaded_old_data", loadedOldData);
        return tag;
    }

    public static BonfireHandler getServerHandler(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(BonfireHandler::create, BonfireHandler::load, DataFixTypes.LEVEL), "bonfires_data");
    }

}
