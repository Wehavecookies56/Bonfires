package uk.co.wehavecookies56.bonfires;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import java.util.*;

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

    public List<Bonfire> getBonfiresByOwner(UUID owner) {
        List<Bonfire> list = new ArrayList<Bonfire>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getId().equals(owner)) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public List<Bonfire> getBonfiresByName(String name) {
        List<Bonfire> list = new ArrayList<Bonfire>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getName().toLowerCase().equals(name.toLowerCase())) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public List<Bonfire> getBonfiresByPublic(boolean isPublic) {
        List<Bonfire> list = new ArrayList<Bonfire>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.isPublic() == isPublic) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public List<Bonfire> getBonfiresByDimension(int dimension) {
        List<Bonfire> list = new ArrayList<Bonfire>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getDimension() == dimension) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public boolean addBonfire(Bonfire bonfire) {
        if (bonfires.containsKey(bonfire.getId())) {
            System.out.println("Attempted to register bonfire with UUID: " + bonfire.getId().toString());
            return false;
        } else {
            bonfires.put(bonfire.getId(), bonfire);
            System.out.println("Successfully registered bonfire with UUID: " + bonfire.getId().toString());
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
            bonfireCompound.setUniqueId("ID", pair.getValue().getId());
            bonfireCompound.setString("NAME", pair.getValue().getName());
            bonfireCompound.setUniqueId("OWNER", pair.getValue().getOwner());
            bonfireCompound.setBoolean("PUBLIC", pair.getValue().isPublic());
            bonfireCompound.setInteger("DIM", pair.getValue().getDimension());
            bonfireCompound.setDouble("POSX", pair.getValue().getPos().getX());
            bonfireCompound.setDouble("POSY", pair.getValue().getPos().getY());
            bonfireCompound.setDouble("POSZ", pair.getValue().getPos().getZ());
            tagCompound.setTag(pair.getKey().toString(), bonfireCompound);
            System.out.println("Wrote bonfire to NBT " + pair.getKey().toString());
        }
        return tagCompound;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        bonfires.clear();
        for (String key : tagCompound.getKeySet()) {
            NBTTagCompound compound = tagCompound.getCompoundTag(key);
            String name = compound.getString("NAME");
            UUID id = compound.getUniqueId("ID");
            UUID owner = compound.getUniqueId("OWNER");
            BlockPos pos = new BlockPos(compound.getDouble("POSX"), compound.getDouble("POSY"), compound.getDouble("POSZ"));
            int dimension = compound.getInteger("DIM");
            boolean isPublic = compound.getBoolean("PUBLIC");
            Bonfire bonfire = new Bonfire(name, id, owner, pos, dimension, isPublic);
            bonfires.put(id, bonfire);
            System.out.println("Loaded bonfire from NBT " + key);
        }
    }

}
