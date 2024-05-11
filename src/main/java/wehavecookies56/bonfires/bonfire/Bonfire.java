package wehavecookies56.bonfires.bonfire;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

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
    ResourceKey<Level> dimension;
    boolean isPublic;

    Instant timeCreated;

    public Bonfire(CompoundTag tag) {
        deserializeNBT(tag);
    }

    public Bonfire(String name, UUID id, UUID owner, BlockPos pos, ResourceKey<Level> dimension, boolean isPublic, Instant timeCreated) {
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

    public Instant getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public CompoundTag serializeNBT() {
        CompoundTag bonfireCompound = new CompoundTag();
        bonfireCompound.putUUID("ID", getId());
        bonfireCompound.putString("NAME", getName());
        bonfireCompound.putUUID("OWNER", getOwner());
        bonfireCompound.putBoolean("PUBLIC", isPublic());
        bonfireCompound.putString("DIM", getDimension().location().toString());
        bonfireCompound.putDouble("POSX", getPos().getX());
        bonfireCompound.putDouble("POSY", getPos().getY());
        bonfireCompound.putDouble("POSZ", getPos().getZ());
        CompoundTag timeCompound = new CompoundTag();
        timeCompound.putLong("SECOND", getTimeCreated().getEpochSecond());
        timeCompound.putInt("NANO", getTimeCreated().getNano());
        bonfireCompound.put("TIME", timeCompound);
        return bonfireCompound;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.id = tag.getUUID("ID");
        this.name = tag.getString("NAME");
        this.owner = tag.getUUID("OWNER");
        this.isPublic = tag.getBoolean("PUBLIC");
        this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("DIM")));
        this.pos = new BlockPos((int) tag.getDouble("POSX"), (int) tag.getDouble("POSY"), (int) tag.getDouble("POSZ"));
        CompoundTag timeTag = tag.getCompound("TIME");
        this.timeCreated = Instant.ofEpochSecond(timeTag.getLong("SECOND"), timeTag.getInt("NANO"));
    }

    public static final StreamCodec<FriendlyByteBuf, Bonfire> STREAM_CODEC = StreamCodec.of((byteBuf, bonfire) -> byteBuf.writeNbt(bonfire.serializeNBT()), byteBuf -> new Bonfire(byteBuf.readNbt()));
}
