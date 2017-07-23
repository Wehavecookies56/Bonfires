package uk.co.wehavecookies56.bonfires.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.wehavecookies56.bonfires.*;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class TileEntityBonfire extends TileEntity implements ITickable {

    boolean bonfire;
    boolean lit;
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
        compound.setUniqueId("id", id);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        bonfire = compound.getBoolean("bonfire");
        lit = compound.getBoolean("lit");
        id = compound.getUniqueId("id");
    }

    public void createBonfire(String name, UUID id, UUID owner, boolean isPublic) {
        Bonfire bonfire = new Bonfire(name, id, owner, this.getPos(), this.getWorld().provider.getDimension(), isPublic);
        BonfireWorldSavedData.get(getWorld()).addBonfire(bonfire);
    }

    public void destroyBonfire(UUID id) {
        BonfireWorldSavedData.get(getWorld()).removeBonfire(id);
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
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

    @Nullable
    @SideOnly(Side.CLIENT)
    @Override
    public ITextComponent getDisplayName() {
        if (!Minecraft.getMinecraft().player.isSneaking()) {
            if (getID() != null && BonfiresConfig.renderTextAboveBonfire) {
                if (BonfireRegistry.INSTANCE.getBonfire(getID()) != null) {
                    if (BonfireRegistry.INSTANCE.getBonfire(getID()).isPublic()) {
                        return new TextComponentTranslation(BonfireRegistry.INSTANCE.getBonfire(getID()).getName());
                    } else {
                        return new TextComponentTranslation(LocalStrings.TILEENTITY_BONFIRE_LABEL, BonfireRegistry.INSTANCE.getBonfire(getID()).getName());
                    }
                }
            }
        } else {
            //TODO Souls, return new TextComponentTranslation("Souls: 1000");
        }
        return null;
    }

}
