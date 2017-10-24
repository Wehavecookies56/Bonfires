package uk.co.wehavecookies56.bonfires;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Toby on 07/11/2016.
 */
public class BonfireRegistry {

    private Map<UUID, Bonfire> bonfires;

    public BonfireRegistry() {
        bonfires = new HashMap<>();
    }

    public Map<UUID, Bonfire> getBonfires() {
        return bonfires;
    }

    public void clearBonfires() {
        Bonfires.logger.info("Bonfires cleared");
        bonfires.clear();
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
        return getBonfires().values().stream().filter(bonfire -> bonfire.getOwner().compareTo(owner) == 0).collect(Collectors.toList());
    }

    public List<Bonfire> getBonfiresByName(String name) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());

    }

    public List<Bonfire> getBonfiresInRadius(BlockPos pos, int radius, int dimension) {
        return getBonfiresByDimension(dimension).stream().filter(bonfire -> {
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
            return ((fx <= tx && fx >= bx) && (fz <= tz && fz >= bz) && (fy <= ty && fy >= by));
        }).collect(Collectors.toList());
    }

    private List<Bonfire> getBonfiresByPublic(boolean isPublic) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.isPublic == isPublic).collect(Collectors.toList());
    }

    private List<Bonfire> getPrivateBonfiresByOwner(UUID owner) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getOwner().compareTo(owner) == 0 && !bonfire.isPublic).collect(Collectors.toList());
    }

    private List<Bonfire> getBonfiresByPublicPerDimension(boolean isPublic, int dim) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getDimension() == dim && bonfire.isPublic).collect(Collectors.toList());
    }

    private List<Bonfire> getPrivateBonfiresByOwnerPerDimension(UUID owner, int dim) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getOwner().compareTo(owner) == 0 && !bonfire.isPublic && bonfire.getDimension() == dim).collect(Collectors.toList());

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
        return getBonfires().values().stream().filter(bonfire -> bonfire.getDimension() == dimension).collect(Collectors.toList());
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

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound, Map<UUID, Bonfire> bonfires) {
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

    public void readFromNBT(NBTTagCompound tagCompound, Map<UUID, Bonfire> bonfires) {
        //bonfires.clear();
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
