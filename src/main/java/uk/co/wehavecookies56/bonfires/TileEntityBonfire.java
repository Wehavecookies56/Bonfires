package uk.co.wehavecookies56.bonfires;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class TileEntityBonfire extends TileEntity implements ITickable {

    boolean bonfire;
    boolean lit;
    String name = "none";
    BlockPos pos = new BlockPos(0, 0, 0);
    UUID id = UUID.randomUUID();

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("bonfire", bonfire);
        compound.setBoolean("lit", lit);
        compound.setString("name", name);
        compound.setInteger("posX", pos.getX());
        compound.setInteger("posY", pos.getY());
        compound.setInteger("posZ", pos.getZ());
        compound.setUniqueId("id", id);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        bonfire = compound.getBoolean("bonfire");
        lit = compound.getBoolean("lit");
        name = compound.getString("name");
        pos = new BlockPos(compound.getInteger("posX"), compound.getInteger("posY"), compound.getInteger("posZ"));
        id = compound.getUniqueId("id");
    }

    public boolean isBonfire() {
        return bonfire;
    }

    public void setBonfire(boolean bonfire) {
        this.bonfire = bonfire;
        markDirty();
    }

    public boolean isLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
        markDirty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        markDirty();
    }

    public BlockPos getBonfirePos() {
        return pos;
    }

    public void setBonfirePos(BlockPos pos) {
        this.pos = pos;
        markDirty();
    }

    public void setBonfirePos(int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
        markDirty();
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
        markDirty();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void update() {

    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), 1, compound);
    }
}
