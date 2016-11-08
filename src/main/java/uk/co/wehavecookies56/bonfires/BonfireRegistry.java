package uk.co.wehavecookies56.bonfires;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class BonfireRegistry {

    public static final BonfireRegistry INSTANCE = new BonfireRegistry();

    public Map<UUID, Bonfire> bonfires;

    public BonfireRegistry() {
        bonfires = new HashMap<>();
    }

    public Map<UUID, Bonfire> getBonfires() {
        return bonfires;
    }

    public void setBonfires(Map<UUID, Bonfire> bonfires) {
        this.bonfires = bonfires;
    }

    public boolean removeBonfire(UUID id) {
        if (bonfires.containsKey(id)) {
            bonfires.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addBonfire(Bonfire bonfire) {
        if (bonfires.containsKey(bonfire.getId())) {
            System.out.print("Attempted to register bonfire with UUID: " + bonfire.getId().toString());
            return false;
        } else {
            bonfires.put(bonfire.getId(), bonfire);
            System.out.print("Successfully registered bonfire with UUID: " + bonfire.getId().toString());
            return true;
        }
    }

    public Bonfire getBonfire(UUID id) {
        if (bonfires.containsKey(id)) {
            return bonfires.get(id);
        } else {
            return null;
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        Iterator<Map.Entry<UUID, Bonfire>> it = bonfires.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Bonfire> pair = (Map.Entry<UUID, Bonfire>) it.next();
            NBTTagCompound bonfireCompound = new NBTTagCompound();
            bonfireCompound.setUniqueId("ID:"+pair.getKey().toString(), pair.getValue().getId());
            bonfireCompound.setString("NAME:"+pair.getKey().toString(), pair.getValue().getName());
            bonfireCompound.setUniqueId("OWNER:"+pair.getKey().toString(), pair.getValue().getOwner());
            bonfireCompound.setBoolean("PUBLIC:"+pair.getKey().toString(), pair.getValue().isPublic());
            bonfireCompound.setInteger("DIM:"+pair.getKey().toString(), pair.getValue().getDimension());
            bonfireCompound.setDouble("POSX:"+pair.getKey().toString(), pair.getValue().getPos().getX());
            bonfireCompound.setDouble("POSY:"+pair.getKey().toString(), pair.getValue().getPos().getY());
            bonfireCompound.setDouble("POSZ:"+pair.getKey().toString(), pair.getValue().getPos().getZ());
            tagCompound.setTag(pair.getKey().toString(), bonfireCompound);
            System.out.println("Wrote bonfire to NBT " + pair.getKey().toString());
        }
        return tagCompound;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        for (String key : tagCompound.getKeySet()) {
            NBTTagCompound compound = tagCompound.getCompoundTag(key);
            String name = compound.getString("NAME:"+key);
            UUID id = compound.getUniqueId("ID:"+key);
            UUID owner = compound.getUniqueId("OWNER:"+key);
            BlockPos pos = new BlockPos(compound.getDouble("POSX:"+key), compound.getDouble("POSY:"+key), compound.getDouble("POSZ:"+key));
            int dimension = compound.getInteger("DIM:"+key);
            boolean isPublic = compound.getBoolean("PUBLIC:"+key);
            Bonfire bonfire = new Bonfire(name, id, owner, pos, dimension, isPublic);
            bonfires.put(id, bonfire);
            System.out.println("Loaded bonfire from NBT " + key);
        }
    }

}
