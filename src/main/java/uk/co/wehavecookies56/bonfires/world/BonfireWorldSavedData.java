package uk.co.wehavecookies56.bonfires.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.Bonfires;

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
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        Bonfires.logger.info("Saving Bonfires");
        return bonfires.writeToNBT(compound, bonfires.getBonfires());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        bonfires.readFromNBT(compound, bonfires.getBonfires());
    }

    public boolean addBonfire(Bonfire bonfire) {
        boolean added = bonfires.addBonfire(bonfire);
        markDirty();
        return added;
    }

    public boolean removeBonfire(UUID id) {
        boolean removed = bonfires.removeBonfire(id);
        markDirty();
        return removed;
    }

    public static BonfireWorldSavedData get(World world) {
        MapStorage storage = world.getMapStorage();
        BonfireWorldSavedData instance = (BonfireWorldSavedData) storage.getOrLoadData(BonfireWorldSavedData.class, DATA_NAME);
        if (instance == null) {
            instance = new BonfireWorldSavedData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

}
