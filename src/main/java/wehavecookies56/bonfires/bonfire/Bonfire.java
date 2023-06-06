package wehavecookies56.bonfires.bonfire;

import net.minecraft.util.RegistryKey;
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
}
