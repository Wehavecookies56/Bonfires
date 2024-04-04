package wehavecookies56.bonfires.data;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.mixins.WorldSavePathMixin;

import java.io.*;
import java.util.UUID;

public class BonfireHandler implements WorldComponentInitializer {
    public static final ComponentKey<IBonfireHandler> BONFIRES = ComponentRegistry.getOrCreate(new Identifier(Bonfires.modid, "bonfires"), IBonfireHandler.class);

    public static IBonfireHandler getServerHandler(MinecraftServer server) {
        return BONFIRES.get(server.getOverworld());
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(BONFIRES, Default::new);
    }

    public interface IBonfireHandler extends AutoSyncedComponent {
        BonfireRegistry getRegistry();
        boolean addBonfire(Bonfire bonfire);
        boolean removeBonfire(UUID id);
        boolean loadedOldData();
        void loadOldBonfireData(MinecraftServer server);
    }

    public static class Default implements IBonfireHandler {

        World provider;
        boolean loadedOldData = false;

        public Default(World provider) {
            this.provider = provider;
        }
        BonfireRegistry registry = new BonfireRegistry();

        @Override
        public BonfireRegistry getRegistry() {
            return registry;
        }

        @Override
        public boolean addBonfire(Bonfire bonfire) {
            boolean result = registry.addBonfire(bonfire);
            BONFIRES.sync(provider);
            return result;
        }

        @Override
        public boolean removeBonfire(UUID id) {
            boolean result = registry.removeBonfire(id);
            BONFIRES.sync(provider);
            return result;
        }

        @Override
        public void loadOldBonfireData(MinecraftServer server) {
            if (!loadedOldData) {
                File worldDataFolder = server.getSavePath(WorldSavePathMixin.create("data")).toFile();
                File capabilitiesDat = new File(worldDataFolder, "capabilities.dat");
                if (capabilitiesDat.exists()) {
                    try {
                        Bonfires.LOGGER.info("Attempting to convert existing Bonfire data from capabilities.dat");
                        FileInputStream fileinputstream = new FileInputStream(capabilitiesDat);
                        PushbackInputStream pushbackinputstream = new PushbackInputStream(fileinputstream, 2);
                        DataInputStream inputStream = new DataInputStream(pushbackinputstream);
                        NbtCompound main = NbtIo.readCompressed(inputStream);
                        NbtCompound data = main.getCompound("data");
                        if (data.contains("bonfires:bonfire")) {
                            NbtCompound bonfires = data.getCompound("bonfires:bonfire");
                            BonfireRegistry reg = new BonfireRegistry();
                            reg.readFromNBT(bonfires, reg.getBonfires());
                            reg.getBonfires().entrySet().forEach(entry -> {
                                this.addBonfire(entry.getValue());
                            });
                            loadedOldData = true;
                            main.getCompound("data").remove("bonfires:bonfire");
                            NbtIo.writeCompressed(main, capabilitiesDat);
                            BONFIRES.sync(provider);
                            Bonfires.LOGGER.info("Existing old data successfully loaded");
                        }
                    } catch (IOException e) {
                        Bonfires.LOGGER.info("Existing data either doesn't exist or failed to load, ignoring.");
                        loadedOldData = true;
                        BONFIRES.sync(provider);
                    }
                }
                if (!loadedOldData) {
                    File bonfireDataDat = new File(worldDataFolder, "bonfires_data.dat");
                    if (bonfireDataDat.exists()) {
                        try {
                            Bonfires.LOGGER.info("Attempting to convert existing Bonfire data from bonfires_data.dat");
                            FileInputStream fileinputstream = new FileInputStream(bonfireDataDat);
                            PushbackInputStream pushbackinputstream = new PushbackInputStream(fileinputstream, 2);
                            DataInputStream inputStream = new DataInputStream(pushbackinputstream);
                            NbtCompound main = NbtIo.readCompressed(inputStream);
                            NbtCompound data = main.getCompound("data");
                            if (!data.isEmpty()) {
                                BonfireRegistry reg = new BonfireRegistry();
                                reg.readFromNBT(data, reg.getBonfires());
                                reg.getBonfires().entrySet().forEach(entry -> {
                                    this.addBonfire(entry.getValue());
                                });
                                loadedOldData = true;
                                main.remove("data");
                                main.put("data", new NbtCompound());
                                NbtIo.writeCompressed(main, bonfireDataDat);
                                BONFIRES.sync(provider);
                                Bonfires.LOGGER.info("Existing old data successfully loaded");
                            }
                        } catch (IOException e) {
                            Bonfires.LOGGER.info("Existing data either doesn't exist or failed to load, ignoring.");
                            loadedOldData = true;
                            BONFIRES.sync(provider);
                        }
                    }
                }
            }
        }

        @Override
        public boolean loadedOldData() {
            return loadedOldData;
        }

        @Override
        public void readFromNbt(NbtCompound tag) {
            getRegistry().readFromNBT(tag, getRegistry().getBonfires());
            loadedOldData = tag.getBoolean("loaded_old_data");
        }

        @Override
        public void writeToNbt(NbtCompound tag) {
            getRegistry().writeToNBT(tag, getRegistry().getBonfires());
            tag.putBoolean("loaded_old_data", loadedOldData());
        }
    }

}
