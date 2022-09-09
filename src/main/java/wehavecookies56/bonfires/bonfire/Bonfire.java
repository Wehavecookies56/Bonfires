package wehavecookies56.bonfires.bonfire;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class Bonfire {

    private String name;
    private UUID id;
    private UUID owner;
    private BlockPos pos;
    ResourceKey<Level> dimension;
    boolean isPublic;

    public Bonfire(String name, UUID id, UUID owner, BlockPos pos, ResourceKey<Level> dimension, boolean isPublic) {
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.pos = pos;
        this.dimension = dimension;
        this.isPublic = isPublic;
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

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
