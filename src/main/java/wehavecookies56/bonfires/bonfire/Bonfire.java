package wehavecookies56.bonfires.bonfire;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.time.Instant;
import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class Bonfire {

    private String name;
    private UUID id;
    private UUID owner;
    private BlockPos pos;
    RegistryKey<World> dimension;
    boolean isPublic;

    Instant timeCreated;

    public Bonfire(NbtCompound tag) {
        deserializeNBT(tag);
    }

    public Bonfire(String name, UUID id, UUID owner, BlockPos pos, RegistryKey<World> dimension, boolean isPublic, Instant timeCreated) {
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.pos = pos;
        this.dimension = dimension;
        this.isPublic = isPublic;
        this.timeCreated = timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public RegistryKey<World> getDimension() {
        return dimension;
    }

    public void setDimension(RegistryKey<World> dimension) {
        this.dimension = dimension;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Instant getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public NbtCompound serializeNBT() {
        NbtCompound bonfireCompound = new NbtCompound();
        bonfireCompound.putUuid("ID", getId());
        bonfireCompound.putString("NAME", getName());
        bonfireCompound.putUuid("OWNER", getOwner());
        bonfireCompound.putBoolean("PUBLIC", isPublic());
        bonfireCompound.putString("DIM", getDimension().getValue().toString());
        bonfireCompound.putDouble("POSX", getPos().getX());
        bonfireCompound.putDouble("POSY", getPos().getY());
        bonfireCompound.putDouble("POSZ", getPos().getZ());
        NbtCompound timeCompound = new NbtCompound();
        timeCompound.putLong("SECOND", getTimeCreated().getEpochSecond());
        timeCompound.putInt("NANO", getTimeCreated().getNano());
        bonfireCompound.put("TIME", timeCompound);
        return bonfireCompound;
    }

    public void deserializeNBT(NbtCompound tag) {
        this.id = tag.getUuid("ID");
        this.name = tag.getString("NAME");
        this.owner = tag.getUuid("OWNER");
        this.isPublic = tag.getBoolean("PUBLIC");
        this.dimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(tag.getString("DIM")));
        this.pos = new BlockPos((int) tag.getDouble("POSX"), (int) tag.getDouble("POSY"), (int) tag.getDouble("POSZ"));
        NbtCompound timeTag = tag.getCompound("TIME");
        this.timeCreated = Instant.ofEpochSecond(timeTag.getLong("SECOND"), timeTag.getInt("NANO"));
    }
}
