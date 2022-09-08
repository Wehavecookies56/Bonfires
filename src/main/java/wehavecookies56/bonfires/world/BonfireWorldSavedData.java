package wehavecookies56.bonfires.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.PacketHandler;

import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class BonfireWorldSavedData extends WorldSavedData {

    private static final String DATA_NAME = Bonfires.modid + "_bonfiredata";

    public BonfireRegistry bonfires = new BonfireRegistry();

    public BonfireWorldSavedData() {
        super(DATA_NAME);
    }

    public BonfireWorldSavedData(String s) {
        super(s);
    }

    @Override
    public void load(CompoundNBT compound) {
        bonfires.readFromNBT(compound, bonfires.getBonfires());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        Bonfires.LOGGER.info("Saving Bonfires");
        return bonfires.writeToNBT(compound, bonfires.getBonfires());
    }

    public boolean addBonfire(Bonfire bonfire) {
        boolean added = bonfires.addBonfire(bonfire);
        setDirty();
        return added;
    }

    public boolean removeBonfire(UUID id) {
        boolean removed = bonfires.removeBonfire(id);
        setDirty();
        return removed;
    }

    public static BonfireWorldSavedData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getDataStorage();
        BonfireWorldSavedData instance = storage.get(BonfireWorldSavedData::new, DATA_NAME);
        if (instance == null) {
            instance = new BonfireWorldSavedData();
            storage.set(instance);
        }
        return instance;
    }

}
