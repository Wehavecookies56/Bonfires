package uk.co.wehavecookies56.bonfires;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.*;

/**
 * Created by Toby on 07/11/2016.
 */
public class BonfireRegistry {

    public static BonfireRegistry INSTANCE = new BonfireRegistry();

    private static Map<UUID, Bonfire> bonfires;

    private BonfireRegistry() {
        bonfires = new HashMap<>();
    }

    public Map<UUID, Bonfire> getBonfires() {
        return ImmutableMap.copyOf(bonfires);
    }

    public void clearBonfires() {
        Bonfires.logger.info("Bonfires cleared");
        bonfires.clear();
    }

    public void setBonfires(Map<UUID, Bonfire> bonfires) {
        BonfireRegistry.bonfires = bonfires;
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
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getOwner().compareTo(owner) == 0) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public List<Bonfire> getBonfiresByName(String name) {
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getName().toLowerCase().contains(name.toLowerCase())) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public List<Bonfire> getBonfiresInRadius(BlockPos pos, int radius, int dimension) {
        List<Bonfire> list = getBonfiresByDimension(dimension);
        bonfires.forEach((id, bonfire) -> {
            int cx = pos.getX();
            int cz = pos.getZ();
            int cy = pos.getY();
            int fx = bonfire.getPos().getX();
            int fz = bonfire.getPos().getZ();
            int fy = bonfire.getPos().getY();
            int tx = cx + radius;
            int bx = cx - radius;
            int tz = cz + radius;
            int bz = cz - radius;
            int ty = cy + radius;
            int by = cy - radius;
            if (!((fx <= tx && fx >= bx) && (fz <= tz && fz >= bz) && (fy <= ty && fy >= by))) {
                list.remove(bonfire);
            }
        });
        return list;
    }

    private List<Bonfire> getBonfiresByPublic(boolean isPublic) {
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.isPublic() == isPublic) {
                list.add(bonfire);
            }
        });
        return list;
    }

    private List<Bonfire> getPrivateBonfiresByOwner(UUID owner) {
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getOwner().compareTo(owner) == 0 && !bonfire.isPublic) {
                list.add(bonfire);
            }
        });
        return list;
    }

    private List<Bonfire> getBonfiresByPublicPerDimension(boolean isPublic, int dim) {
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.isPublic() == isPublic && bonfire.getDimension() == dim) {
                list.add(bonfire);
            }
        });
        return list;
    }

    private List<Bonfire> getPrivateBonfiresByOwnerPerDimension(UUID owner, int dim) {
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getOwner().compareTo(owner) == 0 && !bonfire.isPublic && bonfire.getDimension() == dim) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public List<Bonfire> getPrivateBonfiresByOwnerAndPublicPerDimension(UUID owner, int dim) {
        List<Bonfire> list = new ArrayList<>();
        list.addAll(getBonfiresByPublicPerDimension(true, dim));
        list.addAll(getPrivateBonfiresByOwnerPerDimension(owner, dim));
        return list;
    }

    public List<Bonfire> getPrivateBonfiresByOwnerAndPublic(UUID owner) {
        List<Bonfire> list = new ArrayList<>();
        list.addAll(getBonfiresByPublic(true));
        list.addAll(getPrivateBonfiresByOwner(owner));
        return list;
    }

    public List<Bonfire> getBonfiresByDimension(int dimension) {
        List<Bonfire> list = new ArrayList<>();
        bonfires.forEach((id, bonfire) -> {
            if (bonfire.getDimension() == dimension) {
                list.add(bonfire);
            }
        });
        return list;
    }

    public boolean addBonfire(Bonfire bonfire) {
        if (bonfires.containsKey(bonfire.getId())) {
            return false;
        } else {
            bonfires.put(bonfire.getId(), bonfire);
            return true;
        }
    }

    public Bonfire getBonfire(UUID id) {
        return bonfires.getOrDefault(id, null);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        for (Map.Entry<UUID, Bonfire> pair : bonfires.entrySet()) {
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
        }
    }

}
