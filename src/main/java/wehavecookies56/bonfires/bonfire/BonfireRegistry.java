package wehavecookies56.bonfires.bonfire;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.time.Instant;
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

    public List<Bonfire> getBonfiresInRadius(BlockPos pos, int radius, ResourceLocation dimension) {
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

    public Bonfire getBonfireAtPos(BlockPos pos, RegistryKey<World> dim) {
        List<Bonfire> result = getBonfiresByDimension(dim.location()).stream().filter(bonfire -> pos.equals(bonfire.getPos())).collect(Collectors.toList());
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    private List<Bonfire> getBonfiresByPublic(boolean isPublic) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.isPublic == isPublic).collect(Collectors.toList());
    }

    private List<Bonfire> getPrivateBonfiresByOwner(UUID owner) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getOwner().compareTo(owner) == 0 && !bonfire.isPublic).collect(Collectors.toList());
    }

    private List<Bonfire> getBonfiresByPublicPerDimension(boolean isPublic, ResourceLocation dim) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getDimension().location().equals(dim) && bonfire.isPublic).collect(Collectors.toList());
    }

    private List<Bonfire> getPrivateBonfiresByOwnerPerDimension(UUID owner, ResourceLocation dim) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getOwner().compareTo(owner) == 0 && !bonfire.isPublic && bonfire.getDimension().location().equals(dim)).collect(Collectors.toList());

    }

    public List<Bonfire> getPrivateBonfiresByOwnerAndPublicPerDimension(UUID owner, ResourceLocation dim) {
        List<Bonfire> list = new ArrayList<>();
        list.addAll(getBonfiresByPublicPerDimension(true, dim));
        list.addAll(getPrivateBonfiresByOwnerPerDimension(owner, dim));
        return list;
    }

    public static List<Bonfire> sortBonfiresByTime(List<Bonfire> bonfiresToSort) {
        List<Bonfire> sortedList = new ArrayList<>(bonfiresToSort);
        sortedList.sort(Comparator.comparing(Bonfire::getTimeCreated));
        return sortedList;
    }

    public List<Bonfire> getPrivateBonfiresByOwnerAndPublic(UUID owner) {
        List<Bonfire> list = new ArrayList<>();
        list.addAll(getBonfiresByPublic(true));
        list.addAll(getPrivateBonfiresByOwner(owner));
        return list;
    }

    public List<Bonfire> getBonfiresByDimension(ResourceLocation dimension) {
        return getBonfires().values().stream().filter(bonfire -> bonfire.getDimension().location().equals(dimension)).collect(Collectors.toList());
    }

    public boolean addBonfire(Bonfire bonfire) {
        if (bonfires.containsKey(bonfire.getId()) || getBonfireAtPos(bonfire.getPos(), bonfire.getDimension()) != null) {
            return false;
        } else {
            bonfires.put(bonfire.getId(), bonfire);
            return true;
        }
    }

    public Bonfire getBonfire(UUID id) {
        return bonfires.getOrDefault(id, null);
    }

    public CompoundNBT writeToNBT(CompoundNBT tagCompound, Map<UUID, Bonfire> bonfires) {
        for (Map.Entry<UUID, Bonfire> pair : bonfires.entrySet()) {
            CompoundNBT bonfireCompound = new CompoundNBT();
            bonfireCompound.putUUID("ID", pair.getValue().getId());
            bonfireCompound.putString("NAME", pair.getValue().getName());
            bonfireCompound.putUUID("OWNER", pair.getValue().getOwner());
            bonfireCompound.putBoolean("PUBLIC", pair.getValue().isPublic());
            bonfireCompound.putString("DIM", pair.getValue().getDimension().location().toString());
            bonfireCompound.putDouble("POSX", pair.getValue().getPos().getX());
            bonfireCompound.putDouble("POSY", pair.getValue().getPos().getY());
            bonfireCompound.putDouble("POSZ", pair.getValue().getPos().getZ());
            CompoundNBT timeCompound = new CompoundNBT();
            timeCompound.putLong("SECOND", pair.getValue().getTimeCreated().getEpochSecond());
            timeCompound.putInt("NANO", pair.getValue().getTimeCreated().getNano());
            bonfireCompound.put("TIME", timeCompound);
            tagCompound.put(pair.getKey().toString(), bonfireCompound);
        }
        return tagCompound;
    }

    public void readFromNBT(CompoundNBT tagCompound, Map<UUID, Bonfire> bonfires) {
        for (String key : tagCompound.getAllKeys()) {
            CompoundNBT compound = tagCompound.getCompound(key);
            String name = compound.getString("NAME");
            UUID id = compound.getUUID("ID");
            UUID owner = compound.getUUID("OWNER");
            BlockPos pos = new BlockPos(compound.getDouble("POSX"), compound.getDouble("POSY"), compound.getDouble("POSZ"));
            RegistryKey<World> dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(compound.getString("DIM")));
            boolean isPublic = compound.getBoolean("PUBLIC");
            Instant time;
            if (compound.contains("TIME", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT timeCompound = compound.getCompound("TIME");
                time = Instant.ofEpochSecond(timeCompound.getLong("SECOND"), timeCompound.getInt("NANO"));
            } else {
                time = Instant.now();
            }
            Bonfire bonfire = new Bonfire(name, id, owner, pos, dimension, isPublic, time);
            if (getBonfireAtPos(pos, dimension) == null) {
                bonfires.put(id, bonfire);
            }
        }
    }

}
